package learning.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * 自定义typehandler，解决mybatis存储blob字段后，出现乱码的问题
 * 配置mapper.xml： <result typeHandler="com.gisquest.platform.common.utils.ConvertBlobTypeHandler"/>
 * 2017-05-23
 * @author lengbj
 *
 */
@MappedJdbcTypes({JdbcType.BLOB, JdbcType.BINARY, JdbcType.LONGVARBINARY})
@MappedTypes({String.class})
public class ConvertBlobTypeHandler extends BaseTypeHandler<String> {
    //指定字符集
    private static final String DEFAULT_CHARSET = "utf-8";

    private static String dataType = "postgres";


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        ByteArrayInputStream bis;
        byte[] bytes = parameter.getBytes(StandardCharsets.UTF_8);
        bis = new ByteArrayInputStream(bytes);
        ps.setBinaryStream(i, bis, parameter.length());
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if(dataType.contains("oracle")){
            byte[] bytes = rs.getBytes(columnName);
            if (null == bytes)
                return null;
            return new String(bytes, StandardCharsets.UTF_8);
        }
        if(dataType.contains("postgres")){
            return new String(rs.getBytes(columnName), StandardCharsets.UTF_8);
        }
        Blob blob = rs.getBlob(columnName);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1, (int) blob.length());
            return new String(returnValue, StandardCharsets.UTF_8);
        }
        return null;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Blob blob = cs.getBlob(columnIndex);
        byte[] returnValue = null;
        if (null != blob) {
            returnValue = blob.getBytes(1, (int) blob.length());

            try {
                return new String(returnValue, DEFAULT_CHARSET);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Blob Encoding Error!");
            }
        }
        return null;
    }

    @Override
    public String getNullableResult(ResultSet arg0, int arg1) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}


