# Hibernate

 Hibernate是一个开放源代码的对象关系映射框架，它对JDBC进行了非常轻量级的对象封装，它将POJO与数据库表建立映射关系，是一个全自动的ORM框架.

hibernate 框架就是对jdbc的封装，使用hibernate的好处就是不需要写复杂的jdbc代码，也不需要写sql实现了.

**什么是ORM？**

​	对象关系映射，将Java中的对象与关系型数据库的表建立映射关系，从而操作对象就可以操作表.



**映射类配置文件约束**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC 
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
   <!--  内容 -->
</hibernate-mapping>
```

**核心配置文件约束**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
	"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
	"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <!--  内容 -->
</hibernate-configuration>
```









## 入门案例

​	**步骤：**导入所需jar包，创建属性类、创建对应属性类的映射配置文件【必须与属性类同包下】，创建核心配置文件【src/】，一般名为hibernate.cfg.xml 因为当使用hibernate时需要提供核心配置文件的路径，若不提供则默认读名为hibernate.cfg.xml 那个名称的配置文件.

**属性类**

```java
private class User{
	private Integer id;
    private String name;
    private String psWord;
    //get、set、toString此处省略
}
```

**对应属性类的映射配置文件**

配置文件名称一般为：类名.hbm.xml  当前配置文件名称：User.hbm.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC 
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
    <!-- name="全类名" table="属性类所对应的数据库表名" -->
    <class name="com.znsd.User" table="user">
        <!-- id标签设置的字段为主键 -->
        <!-- name="属性类字段名" cloumn="属性类所对应的表字段名" -->
        <id name="id" cloumn="id">
        	<generator class="native" />
        </id>
        <!-- 普通字段 property -->
        <!-- name="属性类字段名" cloumn="属性类所对应的表字段名" -->
        <property name="name" column="name" />
		<property name="psWord" column="psWord" />
    </class>
</hibernate-mapping>
```

**核心配置文件** hibernate.cfg.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
	"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
	"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
	<session-factory>
		<!-- 链接数据库的基本参数 -->
		<property name="hibernate.connection.driver_class">
            com.mysql.jdbc.Driver
        </property>
		<property name="hibernate.connection.url">
            jdbc:mysql:///test
        </property>
		<property name="hibernate.connection.username">
            root
        </property>
		<property name="hibernate.connection.password">
            123
        </property>
		<!-- 配置Hibernate方言 主要目的使用任何操作时生成对应的数据库的sql -->
		<property name="hibernate.dialect">
            org.hibernate.dialect.MySQLDialect
        </property>
 
		<!-- 可选配置 -->
		<!-- 打印sql -->
		<property name="hibernate.show_sql">true</property>
		<!-- 格式化sql -->
		<property name="hibernate.format_sql">true</property>
 
		<!-- 读取映射配置文件 -->
		<mapping resource="com/znsd/user/User.hbm.xml" />
	</session-factory>
 
 
</hibernate-configuration>
```

**测试类**

```java
package com.znsd.user;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Main {
	public static void main(String[] args) {
		// 1.加载Hibernate的核心配置文件
		Configuration configuration = new Configuration().configure();

		// 2.创建一个SessionFactory对象：类似于JDBC中连接池
		SessionFactory sessionFactury = configuration.buildSessionFactory();

		// 3.通过SessionFactory获取到Session对象：类似于JDBC中Connection
		Session session = sessionFactury.openSession();

		// 4.手动开启实物（兼容Hibernate3）： 可以不写，h5默认有
		Transaction transaction = session.beginTransaction();

		// 5.编写代码
		User customer = new User();
		customer.setId(1);
		customer.setUserName("张三");
		customer.setUserPsWord("123");

		session.save(customer);
		// 6.事务提交
		transaction.commit();

		// 7.资源释放
		session.close();
	}
}
```











### 映射配置文件属性详解

**自动建表配置**

```xml
<!-- 自动建表语句，其属性值有多个，update是其中一个，属性值不同功能不同 -->
<property name="hibernate.hbm2ddl.auto" >update</property>  
<!-- 参考：https://www.cnblogs.com/liutao1122/p/8507917.html -->
<!-- 该标签主要写在核心配置文件头节点下即可 -->
```





**映射类配置文件约束**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC 
    "-//Hibernate/Hibernate Mapping DTD 3.0//EN"
    "http://www.hibernate.org/dtd/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
   <!--  内容 -->
</hibernate-mapping>
```

<hibernate-mapping> 为映射类的头标签，从此处内开始写映射信息.

首先此标签有许多子标签，但我们现在主要关注怎么让属性类与数据库表对应，只需要关注class标签.

<class> 该标签就可以让属性类与数据库中的表对应，以下介绍标签中的常用属性.

```txt
name="属性类全类名" 
table="数据库表名" 
catalog="数据库名称" #若不指定则使用核心配置文件配置的数据库.
```

**案例演示**

```xml
<!-- 省略约束 -->
<hibernate-mapping>
    <class name="com.znsd.user" table="user" catalog=“testDB”>
    	<!-- 此处内容会慢慢标明 -->
    </class>
    <class name="com.znsd.class" table="class" catalog=“test2DB”>
    </class>
</hibernate-mapping>
```



<class> 内的入门常用标签 <id>  与 <properties>

<id> 主要作用用于设置属性类的主键字段，以下有id包含的属性

```xml
name="属性类字段名" 
column="表字段名" 
<!-- 以下属性是id关于创建表，hibernate在对表做操作时首先会判断是否存在表若不存在则会创建 需要配置 -->
length="字段长度" <!-- 创建表中表字段的长度，若不设置则为默认值 例如:varchar默认值为255 -->
type="字段类型" <!-- 不写的话hibernate自动转换为对应类型 --> 
```

**案例演示**

```xml
<!-- 省略约束 -->
<hibernate-mapping>
    <!-- java 类型 -->
    <class name="com.znsd.user" table="user" catalog=“testDB”>
        <id name="uId" column="uId" type="java.lang.Integer">
        	<generator class="native" />
        </id>
    </class>
    <!-- hibernate 类型 -->
    <class name="com.znsd.user" table="user" catalog=“testDB”>
    	<id name="uId" column="uId" type="integer" />
    </class>
</hibernate-mapping>
```

更多字段类型参考

```txt
https://www.cnblogs.com/notably/p/10522351.html
```

**sql类型演示**

```xml
<!-- 省略约束 -->
<hibernate-mapping>
    <!-- hibernate 类型 -->
    <class name="com.znsd.user" table="user" catalog=“testDB”>
        <id name="uId">
        	<column name="uId" sql-type="int"></column>
			<generator class="native" />
        </id>
    </class>
</hibernate-mapping>
```



<properties> 主要映射普通字段.

```txt
 name="属性类字段名" 
 column="表字段名" 
 length="字段长度" 
 type="字段类型" 
 not-null="true||false" #生成表时该字段是否不为null 默认false
 unique="true||false"	#生成表示该字段是否作为唯一 默认为false
```

**案例演示**

```xml
<!-- 省略约束 -->
<hibernate-mapping>
    <class name="com.znsd.user" table="user" catalog=“testDB”>
        <id name="uId" column="uId">
        	<generator class="native" />
        </id>
        <!-- 创建表时该字段属性会包含不为空，唯一键 -->
        <properties name="uName" column="uName" not-null="true" unique="true" />
    </class>
</hibernate-mapping>
```

以上就是class、id、properties 的介绍.











### 核心配置文件详解

**必须的配置**

​	数据库驱动、数据库地址、数据库账号、数据库密码、配置方言

```xml
<hibernate-configuration>
	<session-factory>
		<!-- 链接数据库的基本参数 -->
		<property name="hibernate.connection.driver_class">
            com.mysql.jdbc.Driver
        </property>
		<property name="hibernate.connection.url">
            jdbc:mysql:///test
        </property>
		<property name="hibernate.connection.username">
            root
        </property>
		<property name="hibernate.connection.password">
            123
        </property>
        <!-- 方言 -->
		<property name="hibernate.dialect">
            org.hibernate.dialect.MySQLDialect
        </property>
	</session-factory>
</hibernate-configuration>
```

**可选的配置**

​	sql格式化、sql打印、表创建

```xml
<hibernate-configuration>
	<session-factory>
		<!-- 链接数据库的基本参数 -->
		<property name="hibernate.connection.driver_class">
            com.mysql.jdbc.Driver
        </property>
		<property name="hibernate.connection.url">
            jdbc:mysql:///test
        </property>
		<property name="hibernate.connection.username">
            root
        </property>
		<property name="hibernate.connection.password">
            123
        </property>
		<!-- 配置Hibernate方言 主要目的使用任何操作时生成对应的数据库的sql -->
		<property name="hibernate.dialect">
            org.hibernate.dialect.MySQLDialect
        </property>
 
		<!-- --------------------------可选配置-------------------------- -->
		<!-- 打印sql -->
		<property name="hibernate.show_sql">true</property>
		<!-- 格式化sql -->
		<property name="hibernate.format_sql">true</property>
        <!-- hibernate建表 -->
        <property name="hibernate.hbm2ddl.auto" >update</property>
		<!-- --------------------------可选配置-------------------------- -->
	</session-factory>
</hibernate-configuration>
```

建表标签中其他属性参考

```txt
https://blog.csdn.net/cactusz/article/details/74557154
```

**映射文件引入**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
	"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
	"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
	<session-factory>
		<!-- 链接数据库的基本参数 -->
		<property name="hibernate.connection.driver_class">
            com.mysql.jdbc.Driver
        </property>
		<!-- 链接数据库的基本参数 -->
		<property name="hibernate.connection.driver_class">
            com.mysql.jdbc.Driver
        </property>
		<property name="hibernate.connection.url">
            jdbc:mysql:///test
        </property>
		<property name="hibernate.connection.username">
            root
        </property>
		<property name="hibernate.connection.password">
            123
        </property>
		<!-- 配置Hibernate方言 主要目的使用任何操作时生成对应的数据库的sql -->
		<property name="hibernate.dialect">
            org.hibernate.dialect.MySQLDialect
        </property>
 
		<!-- 可选配置 -->
		<!-- 打印sql -->
		<property name="hibernate.show_sql">true</property>
		<!-- 格式化sql -->
		<property name="hibernate.format_sql">true</property>
 
		<!-- 包路径换成 / -->
		<mapping resource="com/znsd/user/User.hbm.xml" />
	</session-factory>
 
 
</hibernate-configuration>
```

核心配置文件创建方式有俩种：xml || .properties 区别是xml可以引入映射 另一个不可以.

**创建方式** 名称 hibernate.properties

```properties
hibernate.connection.driver_class=com.mysql.jdbc.Driver
hibernate.connection.driver_clas=com.mysql.jdbc.Driver
...
```







### Configuration 对象

 Configuration 类的作用是对Hibernate 进行配置，以及对它进行启动。

​		在Hibernate 的启动过程中，Configuration 类的实例首先定位映射文档的位置，读取这些配置，然后创建一个SessionFactory对象。虽然Configuration 类在整个Hibernate 项目中只扮演着一个很小的角色，但它是启动hibernate 时所遇到的第一个对象。 

**功能简介：**可以读取核心配置文件，映射配置文件，创建连接池 ...

**使用条件：**导入hibernatejar包，配置核心配置文件

```java
public void main(String [] s){
    //该方式默认读 hibernate.cfg.xml 
    Configuration config = new Configuration().configure();
    //该方式默认读 hibernate.properties
    Configuration config = new Configuration();
    //前面说了使用.properties不能配置映射文件，但config对象提供了方法在运行时加载映射文件
    //以下方法就可以加载映射配置文件
    configuration.addResource("com/znsd/user/User.hbm.xml");
}
```







### 创建sessionFactory的方式

不同版本的hibernate创建的方式都不太相同，虽然有些可以兼容，但也会有一些缺陷.

hibernate 3.x

```java
public void test(){
    Configuration config = new Configuration().configure();
	SessionFactory sessionFactory = config.buildSessionFactory();
}
```

hibernate 4.x

```java
SessionFactory sessionFactory = null;
Configuration configuration =  new Configuration().configure();
ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().
    applySettings(configuration.getProperties()).buildServiceRegistry();
sessionFactory = configuration.buildSessionFactory(serviceRegistry);
```

hibernate 5.x

```java
final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
	sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
```

**详情链接**

```cmd
https://blog.csdn.net/river6666/article/details/83445154
```











### CRUD操作

> 增加

```java
public Serializable save(Object obj);	//传入对象:返回生成自增长的id 强转int
```

> 查询

```java
>>> 根据id查询
public Object get(Class clazz,Serializable id); //传入Class,id:返回Object类型的Class对象
public Object load(Class clazz, Serializable id); //传入Class,id:返回Object类型的Class对象
>>> 查询全部
public Query createQuery(String sql); //传入sql语句:返回查询结果
public SQLQuery createSQLQuery(String sql); //传入sql
```

**案例演示**

```java
public void test(){
    Configuration cfg = new Configuration().configure();
    SessionFactory factory = cfg.buildSessionFactory();
    Session session = factory.openSession();
    >>> 核心代码 from后必须是实体类的名称，并非数据表
    Query query = session.createQuery("from User");
    List<User> list = query.list();
}
```

**get 与 load 的区别参考【很重要】**

```txt
https://www.cnblogs.com/yichenscc/p/11244227.html
#映射类被final修饰后get与load将没有区别，因为final不能被继承，那么将不存在有代理类.
```

> 修改

```java
public void update(Object obj); //传入JavaBean:修改成功! 
//bean中必须包含id,老值,新值,否则bean中属性为null也会导致修改时数据库的老值消失
//推荐先查询，set新值后再将bean传入update方法内.
```

> 删除

```java
public void delete(Object obj); //传入JavaBean:删除成功! 必须包含id
//推荐先查询,再删除.
```













### 主键生成策略



##### 主键的分类

> 自然主键

```txt
将表中某一个唯一的字段设为主键. 比如：用户的身份证号.
```

> 代理主键

```txt
新建一个与表毫无关系的字段作为id. 比如：通常用的id字段.
```



##### Hibernate的主键生成策略

解开映射类id中native的疑惑.

```xml
<id name="uId" column="uId">
    <generator class="native" />
</id>
```

> increment

```tex
hibernate提供的自动增长,底层首先会通过sql语句获取主键的最大值在最大值的基础上加1,在单个项目中可以正常使用，因为底层使用了synchronized,不建议在集群环境使用,因为此时会产生线程安全问题.
```

> identity

```tex
使用的是数据库自带的自增长,使用时对应表中的该字段必须为主键且自动增长. 
--Oracle这类没有自增字段的则不支持.
```

> sequence

```tex
使用的是数据库自带的序列[类似于自增长].
不支持sequence的数据库则不行,比如MySQL没有序列.
```

> native

```java
由hibernate根据使用的数据库自行判断采用identity、hilo、sequence其中一种作为主键生成方式.
```

> assigned

```java
表示不需要hibernate帮助生成id,当映射对象中属性id是什么存入数据库就是什么.
```

> uuid

```java
字符串字段生成主键可以通过该方式生成.
```











### 三种状态

hibernate为了更好管理对象，将对象划分了三种状态：瞬时态、持久态、脱管态.

![](images/hibernate 三种状态.png)

**瞬时(临时)态：**数据库中不存在该对象，不被session管理 直接通过new创建的.

**持久态：**被session管理，数据库中存在.

**脱管(游离)态：**对象的主键在缓存中不存在，且数据库中存在.









### 一级缓存

​			hibernate优化反复查询数据库消耗问题，数据库数据存储在磁盘中，当读取数据库数据时需要到磁盘中查找，但磁盘读取速度比较慢，此时hibernate就会将磁盘中的数据读取到内存中，然后当读取数据时在内存中读取即可，若内存中没有该数据时才会到数据库中读取该数据并存储在hibernate缓存中，这样提高了再次读取数据的速度。

**详细参考**

```tex
https://www.cnblogs.com/toby-ruan/p/8510408.html
```







### Threadlocal

共享session对象，保证多个类执行的是同一个session对象

需要在核心配置文件中配置以下标签

```xml
<property name="current_session_context_class">thread</property>
```

获取threadlocal的session

```java
public void test(){
	Configuration configuration = new Configuration().configure();
	SessionFactory sessionFactury = configuration.buildSessionFactory();
    Session currentSession = sessionFactury.getCurrentSession();
    //该session不需要close 只需要commit 就行，底层会close.
}
```

