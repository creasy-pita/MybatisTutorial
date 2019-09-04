
##1 chapter01
    引入mybatis,mysql connector 依赖
    database setup   创建 student表
    MYBATIS - Configuration XML
## chapter02
springconfig.xml 中 dataSource 不同  mybatis-spring 版本 实现类不同
+ 1 add the dependencies which are required by java spring + mybatis application
+ 2 Modify web.xml
    use org.springframework.web.servlet.DispatcherServlet
    set the init spring-config.xml 
+ 3 add  
<mvc:annotation-driven />  
<context:component-scan base-package="com.github.elizabetht" />
 to indicate that the application is mvc annoation dirven and base-package  for the context component scan
 
+ 4 the bean InternalResourceViewResolver of Spring to locate the jsp files
 <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver">
   <property name="prefix" value="/WEB-INF/jsp/" />
   <property name="suffix" value=".jsp" />
 </bean>  
 
+ 5 add mysql dirven datasource 
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver" />
        <property name="url" value="jdbc:mysql://localhost:3306/huangyongsmartbookcodedemo" />
        <property name="username" value="huangyongsmartbookcodedemo" />
        <property name="password" value="root" />
    </bean>
+ 6 Include the bean for transaction manager for scoping/controlling the transactions, that takes the data source defined above as reference (dependent)
    <tx:annotation-driven transaction-manager="transactionManager" />
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource" />
    </bean>
+ 7 The MyBatis specific configurations
    6.1 include the bean for sqlSessionFactory which is the central configuration in a MyBatis application.
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
      <property name="dataSource" ref="dataSource" />
      <property name="typeAliasesPackage" value="com.github.elizabetht.model"/>
      <property name="mapperLocations" value="classpath*:com/github/elizabetht/mappers/*.xml" />
    </bean>
    
    the mapperLocations property will not used for the mapper with scan by mybatis:scan node
+ 8 Include the bean for sqlSession
  <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
    <constructor-arg index="0" ref="sqlSessionFactory" />
  </bean>
    
+ 9 mybatis scan
  <mybatis:scan base-package="com.creasypita.learning.mappers"/>

#### url
get:/studentlist    进入student列表页面    studentList.jsp
get:/student_view   查看studnet           student_view.jsp
get:/student_insert 进入插入student页面       student_insert.jsp
post:/student_insert    插入student       studentList.jsp
get:/student_update?id={id} 进入更新student页面   student_update.jsp
post:/student_post?id={id}  更新student           studentList.jsp
delete:/student_delete?id={id} 删除student    studnetList.jsp

jsp: 
    studentList.jsp
    student_insert.jsp
    student_update.jsp
    student_view.jsp

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
        
    4 错误：
    org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'cacheManager' is defined
    原因
    修改
        参考： https://stackoverflow.com/questions/24816502/cachemanager-no-bean-found-not-trying-to-setup-any-cache
    5 错误 The matching wildcard is strict, but no declaration can be found for element 'mvc:annotation-driven'
      参考： https://stackoverflow.com/questions/15406231/no-declaration-can-be-found-for-element-mvcannotation-driven
    6 错误：org.springframework.beans.factory.NoSuchBeanDefinitionException: No matching bean of type [com.creasypita.learning.mappers.StudentMapper] found for dependency: expected at least 1 bean which qualifies as autowire candidate for this dependency. Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}
    7     org.springframework  spring-jdbc 没有引入 
     通过    
     <dependency>
               <groupId>org.springframework</groupId>
               <artifactId>spring-jdbc</artifactId>
               <version>5.1.8.RELEASE</version>
           </dependency>
     强制引入	
     8 
     error: MalformedByteSequenceException: Invalid byte 2 of 2-byte UTF-8 sequence
     reason:
        1 xml not saved in utf-8
        2  如下    mapperLocations 找不到xml 而去找其他文件格式的文件 时 解析出错 
            <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
                <property name="dataSource" ref="dataSource" />
                <property name="typeAliasesPackage" value="com.creasypita.learning.model"/>
                <property name="mapperLocations" value="classpath:com/creasypita/learning/mappers/*" />
            </bean>
            修改为
            <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
                  <property name="dataSource" ref="dataSource" />
                  <property name="typeAliasesPackage" value="com.creasypita.learning.model"/>
                  <!--<property name="mapperLocations" value="classpath:com/creasypita/learning/mappers/*" />-->
              </bean>
            reference : https://stackoverflow.com/questions/9920758/malformedbytesequenceexception-invalid-byte-2-of-2-byte-utf-8-sequence
              
     