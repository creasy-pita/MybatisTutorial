# spring集成mybatis原理分析

1 先搭建一个简单的spring-mybatis环境
2 手动初始而不是webserver的机制去创建spring上下文  applicationcontext
3 直接获取mapper类进行orm操作

PostProcessorRegistrationDelegate类会扫描 BeanDefinitionRegistryPostProcessor的实现类，这里MapperScannerConfigurer就是它的实现类
然后会调用获取这个示例 beanFactory.getBean(MapperScannerConfigurer.class)
然后调用postProcessBeanDefinitionRegistry，当中的scan方法会把mybatis的mapper接口注册Beanfactory中，这样就可以使用applictioncontext.getBean("mappername")获取mapper。


```java
// 通过MapperScannerConfigurer向spring注册mapper
scan:253, ClassPathBeanDefinitionScanner (org.springframework.context.annotation)
postProcessBeanDefinitionRegistry:356, MapperScannerConfigurer (org.mybatis.spring.mapper)
invokeBeanDefinitionRegistryPostProcessors:275, PostProcessorRegistrationDelegate (org.springframework.context.support)
invokeBeanFactoryPostProcessors:125, PostProcessorRegistrationDelegate (org.springframework.context.support)
invokeBeanFactoryPostProcessors:705, AbstractApplicationContext (org.springframework.context.support)
refresh:531, AbstractApplicationContext (org.springframework.context.support)
<init>:144, ClassPathXmlApplicationContext (org.springframework.context.support)
<init>:85, ClassPathXmlApplicationContext (org.springframework.context.support)
main:13, ApplicationTest (learning)
```

```java
// 
getBean:199, AbstractBeanFactory (org.springframework.beans.factory.support) [2]
resolveReference:303, BeanDefinitionValueResolver (org.springframework.beans.factory.support)
resolveValueIfNecessary:110, BeanDefinitionValueResolver (org.springframework.beans.factory.support)
resolveConstructorArguments:662, ConstructorResolver (org.springframework.beans.factory.support)
autowireConstructor:188, ConstructorResolver (org.springframework.beans.factory.support)
autowireConstructor:1341, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
createBeanInstance:1187, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
doCreateBean:555, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
createBean:515, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
lambda$doGetBean$0:320, AbstractBeanFactory (org.springframework.beans.factory.support)
getObject:-1, 210156003 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$19)
getSingleton:222, DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
doGetBean:318, AbstractBeanFactory (org.springframework.beans.factory.support)
// 
getBean:199, AbstractBeanFactory (org.springframework.beans.factory.support) [1]
preInstantiateSingletons:845, DefaultListableBeanFactory (org.springframework.beans.factory.support)
finishBeanFactoryInitialization:877, AbstractApplicationContext (org.springframework.context.support)
refresh:549, AbstractApplicationContext (org.springframework.context.support)
<init>:144, ClassPathXmlApplicationContext (org.springframework.context.support)
<init>:85, ClassPathXmlApplicationContext (org.springframework.context.support)
main:13, ApplicationTest (learning)

```

```java
// AbstractApplicationContext 类中 有一步 初始预留的单例bean // Instantiate all remaining (non-lazy-init) singletons.


public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        // Prepare this context for refreshing.
        prepareRefresh();

        // Tell the subclass to refresh the internal bean factory.
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // Prepare the bean factory for use in this context.
        prepareBeanFactory(beanFactory);

        try {
            // Allows post-processing of the bean factory in context subclasses.
            postProcessBeanFactory(beanFactory);

            // Invoke factory processors registered as beans in the context.
            invokeBeanFactoryPostProcessors(beanFactory);

            // Register bean processors that intercept bean creation.
            registerBeanPostProcessors(beanFactory);

            // Initialize message source for this context.
            initMessageSource();

            // Initialize event multicaster for this context.
            initApplicationEventMulticaster();

            // Initialize other special beans in specific context subclasses.
            onRefresh();

            // Check for listener beans and register them.
            registerListeners();

            // Instantiate all remaining (non-lazy-init) singletons.
            // <1>
            finishBeanFactoryInitialization(beanFactory);

            // Last step: publish corresponding event.
            finishRefresh();
        }


    }
}

```

- <1> 中包括如下单例bean,可以看到mybatsi相关的顺序是  dataSource|transactionManager|sqlSessionFactory|sqlSession|studentMapper
- 所以在初始studentMapper时已经存在 dataSource|transactionManager|sqlSessionFactory|sqlSession了
- 不过即使不存在顺序，也没有关系，studentmapper在设置sqlSession属性时没有会去创建的

**问题  如下的这个顺序是如何处理出来的**

```java
// 包括
beanNames = {ArrayList@2891}  size = 15
 0 = "org.springframework.context.annotation.internalConfigurationAnnotationProcessor"
 1 = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor"
 2 = "org.springframework.context.annotation.internalCommonAnnotationProcessor"
 3 = "org.springframework.context.event.internalEventListenerProcessor"
 4 = "org.springframework.context.event.internalEventListenerFactory"
 5 = "dataSource"
 6 = "org.springframework.transaction.config.internalTransactionalEventListenerFactory"
 7 = "org.springframework.aop.config.internalAutoProxyCreator"
 8 = "org.springframework.transaction.annotation.AnnotationTransactionAttributeSource#0"
 9 = "org.springframework.transaction.interceptor.TransactionInterceptor#0"
 10 = "org.springframework.transaction.config.internalTransactionAdvisor"
 11 = "transactionManager"
 12 = "sqlSessionFactory"
 13 = "sqlSession"
 14 = "studentMapper"
```



### 整个逻辑

引入mybatis-spring包
`ApplicationTest`类调用 `ApplicationContext context = new ClassPathXmlApplicationContext("springConfig.xml")`，会创建spring ApplicationContext
StudentMapper会通过MapperFactoryBean去初始化
创建过程中因为MapperFactoryBean 会被applicationcontext初始化时注册并创建，因为继承了FactoryBean,所以applicationcontext.getBean("studentMapper")时会调用其getObject()方法，实际返回StudentMapper`<1>`
StudentMapper在createBean过程中会注入相关的依赖 `dataSource|transactionManager|sqlSessionFactory|sqlSession`，如果这些依赖的Bean容器中没有，会去按照BeanDefinition创建

- MapperFactoryBean继承了SqlSessionDaoSupport，SqlSessionDaoSupport继承DaoSupport，DaoSupport实现了InitializingBean接口
- SqlSessionFactoryBean这个类实现了三个接口，一个是InitializingBean，另一个是FactoryBean，还有就是ApplicationListener接口。下面说明一下实现了这三个接口的，有什么作用

InitializingBean接口：实现了这个接口，那么当bean初始化的时候，spring就会调用该接口的实现类的afterPropertiesSet方法，去实现当spring初始化该Bean 的时候所需要的逻辑。

FactoryBean接口：实现了该接口的类，在调用getBean的时候会返回该工厂返回的实例对象，也就是再调一次getObject方法返回工厂的实例。

ApplicationListener接口：实现了该接口，如果注册了该监听的话，那么就可以了监听到Spring的一些事件，然后做相应的处理

### applicationContext.getMapper("mappername")的调用栈

```java
newInstance:47, MapperProxyFactory (org.apache.ibatis.binding)
newInstance:52, MapperProxyFactory (org.apache.ibatis.binding)
getMapper:50, MapperRegistry (org.apache.ibatis.binding)
getMapper:779, Configuration (org.apache.ibatis.session)
getMapper:311, SqlSessionTemplate (org.mybatis.spring)
getObject:95, MapperFactoryBean (org.mybatis.spring.mapper)
doGetObjectFromFactoryBean:171, FactoryBeanRegistrySupport (org.springframework.beans.factory.support)
getObjectFromFactoryBean:101, FactoryBeanRegistrySupport (org.springframework.beans.factory.support)
getObjectForBeanInstance:1674, AbstractBeanFactory (org.springframework.beans.factory.support)
getObjectForBeanInstance:1249, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
doGetBean:257, AbstractBeanFactory (org.springframework.beans.factory.support)
getBean:199, AbstractBeanFactory (org.springframework.beans.factory.support)
getBean:1105, AbstractApplicationContext (org.springframework.context.support)
main:14, ApplicationTest (learning)
```

```java
public class ApplicationTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("springConfig.xml");
        //<1>
        StudentMapper studentMapper = ((StudentMapper) context.getBean("studentMapper"));
        System.out.println(studentMapper.GetAll().get(3).getId());
    }
}
// AbstractBeanFactory java
// <2>  容器中获取指定mapper实际借助一个mapperfactorybean，它实现了FactoryBean接口，是一个FactoryBean
protected Object getObjectForBeanInstance(
        Object beanInstance, String name, String beanName, @Nullable RootBeanDefinition mbd) {

    // Don't let calling code try to dereference the factory if the bean isn't a factory.
    if (BeanFactoryUtils.isFactoryDereference(name)) {
        if (beanInstance instanceof NullBean) {
            return beanInstance;
        }
        if (!(beanInstance instanceof FactoryBean)) {
            throw new BeanIsNotAFactoryException(beanName, beanInstance.getClass());
        }
    }

    // Now we have the bean instance, which may be a normal bean or a FactoryBean.
    // If it's a FactoryBean, we use it to create a bean instance, unless the
    // caller actually wants a reference to the factory.
    if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils.isFactoryDereference(name)) {
        return beanInstance;
    }

    Object object = null;
    if (mbd == null) {
        object = getCachedObjectForFactoryBean(beanName);
    }
    if (object == null) {
        // Return bean instance from factory.
        FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
        // Caches object obtained from FactoryBean if it is a singleton.
        if (mbd == null && containsBeanDefinition(beanName)) {
            mbd = getMergedLocalBeanDefinition(beanName);
        }
        boolean synthetic = (mbd != null && mbd.isSynthetic());
        //<2.1>  使用MapperFactoryBean的getObject方法返回一个bean实例
        object = getObjectFromFactoryBean(factory, beanName, !synthetic);
    }
    return object;
}

// FactoryBeanRegistrySupport java

private Object doGetObjectFromFactoryBean(final FactoryBean<?> factory, final String beanName)
        throws BeanCreationException {

    Object object;
    try {
        if (System.getSecurityManager() != null) {
            AccessControlContext acc = getAccessControlContext();
            try {
                object = AccessController.doPrivileged((PrivilegedExceptionAction<Object>) factory::getObject, acc);
            }
            catch (PrivilegedActionException pae) {
                throw pae.getException();
            }
        }
        else {
            // <3>借助FactoryBeanRegistrySupport 中的factory.getObject()
            object = factory.getObject();
        }
    }
    catch (FactoryBeanNotInitializedException ex) {
        throw new BeanCurrentlyInCreationException(beanName, ex.toString());
    }
    catch (Throwable ex) {
        throw new BeanCreationException(beanName, "FactoryBean threw exception on object creation", ex);
    }

    // Do not accept a null value for a FactoryBean that's not fully
    // initialized yet: Many FactoryBeans just return null then.
    if (object == null) {
        if (isSingletonCurrentlyInCreation(beanName)) {
            throw new BeanCurrentlyInCreationException(
                    beanName, "FactoryBean which is currently in creation returned null from getObject");
        }
        object = new NullBean();
    }
    return object;
}
```

```java
// MapperFactoryBean
@Override
public T getObject() throws Exception {
    //<1.1> MapperFactoryBean的会使用getSqlSession().getMapper获取，这个就与原生使用mabatis获取mapper的方式一致了
    //<1.2> MapperFactoryBean有dataSource|transactionManager|sqlSessionFactory|sqlSession等属性，会bean的创建过程中注入，所以这里可以拿到sqlSessio
    return getSqlSession().getMapper(this.mapperInterface);
}
// MapperRegistry java

public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
    final MapperProxyFactory<T> mapperProxyFactory = (MapperProxyFactory<T>) knownMappers.get(type);
    if (mapperProxyFactory == null) {
        throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
    }
    try {
        //<2.1>会借助指定mapperl类型的mapperProxyFactory创建实例，
        return mapperProxyFactory.newInstance(sqlSession);
    } catch (Exception e) {
        throw new BindingException("Error getting mapper instance. Cause: " + e, e);
    }
}
// MapperProxyFactory java
protected T newInstance(MapperProxy<T> mapperProxy) {
    //<3.1> 产生一个mapper代理类，这里的MapperProxy实现了 InvocationHandler，
    return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
}

public T newInstance(SqlSession sqlSession) {
    final MapperProxy<T> mapperProxy = new MapperProxy<>(sqlSession, mapperInterface, methodCache);
    return newInstance(mapperProxy);
}

//MapperProxy

public class MapperProxy<T> implements InvocationHandler, Serializable {

  private static final long serialVersionUID = -6424540398559729838L;
  private final SqlSession sqlSession;
  private final Class<T> mapperInterface;
  private final Map<Method, MapperMethod> methodCache;

  public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache) {
    this.sqlSession = sqlSession;
    this.mapperInterface = mapperInterface;
    this.methodCache = methodCache;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    try {
      if (Object.class.equals(method.getDeclaringClass())) {
        return method.invoke(this, args);
      } else if (method.isDefault()) {
        return invokeDefaultMethod(proxy, method, args);
      }
    } catch (Throwable t) {
      throw ExceptionUtil.unwrapThrowable(t);
    }
    // <4>MapperMethod 处理具体的mapper各种接口调用的具体效果,MapperMethod和mapper接口方法是一对一，每个MapperMethod都有自己的sqlcommand，sqlstatement
    final MapperMethod mapperMethod = cachedMapperMethod(method);
    return mapperMethod.execute(sqlSession, args);
  }
}

// MapperMethod 
public class MapperMethod {

  private final SqlCommand command;
  private final MethodSignature method;

  public MapperMethod(Class<?> mapperInterface, Method method, Configuration config) {
    this.command = new SqlCommand(config, mapperInterface, method);
    this.method = new MethodSignature(config, mapperInterface, method);
  }

  public Object execute(SqlSession sqlSession, Object[] args) {
    Object result;
    switch (command.getType()) {
      case INSERT: {
        Object param = method.convertArgsToSqlCommandParam(args);
        //<1> command是SqlCommand，command.getName()就是mybatis中sqlstatement的id值，
        // sqlSession.insert内部会去根据MapperConfigration拿到具体的Statement
        // MapperConfigration会在初始的SqlSessionFactory时的适当环节中初始，可以看后续`SqlSessionFactory的初始`的章节
        result = rowCountResult(sqlSession.insert(command.getName(), param));
        break;
      }
      case UPDATE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.update(command.getName(), param));
        break;
      }
      case DELETE: {
        Object param = method.convertArgsToSqlCommandParam(args);
        result = rowCountResult(sqlSession.delete(command.getName(), param));
        break;
      }
      case SELECT:
        if (method.returnsVoid() && method.hasResultHandler()) {
          executeWithResultHandler(sqlSession, args);
          result = null;
        } else if (method.returnsMany()) {
          result = executeForMany(sqlSession, args);
        } else if (method.returnsMap()) {
        //<2> command是SqlCommand，command.getName()就是mybatis中sqlstatement的id值，
        // sqlSession.insert内部会去根据MapperConfigration拿到具体的Statement            
          result = executeForMap(sqlSession, args);
        } else if (method.returnsCursor()) {
          result = executeForCursor(sqlSession, args);
        } else {
          Object param = method.convertArgsToSqlCommandParam(args);
          result = sqlSession.selectOne(command.getName(), param);
          if (method.returnsOptional()
              && (result == null || !method.getReturnType().equals(result.getClass()))) {
            result = Optional.ofNullable(result);
          }
        }
        break;
      case FLUSH:
        result = sqlSession.flushStatements();
        break;
      default:
        throw new BindingException("Unknown execution method for: " + command.getName());
    }
    if (result == null && method.getReturnType().isPrimitive() && !method.returnsVoid()) {
      throw new BindingException("Mapper method '" + command.getName()
          + " attempted to return null from a method with a primitive return type (" + method.getReturnType() + ").");
    }
    return result;
  }
}
```

### SqlSessionFactory的初始

```java
public class SqlSessionFactoryBean
    implements FactoryBean<SqlSessionFactory>, InitializingBean, ApplicationListener<ApplicationEvent> {

    // SqlSessionFactoryBean java
    public void afterPropertiesSet() throws Exception {
        notNull(dataSource, "Property 'dataSource' is required");
        notNull(sqlSessionFactoryBuilder, "Property 'sqlSessionFactoryBuilder' is required");
        state((configuration == null && configLocation == null) || !(configuration != null && configLocation != null),
            "Property 'configuration' and 'configLocation' can not specified with together");
        // <1>
        this.sqlSessionFactory = buildSqlSessionFactory();
    }
    // <2> 加载mybatis的各种配置，可以查看 xml配置 https://mybatis.org/mybatis-3/zh/configuration.html; sql映射配置 https://mybatis.org/mybatis-3/zh/sqlmap-xml.html
    protected SqlSessionFactory buildSqlSessionFactory() throws Exception {

        final Configuration targetConfiguration;

        XMLConfigBuilder xmlConfigBuilder = null;
        if (this.configuration != null) {
        targetConfiguration = this.configuration;
        if (targetConfiguration.getVariables() == null) {
            targetConfiguration.setVariables(this.configurationProperties);
        } else if (this.configurationProperties != null) {
            targetConfiguration.getVariables().putAll(this.configurationProperties);
        }
        } else if (this.configLocation != null) {
        xmlConfigBuilder = new XMLConfigBuilder(this.configLocation.getInputStream(), null, this.configurationProperties);
        targetConfiguration = xmlConfigBuilder.getConfiguration();
        } else {
        LOGGER.debug(
            () -> "Property 'configuration' or 'configLocation' not specified, using default MyBatis Configuration");

        }
        if (hasLength(this.typeAliasesPackage)) {
        }

        if (!isEmpty(this.typeAliases)) {
        }

        if (!isEmpty(this.plugins)) {
        }

        if (hasLength(this.typeHandlersPackage)) {
        }

        if (!isEmpty(this.typeHandlers)) {
        }

        if (!isEmpty(this.scriptingLanguageDrivers)) {
        }

        if (this.databaseIdProvider != null) {// fix #64 set databaseId before parse mapper xmls
        }

        Optional.ofNullable(this.cache).ifPresent(targetConfiguration::addCache);


        targetConfiguration.setEnvironment(new Environment(this.environment,
            this.transactionFactory == null ? new SpringManagedTransactionFactory() : this.transactionFactory,
            this.dataSource));

        if (this.mapperLocations != null) {
            // <3> 加载mapper的配置
        } else {
        LOGGER.debug(() -> "Property 'mapperLocations' was not specified.");
        }
        // <4> 创建SqlSessionFactory，其实就是Mybatis原生创建的方法
        return this.sqlSessionFactoryBuilder.build(targetConfiguration);
    }
}
```

- <1> 借助SqlSessionFactoryBean初始，`SqlSessionFactoryBean`实现InitializingBean接口，所以在bean创建周期中会调用`afterPropertiesSet`方法
- <2> 加载mybatis的各种配置，可以查看 xml配置 https://mybatis.org/mybatis-3/zh/configuration.html; sql映射配置 https://mybatis.org/mybatis-3/zh/sqlmap-xml.html
- <3> 加载mapper的配置
- <4> 创建SqlSessionFactory，其实就是Mybatis原生创建的方法

## 总结


## 示例源码

https://github.com/creasy-pita/MybatisTutorial.git  Spring-MybatisTest模块

## 问题

`<1>`中如何获得spring 容器中的name为`studentMapper`的bean
StudentMapper是一个接口，mybatsi如何产生整合dao逻辑，返回一个实现类


参考 ：https://blog.csdn.net/fighterandknight/article/details/51448161 
