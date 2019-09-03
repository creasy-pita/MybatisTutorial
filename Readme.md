
##1 chapter01
    引入mybatis,mysql connector 依赖
    database setup   创建 student表
    MYBATIS - Configuration XML
## chapter02
springconfig.xml 中 dataSource 不同  mybatis-spring 版本 实现类不同


错误记录：
    1 SqlMapConfig.xml 中的配置出错时 mybatis config的 environment 不能加载 提示 nullpointexception
    <environments default = "development">
        <environment id = "development">
    2  
    错误：Error updating database.  Cause: java.lang.IllegalArgumentException: Mapped Statements collection does not contain value for Student.insert
    原因：
        SqlSession session.insert("Student.insert", student);
        使用 namespace=Student
        而不是 com.creasypita.learning.mybatis.domain.Student
    修改：    
    Student.xml 中 <mapper namespace = "com.creasypita.learning.mybatis.domain.Student">
     namespace 修改为  <mapper namespace = "Student">
    
    3 错误： Cause: org.apache.ibatis.executor.ExecutorException: No constructor found in com.creasypita.learning.mybatis.domain.Student matching [java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, java.lang.String]
      ### The error may exist in mybatis/Student.xml
      ### The error may involve Student.selectById
      ### The error occurred while handling results
      ### SQL: SELECT * FROM STUDENT WHERE id=?;
      可能原因：
        selectOne 创建 student 实例时 使用  无参构造方法 并 使用各属性的 setters 来完成整个创建操作
        Student s = session.selectOne("Student.selectById",1); 
      修改
        加入 无参构造方法 
      