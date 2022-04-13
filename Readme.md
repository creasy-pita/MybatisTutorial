# 测试mybatis的typeHandler


## 2022-04-08
## java boolean字段处理

- javatype=java.lang.Boolean,jdbctype=bit,后端mysql使用tinyint,pg使用bool,oracle使用Nubmer(1,0) 可以正确转化
- javatype=java.lang.Boolean,jdbctype=INTEGER,后端mysql使用tinyint,pg使用bool,oracle使用Nubmer(1,0) 可以正确转化
- javatype=java.lang.Boolean,jdbctype=bit,后端mysql使用tinyint,oracle使用Nubmer(1,0) 可以正确转化;后端pg使用numberic(1,0) 不能正常转化 需要使用显示或隐式的转化 cast  boolean to smallint
- javatype=java.lang.Boolean,jdbctype=INTEGER,后端mysql使用tinyint,oracle使用Nubmer(1,0) 可以正确转化;后端pg使用numberic(1,0) 不能正常转化 需要使用显示或隐式的转化 cast  boolean to smallint; boolean to integer
  - 比如 CAST(#{xxx,jdbcType=INTEGER} AS INTEGER)
  
- javatype=java.lang.Boolean,jdbctype=bit,oracle使用varchar2(5)不能正常使用

## jdbc程序使用date字段处理

- javatype=java.util.date,jdbctype=Date 数据库时date,timestamp字段
保存时默认只有年月日，时分秒没有
- javatype=java.util.date,jdbctype=timestamp 数据库时date,timestamp字段 
  保存时默认有年月日时分秒毫秒  

## 2022-04-07
## jdbc程序使用jdbctype clob处理数据库字段blob 
数据库blob字段包括
mysql blob longblob
postgres bytea 
oracle BLOB

- orcale时insert,update不能使用#{value,jdbcType=CLOB}，这是使用ClobTypeHandler,StringReader赋值给oracle中的blob字段显然是不行的； 比如 update BT_SYS_PROPERTY set VALUE= 'aaa'  where id = '123'; 要使用 utl_raw.cast_to_raw('aaa') 才可以
- pg 时insert,update使用了`CAST(#{value,jdbcType=CLOB} AS bytea)`，先也就是 CAST("哈哈" AS bytea)的方式，时正确的，就好比这种sql可以执行，以下可以正确赋值： insert into blobtest (aa) values('哈哈'); insert into blobtest (aa) values(CAST('哈哈' AS bytea));
- mysql时insert,update使用了`#{value,jdbcType=CLOB}`，内部可以自动处理：以下可以正确赋值 insert into blobtest (aa) values('哈哈');

## 2022-03-25

## 使用自定义的BlobTypeHandler测试数据库字段blob到java程序中的String的转化

解决设置log4j日志框架不生效问题
参考： https://stackoverflow.com/questions/36149517/how-to-configure-log4j-in-spring-for-mybatis-sql-queries
