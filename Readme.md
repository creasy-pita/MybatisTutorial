
##1 chapter01
    引入mybatis,mysql connector 依赖
    database setup   创建 student表
    MYBATIS - Configuration XML


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