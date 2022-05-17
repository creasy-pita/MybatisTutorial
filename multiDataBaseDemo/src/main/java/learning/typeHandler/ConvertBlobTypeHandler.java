package learning.typeHandler;

import org.apache.ibatis.type.*;

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
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            if (jdbcType == null) {
                throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            }
            try {
                if ("postgres".equals(dataType)) {
                    //单独处理postgres setnull的方式
                    ps.setNull(i, Types.OTHER);
                }
                else {
                    ps.setNull(i, jdbcType.TYPE_CODE);
                }
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . " +
                        "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. " +
                        "Cause: " + e, e);
            }
        } else {
            try {
                setNonNullParameter(ps, i, parameter, jdbcType);
            } catch (Exception e) {
                throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . " +
                        "Try setting a different JdbcType for this parameter or a different configuration property. " +
                        "Cause: " + e, e);
            }
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        ByteArrayInputStream bis;
        byte[] bytes = parameter.getBytes(StandardCharsets.UTF_8);
        bis = new ByteArrayInputStream(bytes);
        ps.setBinaryStream(i, bis, bytes.length);
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
            byte[] bytes = rs.getBytes(columnName);
            if (null == bytes)
                return null;
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


