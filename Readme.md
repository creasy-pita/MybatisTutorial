
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
<context:component-scan base-package="com.creasypita.learning" />
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
      <property name="typeAliasesPackage" value="com.creasypita.learning.model"/>
      <property name="mapperLocations" value="classpath*:com/creasypita/learning/mappers/*.xml" />
    </bean>
    
    the mapperLocations property will not used for the mapper with scan by mybatis:scan node
+ 8 Include the bean for sqlSession
  <bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
    <constructor-arg index="0" ref="sqlSessionFactory" />
  </bean>
    
+ 9 mybatis scan
  <mybatis:scan base-package="com.creasypita.learning.mappers"/>

#### url设计
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
    
###chapter03
mapper 采用 interface + xml 形式来配置
    1 scan mapperLocations of xml file 
         <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
              <property name="dataSource" ref="dataSource" />
              <property name="typeAliasesPackage" value="com.creasypita.learning.model"/>
              <property name="mapperLocations" value="classpath*:mybatis/*.xml" />
          </bean>
          
      注释
        typeAliasesPackage  用于确定mybatis sql statement 中使用到的 type,parametertype ...
        他们的名称需要一致
        比如 mybatisStudent.xml 中的配置 type="Student" 与 class com.creasypita.learning.model.Student
        
        如果 不一致会提示 如一下错误：  java.lang.ClassNotFoundException: Cannot find class: Student
    2  scan mapper base package  of interface file
    <mybatis:scan base-package="com.creasypita.learning.mappers"/>
    

### chapter05
spring propagation practise spring 事务传播练习  

    重点 
        @service 注解类中的方法类调用时  spring事务切面方式还是原始调用，只有spring事务切面方式 才有事务传播处理
    
    问题：
        Propagation.REQUIRED 不工作的一个例子
        详细描述：
            testRquired testRquiredNew   写在 studentserviceimpl 中  Propagation.REQUIRED 不生效
            testRquired testRquiredNew   写在 Innserservice 中 然后 注入到 studentserviceimpl  Propagation.REQUIRED 生效
        原因： 写在 studentserviceimpl 中  和 写在 Innserservice 中 testRquired方法 调用的不同之处
               Innserservice的 testRquired方法实际会加入事务切面去执行，所以有事务传播管理；
               而 studentserviceimpl 的 testRquired 方法 只是简单的调用，没有spring的事务传播管理，所以报错也不会引起事务回滚（ insertStudentRequired方法才会加入事务切面去执行）
        解决：
            studentserviceimpl 中 insertStudentRequired，insertStudentRequiredNew 方法 内部调用的方法 都会使用 同一事务，如果有外部service调用则会根据他的事务传播属性来管理
            
    涉及事务传播的调用点
        1 studentcontroller action 中调用 studentservice.insertStudentRequired();
        2 studentservice  调用 insertStudent
        3 studentservice  调用 testRequired
    
        代码段如下， 详见 Studentserviceimpl
        public void insertStudent(Student student) {
            studentMapper.insertStudent(student);
        }
    
        @Transactional(propagation = Propagation.REQUIRED)
        public void insertStudentRequired(Student student)
        {
            insertStudent(student);
            try {
                testRequired();
            } catch (RuntimeException ex) {
            }
        }
    
        @Transactional(propagation = Propagation.REQUIRED)
        public void testRequired()
        {
            throw new RuntimeException("testRequired");
        }



### 一张图理清  spring 事务的大致调用堆栈流程, 
    	1 studentcontroller  调用 注解为@service 的 StudentService的代理即 JdkDynamicAopProxy
    	2 JdkDynamicAopProxy#invoke ：
    	Get the interception chain for this method  List<Object> chain = this.advised.getInterceptorsAndDynamicInterceptionAdvice(method, targetClass); ：事务代理链  最终会通过InvokeJointPoint 调用targetobject  targetmethod
    	3 以 MethodInvocation 组织 proxy 链 ，执行时会按 chain 方式处理以知道 joinpoint
    					// We need to create a method invocation...
    				MethodInvocation invocation =
    						new ReflectiveMethodInvocation(proxy, target, method, args, targetClass, chain);
    				// Proceed to the joinpoint through the interceptor chain.
    				retVal = invocation.proceed();
    	4 spring 事务代理链第一个会是  TransactionInterceptor ，结合 TransactionAttribute TransactionDefinition TransactionAspectSupport  PlatformTransactionManager 等完成事务的管理
    	5 TransactionInterceptor#invoke   
    	6 TransactionAspectSupport #invokeWithinTransaction  
    	7 TransactionAspectSupport # createTransactionIfNecessary
    	8 AbstractPlatformTransactionManager #getTransaction
    	9 DataSourceTransactionManager#doGetTransaction   ： 从上下文中获取之前的事务信息，用于后续的事务处理
    	10 DataSourceTransactionManager#isExistingTransaction：如果存在走 第11,否则跳到 12
    	11 AbstractPlatformTransactionManager#handleExistingTransaction ： 根据本次targetmethod 定义事务属性（比如传播属性）to find out how to behave  ,然后 跳转到13
    	12 之前不存在事务情况， 根据本次targetmethod 定义事务属性，to find out how to behave ,然后跳转到13
    	13 继续 TransactionAspectSupport # invokeWithinTransaction 的后续代码：
    		1 调用下一个 interceptor in the chain
    		2 抛异常时 事务异常处理
    		3 无异常时提交 事务提交并返回上一层
    			try {
    			// This is an around advice: Invoke the next interceptor in the chain.
    			// This will normally result in a target object being invoked.
    			retVal = invocation.proceedWithInvocation();
    			}
    			catch (Throwable ex) {
    				// target invocation exception
    				completeTransactionAfterThrowing(txInfo, ex);
    				throw ex;
    			}
    			finally {
    				cleanupTransactionInfo(txInfo);
    			}
    			commitTransactionAfterReturning(txInfo);
    			return retVal;
    	
    	
    	注： 此处还未添加 Suspending| Unsuspending current transaction 的说明
    
## 错误记录：
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
    6 错误：org.springframework.beans.factory.NoSuchBeanDefinitionException: No matching bean of type [com.creasypita.learning.StudentMapper] found for dependency: expected at least 1 bean which qualifies as autowire candidate for this dependency. Dependency annotations: {@org.springframework.beans.factory.annotation.Autowired(required=true)}
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
              
     