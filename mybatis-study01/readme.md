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



### 5、 分析错误

- 事务手动提交

  ```xml
  <transactionManager type="JDBC"/>
  ```

  jdbc方式的事务，需要手动提交

- xml等资源文件加载问题

  ```log
  java.io.IOException: Could not find resource com/creasypita/learning/mybatis-config.xml
  ```

  

## 配置xml

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

> 这里需要注意有些方式mapper接口，mapper.xml的文件名需要相同

### n、分析错误：

-  Could not resolve type alias

  不能解析这个别名

```log
Cause: org.apache.ibatis.type.TypeException: Could not resolve type alias 'user1'
```

### 