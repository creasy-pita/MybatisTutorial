# mybatis

## 什么是mybatis

持久层框架

特点

- 简单易用
- 代码和sql分离

- 灵活



## 第一个mybatis程序

1 mybatis.xml配置文件

2 maven引入mybatis包和mysql驱动包

3 实体类POJO

4 写dao接口，mapper(xml文件格式的sql映射）

5 编写mybatis数据库访问工具类

6 客户端调用



### n、 分析错误

- 事务手动提交

  ```xml
  <transactionManager type="JDBC"/>
  ```

  jdbc方式的事务，需要手动提交

- xml等资源文件加载问题

  ```log
  java.io.IOException: Could not find resource com/creasypita/learning/mybatis-config.xml
  ```

  

## xml配置

### 1、类型别名

类型别名可为 Java 类型设置一个缩写名字，它仅用于 XML 配置，意在降低冗余的全限定类名书写。这样

在mapper.xml中`returnType`,`parameterType`等就可以配置缩写名称。比如：

```xml
<typeAliases>
  <typeAlias alias="Author" type="domain.blog.Author"/>
</typeAliases>
```

mybatis提供了Java 类型内建的类型别名，比如

| 别名     | 映射类型名 |
| -------- | ---------- |
| map      | HashMap    |
| _integer | int        |
| _int     | int        |



### 2、 设置  setting

**事务管理器（transactionManager）**

jdbc-这个配置直接使用了 JDBC 的提交和回滚设施，它依赖从数据源获得的连接来管理事务作用域

managed – 这个配置几乎没做什么。它从不提交或回滚一个连接，而是让容器来管理事务的整个生命周期（比如 EJB容器上下文,spring 容器上下文）。 默认情况下它会关闭连接。

### 3、 映射器（mappers）



我们需要告诉mybatis到哪里去找定义 SQL 映射语句。

可以使用几种方式：相对于`类路径的资源引用`，或`完全限定资源定位符`（包括 `file:///` 形式的 URL），或`类名`和`包名`等

> 这里需要注意有些方式`mapper`接口，`mapper.xml`的文件名需要相同

### n、分析错误：

-  Could not resolve type alias

  不能解析这个别名

```log
Cause: org.apache.ibatis.type.TypeException: Could not resolve type alias 'user1'
```

## xml映射文件



### 1、结果映射（resultMap）

resultMap配置了数据库中的结果即字段到加载成对象的属性的一个映射关系。默认情况下字段加载成同名的对象属性。ResultMap 的设计思想是，对简单的语句做到零配置，对于复杂一点的语句，只需要描述语句之间的关系就行了

> :a: 注意 它是`结果集`到`JavaBean`的映射，而不是相反

如下情况

```java
public class User implements Serializable {
    private int id;
    private String name;
    private String pwd;
    private int age;
}
```

```xml
<!-- user_age 不能正确映射到user的age字段-->    
	<select id="findUserById" resultType="com.xxx.User">
        select id , name,pwd, user_age as age from User where id = #{id}
    </select>
```

mybatis在select的结果集转为JavaBean默认有一个`resultMap`,把结果集字段名同名的映射到avaBean属性。所以

```xml
    <resultMap id="userMap" type="user">
        <result column="user_age" property="age"/>
    </resultMap>    
<!--借助显示的resultMap字段映射， user_age 正确映射到user的age字段--> 
	<select id="findUserById" resultMap="userMap">
        select id, name,pwd, user_age as age from User where id = #{id}
    </select>
```

## 日志



```xml
<!--mybatis xml配置中的setting-->     
	<settings>
        <setting name="logImpl" value="STDOUT_LOGGING"/>
    </settings>
```

```log
Logging initialized using 'class org.apache.ibatis.logging.stdout.StdOutImpl' adapter.
PooledDataSource forcefully closed/removed all connections.
PooledDataSource forcefully closed/removed all connections.
PooledDataSource forcefully closed/removed all connections.
PooledDataSource forcefully closed/removed all connections.
Opening JDBC Connection
Created connection 490150701.
Setting autocommit to false on JDBC Connection [com.mysql.jdbc.JDBC4Connection@1d371b2d]
==>  Preparing: select id , name,pwd, user_age as age from User where id = ? 
==> Parameters: 1(Integer)
<==    Columns: id, name, pwd, age
<==        Row: 1, a, a, 1
<==      Total: 1
Resetting autocommit to true on JDBC Connection [com.mysql.jdbc.JDBC4Connection@1d371b2d]
Closing JDBC Connection [com.mysql.jdbc.JDBC4Connection@1d371b2d]
Returned connection 490150701 to pool.
```



## 其他

### 1、序列

