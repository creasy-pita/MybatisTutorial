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

  

## 配置

别名优化

输入的类型