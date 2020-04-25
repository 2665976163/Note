## Maven快速入门 

### 本章目标 

- 了解什么是项目构建
- 了解什么Maven
- 搭建Maven开发环境
- Maven的基本用法
- 使用Maven搭建SSM项目。

### 什么是构建 

- `以java源文件，配置文件，jar包，国际化资源文件，HTML，CSS，图片等资源文件去生成一个可以运行的项目的过程，`这个称为构建。 
- 如果这些工作要手动完成，那么会浪费我们太多时间了，于是有人用软件的方法让这一系列工作完全自动化，使得项目构建更加简单和方便，从而提高工作效率。 

### 构建过程的各个阶段 

**构建过程的各个环节：**

- 清理：将以前编译的旧的class文件清理掉，为下一次编译做准备。
- 编译：将java文件编译为class字节码文件。
- 测试：自动运行junit测试。
- 报告：测试程序的运行结果。
- 打包：将工程文件进行打包，动态web工程打的是war包，Java工程打的是jar包。
- 安装：Maven的特定概念，指将Maven复制到“仓库”中的指定位置。
- 部署：将动态web工程的war包复制到服务器中。

### 常用的自动化构建工具 

- Make： 最早的构建工具，由Stuart Feldman于1997年在Bell实验室创建。目前Make有很多衍生实现，包括GUN Make、BSD Make和Windows平台的Microsoft nmake等。 

- Ant： Ant是另一个简洁的工具，它最早用来构建Tomcat，其作者James Duncan Davidson创作它的动机是因为受不了Make的语法格式。我们可以将Ant看成一个Java版本的Make，Ant是一个跨平台的Java构建工具。

- Maven：Maven这近年来最流行的Java项目构建工具，在github上几乎大部分的项目都使用maven来进行构建的。 


### Maven所需要解决的问题。 

我们已经可以使用java来开发一些简单的项目，但是还存在一些问题： 

- 一个项目就是一个工程。如果项目过大，那么就不太适合只使用package来划分模块。最好是一个模块对应一个工程。 
- 项目中的jar包需要手动添加到项目中。同样的jar包重复出现在多个项目中，浪费存储空间，也不太便于管理。 
- 所需jar包必须提前准备好，一般从官网下载。不同官网提供下载jar包的形式是五花八门的，有些jar包只提供了使用构建工具或svn进行下载。 
- jar包所依赖的其它jar包需要手动添加。

### 什么是Maven 

什么是Maven？ 

- Maven是服务于Java平台的一种自动构建项目工具。
- 基于项目对象模型(`P`roject `O`bject `M`odel)，可以通过一小段描述信息来管理项目的构建、报告和文档的软件项目管理工具。

Maven的[官方网站](http://maven.apache.org/)

![20180726145414](http://www.znsd.com/znsd/courses/uploads/492f6f84b2dee16093731bc4f68f86af/20180726145414.png)

### Maven的下载及安装 

- Maven的下载连接：<http://maven.apache.org/download.cgi> 
- Maven环境
  - jdk1.7+（由于maven是基于java开发的，所以需要先安装java运行环境）
  - Maven-3.3+
- Maven目录结构

| 名称   | 作用              |
| ---- | --------------- |
| bin  | 包含Maven的运行脚本    |
| boot | 包含一个类加载器的框架     |
| conf | 配置文件目录          |
| lib  | Maven所用到的所有jar包 |



### 配置Maven的环境变量 

**配置环境变量**

- 新建`MAVEN_HOME`变量或者`M2_HOME`

- 在path中添加%M2_HOME%/bin路径 

- 输入mvn –v验证环境变量是否成功 

  ```shell
  C:\Users\Administrator>mvn -v
  Apache Maven 3.2.1 (ea8b2b07643dbb1b84b6d16e1f08391b666bc1e9; 2014-02-15T01:37:52+08:00)
  ```

### Maven的核心概念

- 约定的目录结构
- POM（Project Object Model）
- 坐标
- 依赖
- 仓库
- 生命周期/插件/目标
- 继承
- 聚合

### Maven的目录结构 

![5954965-fb11a8b27a0bd2e5](http://www.znsd.com/znsd/courses/uploads/a5c7e01431edf756c001223ec7bbf255/5954965-fb11a8b27a0bd2e5.jpg)

- `src/main/java` 项目的源代码所在的目录
- `src/main/resources` 项目的资源文件所在的目录
- `src/main/filters` 项目的资源过滤文件所在的目录
- `src/main/webapp` 如果是web项目，则该目录是web应用源代码所在的目录，比如html文件和web.xml等都在该目录下。
- `src/test/java` 测试代码所在的目录
- `src/test/resources` 测试相关的资源文件所在的目录
- `src/test/filters` 测试相关的资源过滤文件所在的目录
- `pom.xml` maven核心配置文件

### 创建maven项目

- 创建普通java项目

  ```shell
  mvn archetype:generate -DgroupId=com.znsd.maven -DartifactId=maven-demo -DarchetypeArtifactId=maven-archetype-quickstart
  # groupId:包名
  # artifactId:项目名称
  # archetypeArtifactId:模版id
  ```

- 创建javaWeb项目

  ```shell
  mvn archetype:generate -DgroupId=com.znsd.maven -DartifactId=maven-web -DarchetypeArtifactId=maven-archetype-webapp
  ```

### Maven配置文件pom.xml 

- 添加Maven核心配置文件pom.xml

  ```xml
  <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  	<!--声明项目描述符遵循哪一个POM模型版本。模型本身的版本很少改变，虽然如此，但它仍然是必不可少的，这是为了当Maven引入了新的特性或者其他模型变更的时候，确保稳定性。-->
  	<modelVersion>4.0.0</modelVersion>
  	<!--项目的全球唯一标识符，通常使用全限定的包名区分该项目和其他项目。并且构建时生成的路径也是由此生成， 如com.mycompany.app生成的相对路径为：/com/mycompany/app-->
  	<groupId>com.znsd.user</groupId>
  	<!--构件的标识符，它和group ID一起唯一标识一个构件。换句话说，你不能有两个不同的项目拥有同样的artifact ID和groupID；在某个特定的group ID下，artifact ID也必须是唯一的。构件是项目产生的或使用的一个东西，Maven为项目产生的构件包括：JARs，源码，二进制发布和WARs等。-->  
  	<artifactId>user-manager</artifactId>
  	<!--项目产生的构件类型，例如jar、war、ear、pom。插件可以创建他们自己的构件类型，所以前面列的不是全部构件类型-->
  	<packaging>war</packaging>
  	<!--项目当前版本，格式为:主版本.次版本.增量版本-限定版本号-->  
  	<version>0.0.1-SNAPSHOT</version>
  	 <!--项目的名称, Maven产生的文档用-->
  	<name>user-manager Maven Webapp</name>
  	<!--项目主页的URL, Maven产生的文档用-->
  	<url>http://maven.apache.org</url>
  	<!--该元素描述了项目相关的所有依赖。 这些依赖组成了项目构建过程中的一个个环节。它们自动从项目定义的仓库中下载。要获取更多信息，请看项目依赖机制。-->
  	<dependencies>
  		<dependency>
  		    <!--依赖的group ID-->
  			<groupId>junit</groupId>
  			<!--依赖的artifact ID-->
  			<artifactId>junit</artifactId>
  			 <!--依赖的版本号。-->
  			<version>4.12</version>
  			<!--依赖范围。在项目发布过程中，帮助决定哪些构件被包括进来。欲知详情请参考依赖机制。 
                - compile ：默认范围，用于编译   
                - provided：类似于编译，但支持你期待jdk或者容器提供，类似于classpath   
                - runtime: 在执行时需要使用   
                - test:    用于test任务时使用   
                - system: 需要外在提供相应的元素。通过systemPath来取得   
                - systemPath: 仅用于范围为system。提供相应的路径   
                - optional:   当项目自身被依赖时，标注依赖是否传递。用于连续依赖时使用-->
  			<scope>test</scope>
  			<!--当计算传递依赖时， 从依赖构件列表里，列出被排除的依赖构件集。即告诉maven你只依赖指定的项目，不依赖项目的依赖。此元素主要用于解决版本冲突问题--> 
            <exclusions>
             <exclusion>  
                    <artifactId>spring-core</artifactId>  
                    <groupId>org.springframework</groupId>  
                </exclusion>  
            </exclusions>    
  		</dependency>
  	</dependencies>
  	<build>
  		<finalName>user-manager</finalName>
  	</build>
  </project>
  ```

### 添加核心代码 

- 在src/main/java目录下添加com/znsd/user包，并添加一个Hello.java文件。 

  ```java
  package com.znsd.user;

  public class Hello {

  	public static void main(String[] args) {
  		
  		System.out.println("hello maven");
  	}
  	
  	public void say() {
  		System.out.println("hello maven");
  	}
  }
  ```

### 添加测试代码

- 在src/test/java下添加com/znsd/user目录，并添加一个HelloTest.java文件。

  ```java
  package com.znsd.user;

  import org.junit.Test;

  public class HelloTest {

  	@Test
  	public void testSay() {
  		Hello hello = new Hello();
  		hello.say();
  	}
  }
  ```

### Maven的常见构建命令

mvn

- -v                       --查看Maven版本
- compile              --编译主程序
- test-complie       --编译测试程序
- test                     --执行测试
- package              --打包项目
- clean                  --删除target
- install                 --安装jar包到本地仓库中
- site                     --生成站点
- exec:java –Dexec.mainClass=“main方法所在的类”  运行main方法

`使用Maven命令时，必须进入pom.xml所在的目录。 `

### 关于Maven联网问题 

- Maven的核心程序中仅仅只包含了抽象的声明周期，而Maven的具体工作必须由特定的插件来完成，而插件本身并不包含在Maven核心文件中。 
- 当我们执行的Maven命令需要用到`某些插件或者jar包`时，会首先在本地仓库中查找，如果本地不存在，会联网进行下载。 

### Maven仓库 

**仓库：分为本地仓库，中央仓库和镜像仓库**

- 本地仓库：所有的下载和引用的包所存放的位置， 默认存放路径：`${user.home}/.m2/repository `。
- 中央仓库：如果本地仓库中找不到所引用的构件，会再maven全球服务器中进行下载。
- 镜像仓库：因为maven的服务器一般位于国外，所以访问速度比较慢，一般国内也会建立一些maven仓库，访问速度更快，更稳定。

**修改本地仓库位置：**

- 修改settings.xml文件中的\<localRepository>本地路径\</localRepository> 节点

**修改中央仓库的地址**

- 在config/setting.xml文件中找到mirrors标签。

  ```xml
  <!--开源中国镜像仓库-->
  <mirror>  
  	<id>CN</id>  
      <name>OSChina Central</name>
      <url>http://maven.oschina.net/content/groups/public</url>  
      <mirrorOf>central</mirrorOf><!--表示匹配原仓库的请求都转到镜像仓库-->
  </mirror> 
  <!--阿里云镜像仓库-->
  <mirror>	  
      <id>alimaven</id>	  
      <name>aliyun maven</name>	
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
      <mirrorOf>central</mirrorOf>        	
  </mirror>
  ```

- 公司自己仓库配置

  ```xml
  <?xml version="1.0" encoding="UTF-8"?>
  <settings xmlns="http://maven.apache.org/settings/1.0.0"
  	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  	xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

  	<localRepository>D:\Maven\m2\repository</localRepository>

  	<pluginGroups>
  		<pluginGroup>org.mortbay.jetty</pluginGroup>
  		<pluginGroup>org.codehaus.cargo</pluginGroup>
  	</pluginGroups>

  	<proxies>
  	</proxies>

  	<servers>
  		<!-- 为部署构件至Nexus配置认证信息 -->
  		<server>
  			<id>nexus-releases</id>
  			<username>admin</username>
  			<password>admin123</password>
  		</server>
  		<server>
  			<id>nexus-snapshots</id>
  			<username>admin</username>
  			<password>admin123</password>
  		</server>
  	</servers>

  	<mirrors>
  		<!-- 配置镜像让Maven只使用私服 -->
  		<mirror>
  			<id>nexus</id>
  			<mirrorOf>*</mirrorOf>
  			<name>my maven repository</name>
  			<url>http://nexus.znsd.com/content/groups/public/</url>
  		</mirror>
  	</mirrors>

  	<profiles>
  		<!-- 配置Nexus仓库 -->
  		<profile>
  			<id>nexus</id>

  			<repositories>
  				<repository>
  					<id>central</id>
  					<!-- <url>http://central</url> -->
  					<url>http://nexus.znsd.com/content/groups/public/</url>
  					<releases>
  						<enabled>true</enabled>
  					</releases>
  					<snapshots>
  						<enabled>true</enabled>
  					</snapshots>
  				</repository>
  			</repositories>

  			<pluginRepositories>
  				<pluginRepository>
  					<id>central</id>
  					<!-- <url>http://central</url> -->
  					<url>http://nexus.znsd.com/content/groups/public/</url>
  					<releases>
  						<enabled>true</enabled>
  					</releases>
  					<snapshots>
  						<enabled>true</enabled>
  					</snapshots>
  				</pluginRepository>
  			</pluginRepositories>
  		</profile>
      </profiles>
  	<activeProfiles>
  		<activeProfile>nexus</activeProfile>
  	</activeProfiles>
  </settings>
  ```

### POM.xml配置文件

- POM（Project Object Model）项目对象模型。 
- pom.xml对于Maven工程是核心配置文件，与构建过程相关的一切配置都在这个文件中进行配置。
- 后面对Maven的学习，其实就是在学习对于pom.xml文件的配置。 

### Maven中的坐标 

- 在数学中，坐标的作用是可以在平面中定位任意一个点。 

- 而maven中每一个元素都有一个坐标，任何一个插件，依赖都可以称为构件。`所有构件通过坐标进行唯一标识。`

- dependency：用来引用构件。 

- 一个坐标由三个元素构成 

- groupid：公司或组织域名倒序+项目名。

- artifactId：项目模块


```xml
<artifactId>user-manage</artifactId>
```

- version：版本号 


```xml
<version>1.0.0</version>
```

- 参考junit的坐标配

  ```xml
  <dependencies>
      <!--应用junit 4.12版本-->
      <dependency>
  		<groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
      </dependency>
  </dependencies>
  ```

### 坐标和仓库中路径的对应关系 

- Maven中的坐标和仓库中的jar包是有对应关系的。

- 例如 

  ```xml
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-core</artifactId>
  <version>4.2.4.Final</version>
  ```

- 那么对应的jar包在Maven仓库中的路径是

  ```shell
  org/hibernate/hibernate-core/4.2.4.Final/hibernate-core-4.2.4.Final.jar
  ```

- `Maven会按照坐标的规则来组织jar包，这种是Maven组织仓库的约定。`

### 使用命令行构建maven项目

- 创建指定文件夹，在指定文件夹下运行maven命令 

- 执行命令：根据模板生成maven项目文件夹。 

  ```shell
  // mvn：maven的命令
  // archetype：调用maven的插件
  // generate：插件中的模版名称，用来生成项目的模板名称
  mvn archetype:generate
  ```

- 如果该指令创建失败，请删除/org/apache/maven/plugins/下的maven-archetype-plugin文件夹，maven会重新下载模版文件。

  ![20180726154527](http://www.znsd.com/znsd/courses/uploads/f27b4f8bb5937e86c0227fc55d362c84/20180726154527.png)

### 第二个项目 

- 创建一个user-dao项目。 

- 在user-dao中引用hello项目的jar包。 

  ```xml
  <dependencies>
      <dependency>
          <groupId>com.znsd.user</groupId>
          <artifactId>user-bean</artifactId>
          <version>0.0.1-SNAPSHOT</version>
          <scope>compile</scope>
      </dependency>
  </dependencies>
  ```

### 第二个项目的测试代码 

- 创建HelloFriend.java文件，调用hello项目的Hello类 

  ```java
  package com.znsd.user.dao;

  import com.znsd.user.Hello;

  public class HelloFrient {

  	public static void main(String[] args) {
  		Hello hello = new Hello();
  		hello.say();
  	}
  }
  ```

- 如果编译user-dao项目时，编译无法通过，原因是由于HelloFriend引用了Hello项目，但是在本地仓库和远程仓库中都无法找到该项目。 

### 使用Eclipse开发Maven项目 

- 目前比较新的版本的Eclipse都已经内置的Maven插件，不需要另外单独安装，如果没有请下载m2eclipse插件。 

- Maven插件安装好后，还需要进行一些简单的配置，需要设置Maven的核心程序位置。 

  ![20180726155400](http://www.znsd.com/znsd/courses/uploads/1cd1d040c8f32e6cd92810f35adccdf5/20180726155400.png)

### 新建Maven项目 

**在Eclipse中创建Maven非常简单**

- 创建Maven版的Java项目。
- 创建Maven版的Web项目。
- 如何执行Maven项目。

#### 创建Java项目 

选择新建Maven Project项目

![20180726155559](http://www.znsd.com/znsd/courses/uploads/5521cc0ce22c919d9bfc010dd40e3037/20180726155559.png)

![20180726155632](http://www.znsd.com/znsd/courses/uploads/e3b6ee998357c1727432bb74da9320a9/20180726155632.png)

![20180726155726](http://www.znsd.com/znsd/courses/uploads/35d8d97c510b217d9a4f0e7588e20a31/20180726155726.png)

Eclipse默认创建的项目使用的JDK版本为jdk1.5版本

![20180726155909](http://www.znsd.com/znsd/courses/uploads/33de912c3554bfc1fcbe7025614f7005/20180726155909.png)

#### 修改Maven默认的jdk版本 

- 设置通过Maven创建的工程的jdk版本 

- 打开settings.xml文件，找到profiles标签，加入如下代码。

  ```xml
  <profile>
      <id>jdk-1.8</id>
      <activation>
          <activeByDefault>true</activeByDefault>
          <jdk>1.8</jdk>
      </activation>
      <properties>
          <maven.compiler.source>1.8</maven.compiler.source>
          <maven.compiler.target>1.8</maven.compiler.target>
          <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
      </properties>
  </profile>
  ```

#### 练习：使用Maven创建Java项目 

- 使用Maven创建一个java项目。 

### 使用Maven创建Web项目 

- 一般Java项目时用来生成jar包，而web项目才是真正的功能实现。 

- 添加Web项目和Java项目区别不大，主要是Web项目将packaging改为war即可。

- Web项目的目录结构和普通项目一致，只是main下面多了一个webapp文件夹，还需要添加对应的web项目文件夹。 

  ![image](http://www.znsd.com/znsd/courses/uploads/5887e6215ea8642b8340e1065d7b7852/image.png)![image](http://www.znsd.com/znsd/courses/uploads/26bfe0c2b9259a16c85fa616e9481ba4/image.png)

#### 添加Web目录的配置文件。 

- 右键点击项目属性，找到Project Factes，更改右侧的复选框，Dynamic Web Module，然后会在下面出现Further configuration available…设置Content directory属性为webapp目录。

  ![image](http://www.znsd.com/znsd/courses/uploads/b0dc01f859e94b2c5552007dfa16d2c9/image.png)![image](http://www.znsd.com/znsd/courses/uploads/1066e7e8a0c0deca26a2f5b9f6e67ca8/image.png)

#### 添加jsp页面，发现无法进行解析 

- 当在页面中添加index.jsp页面时，页面会报错，原因是无法解析HttpServlet，错误原因是项目中没有引入Web服务器对应的jar包。

- 在pom.xml中添加servlet-api和jsp-api的引用。

  ```xml
  <dependencies>
      <dependency>
      	<groupId>javax.servlet</groupId>
      	<artifactId>servlet-api</artifactId>
      	<version>2.5</version>
          <!-- 这里必须使用provided，否则会导致Maven项目中的jar包和tomcat服务器中的jar包冲突。 -->
      	<scope>provided</scope>
      </dependency>
  	<dependency>
      	<groupId>javax.servlet.jsp</groupId>
      	<artifactId>javax.servlet.jsp-api</artifactId>
      	<version>2.3.1</version>
          <!-- 这里必须使用provided，否则会导致Maven项目中的jar包和tomcat服务器中的jar包冲突。 -->
      	<scope>provided</scope>
      </dependency>
  </dependencies>
  ```

### 导入Maven工程 

- 导入Maven工程到Eclipse，使用ImportàMavenàExisting Maven Project，选择对应的Maven工程即可导入。

- 如果是手动创建的Maven工程，第一次只能使用Maven的方式导入，以后可以使用Java默认的工程方式导入。 

  ![20180726161134](http://www.znsd.com/znsd/courses/uploads/48d8dcf6944a7a93ad0b47e9cad7dc0c/20180726161134.png)

  ![20180726161218](http://www.znsd.com/znsd/courses/uploads/f1b271773300d990c810143a65a1e2fb/20180726161218.png)

### 依赖范围

**scope属性的值**

- compile：默认范围，主程序、测试、运行、打包都有效。
- provided：在编译、测试时有效。jsp-servlet-api
- test：只在测试有效，junit

| 名称       | 主程序  | 测试程序 | 打包   | 部署   |
| -------- | ---- | ---- | ---- | ---- |
| compile  | 有效   | 有效   | 有效   | 有效   |
| provided | 有效   | 有效   | 无效   | 无效   |
| test     | 无效   | 有效   | 无效   | 无效   |

![874710-20170704205847972-1718666635](http://www.znsd.com/znsd/courses/uploads/86341705033b9f20b37bba1bc0532f58/874710-20170704205847972-1718666635.png)

### 依赖的传递性

- 当项目依赖生命周期compile的jar包时，另一个项目如果依赖此项目，则新的项目也会依赖原来项目所依赖的jar包，不需要重新导入，有点类似继承的传递性。

- 通过传递依赖，可以不必在每个工程中重复依赖，只需要在“最底层”的工程中依赖一次即可。 

- `注意：非compile范围的依赖不能传递。`

  ![20180726220531](http://www.znsd.com/znsd/courses/uploads/89bfd4ebc9b38b269d71e67669bc4c3d/20180726220531.png)

### 依赖的排除 

- 依赖排除和依赖传递相反，是指当前面的某个jar包，需要依赖另一个jar包也被加载进来了。例如spring-core依赖commons-logging，但是有些情况下，想排除掉commons-looging的依赖

- 排除依赖主要是用来`解决包冲突问题`

  ```xml
  <dependency>
  	<artifactId>Hello</artifactId>
  	<groupId>com.lxit.maven</groupId>
  	<version>0.0.1-SNAPSHOT</version>
      <!-- 排除传递的依赖 -->
  	<exclusions>
          <exclusion>
              <groupId>commons-logging</groupId>
              <artifactId>commons-logging</artifactId>
          </exclusion>
      </exclusions>
  </dependency>
  ```

### 依赖的原则 

- 依赖的原则：主要是为了排除jar包冲突的情况。 

  - 就近原则：根据路径最短选择依赖的jar包。 
  - 先声明者优先：当路径一致的情况下 ，会根据声明顺序选择依赖的jar包，前面声明的优先依赖。 

- 例如：当MakeFriends依赖的两个工程路径是一样的，那么会选择在前面声明的jar则会优先选择。

  ![image](http://www.znsd.com/znsd/courses/uploads/cf23e6b4d88ede125134da78b205c076/image.png)

### 生命周期 

- 生命周期：各个环节的执行顺序，Maven的构建过程包括：清理—>编译—>测试—>报告—>打包—>安装—>部署，一个环节完成后，才能执行下一个环节。 
- Maven的核心程序中定义了抽象的生命周期，生命周期中的各个阶段具体任务由插件来完成的。 
- Maven有三套独立的生命周期： 
  1. Clear Lifecycle：在进行真正的构建之前进行一些清理工作。 
  2. Default Lifecycle：构建的核心部分，编译，测试，打包，安装，部属等等。 
  3. Site Lifecycle：生成项目报告，站点，发布站点。

#### Clean生命周期 

clean生命周期的目的是清理项目，它包含三个阶段： 

1. pre-clean：执行一些清理前需要完成的工作。
2. clean：清理上一次构建生成的文件。
3. post-clean：执行一些清理后需要完成的工作。

#### Default生命周期

**Default生命周期是Maven生命周期中最重要的一个，绝大部分工作都发生在这个生命周期中，这里只解释一些常用的阶段：**

- validate：验证工程是否正确，所有需要的资源是否可用。
- compile：编译项目的源代码。  
- test：使用合适的单元测试框架来测试已编译的源代码。这些测试不需要已打包和布署。
- package：把已编译的代码打包成可发布的格式，比如jar。
- integration-test：如有需要，将包处理和发布到一个能够进行集成测试的环境。
- verify：运行所有检查，验证包是否有效且达到质量标准。
- install：把包安装到maven本地仓库，可以被其他工程作为依赖来使用。
- Deploy：在集成或者发布环境下执行，将最终版本的包拷贝到远程的repository，使得其他的开发者或者工程可以共享。

**Maven Default生命周期的所有阶段：**

- validate
- initialize
- generate-sources
- process-sources
- generate-resources
- `process-resources`：复制和处理资源文件到target目录，准备打包；
- `compile`：编译项目的源代码；
- process-classes
- generate-test-sources
- process-test-sources
- generate-test-resources
- process-test-resources
- `test-compile`：编译测试源代码；
- process-test-classes
- `test`：运行测试代码；
- prepare-package
- `package`：打包成jar或者war或者其他格式的分发包；
- pre-integration-test
- integration-test
- post-integration-test
- verify
- `install`：将打好的包安装到本地仓库，供其他项目使用；
- `deploy`：将打好的包安装到远程仓库，供其他项目使用；

#### Site生命周期 

**site生命周期的目的是建立和发布项目站点，Maven能够基于POM所包含的信息，自动生成一个友好的站点，方便团队交流和发布项目信息。该生命周期包含如下阶段：**

- pre-site：执行一些在生成项目站点之前需要完成的工作。
- site：生成项目站点文档。
- post-site：执行一些在生成项目站点之后需要完成的工作。
- site-deploy：将生成的项目站点发布到服务器上。

![20180129140713358](http://www.znsd.com/znsd/courses/uploads/7210a021c866efe239dcb6326689951d/20180129140713358.jpg)

**Maven核心程序为了更好的实现自动化构建，不论现在要执行生命周期的哪一个阶段，都是从生命周期最开始执行。**

### 插件和目标 

- 生命周期的各个阶段仅仅定义了要执行的任务是什么。
- 各个阶段和插件的目标是对应的。
- 相似的目标由特定的插件来完成。
- 插件目标可以看做是"调用插件功能的命令"。

| 生命周期阶段       | 插件目标        | 插件                    |
| ------------ | ----------- | --------------------- |
| compile      | compile     | maven-compiler-plugin |
| test-compile | testCompile | maven-compiler-plugin |

### 通过插件将web项目部署到tomcat

- 插件实现自动化部署到tomcat服务器

配置tomcat用户和权限

```xml
<tomcat-users>
<role rolename="admin-gui"/>
<role rolename="admin-script"/>
<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<role rolename="manager-jmx"/>
<role rolename="manager-status"/>
<user username="admin" password="password" roles="manager-gui,manager-script,manager-jmx,manager-status,admin-script,admin-gui"/>
</tomcat-users>
```

配置pom.xml

```xml
<plugins>
  <plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
      <!-- 注意此处的url,修改成tomcat监听的端口即可,localhost也可以是服务器的公网ip -->
      <url>http://localhost:8080/manager/text</url>
      <server>tomcat7</server> <!-- 此处的名字必须和setting.xml中配置的ID一致-->
      <path>/项目名</path>
      <server>tomcat7</server>
      <username>admin</username>
      <password>password</password>                
    </configuration>
  </plugin>
</plugins>
```

配置settings.xml

```xml
<server>
      <id>tomcat7</id>
      <username>admin</username>
      <password>password</password>
</server>
```

提前启动tomcat（去tomcat bin目录下启动tomcat->startup.bat）

Run As->Maven build…->在Goals里面输入tomcat7:deploy

### 统一管理依赖的版本 

使用Maven对Spring进行引用时，由于多个jar包的版本都是4.0.0，如果要升级spring版本，那么可能需要修改每一个配置信息，所以可以将版本号提取出来。

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>4.3.6.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-aop</artifactId>
    <version>4.3.6.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-beans</artifactId>
    <version>4.3.6.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>4.3.6.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-expression</artifactId>
    <version>4.3.6.RELEASE</version>
</dependency>
```

![20180726223428](http://www.znsd.com/znsd/courses/uploads/fe8336126ee070695e566cf786d6b9b2/20180726223428.png)

### 统一版本的步骤

- 在<properties>标签内使用自定义标签来统一声明版本号。 

  ```xml
  <properties>
      <spring.version>4.3.6.RELEASE</spring.version>
  </properties>
  ```

- 在需要使用版本的位置使用${自定义标签名}来引用版本号。 

  ```xml
  <dependency>
  	<groupId>org.springframework</groupId>
  	<artifactId>spring-core</artifactId>
  	<version>${spring.version}</version>
  </dependency>
  ```

- peroperteis标签当中声明的标签，除了可以用于版本号控制意外，可以声明任何标签，在需要的时候使用，类似于全局变量。

### 继承 

- 如果需要管理多个版本中的junit的jar包，由于junit是test生命周期的，所以不能被其它项目所依赖，那么就需要在多个项目中配置，这样做会比较零散，很容易造成版本不一致。
- 解决思路： 将junit依赖版本统一提取到"父"工程中，在子工程中声明依赖时不指定版本，以父工程中统一声明为准。同时也便于修改。 
- 操作步骤： 
  - 创建一个Maven工程做为父工程，注意：打包方式为pom。
  - 在父工程中统一管理junit的依赖。
  - 在子工程中声明对父工程的引用。将子工程的坐标中与父工程坐标中重复的删除。
  - 在子工程中删除junit依赖的版本号部分。

#### 创建一个父工程

- 创建一个父工程，package设置为pom 

  ![image](http://www.znsd.com/znsd/courses/uploads/49a5af834c2afd26698b8c3688d9450e/image.png)

#### 父工程的POM.xml 

- 在父工程中添加需要的jar包的配置。

  ```xml
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.znsd.user</groupId>
  <artifactId>user-parent</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <!-- 父项目的packaging必须为pom -->
  <packaging>pom</packaging>

  <!-- 在父类中添加junit的引用 -->
  <dependencyManagement>
      <dependencies>
          <dependency>
              <groupId>junit</groupId>
              <artifactId>junit</artifactId>
              <version>4.12</version>
              <scope>test</scope>
          </dependency>
      </dependencies>
  </dependencyManagement>
  ```

#### 在子工程中声明对父工程的引用

- 在子工程中引用父工程，并删除子工程中重复定义的标签。 

  ```xml
  <parent>
  	<groupId>com.znsd.user</groupId>
  	<artifactId>user-parent</artifactId>
  	<version>0.0.1-SNAPSHOT</version>
  	<!-- 添加父工程的引用路径 -->
  	<relativePath>../user-parent/pom.xml</relativePath>
  </parent>
  ```

- 删除子工程中junit对于版本的引用。

  ```xml
  <!-- 子工程中会继承父工程中添加的jar包，子工程中一般可以去掉对应的<version>标签，也可以重写父工程的版本号，这样可以简化子类的pom.xml文件。 -->
  <dependency>
  	<groupId>junit</groupId>
  	<artifactId>junit</artifactId>
  	<scope>test</scope>
  </dependency>
  ```

### 聚合 

- 一键安装各个模块工程。 

- 配置方式： 在父工程中添加各个子工程项目 

  ```xml
  <modules>
  	<module>../user-bean</module>
  	<module>../user-dao</module>
  </modules>
  ```

- 然后直接安装父工程就可以了，子工程也会一键安装。 

#### 部属pom.xml 

  - mvn clean:install

    clean是清理输出文件，install编译打包，在每次打包之前必须执行clean，才能保证发布为最新文件

  - mvn tomcat7:redeploy

    第一次发布 tomcat7:deploy，再次发布 tomcat7:redeploy 

### 搜索Maven的jar包

- maven的包，一般配置信息都可以直接从Maven中央仓库上查找。

- maven中央仓库：[http](http://mvnrepository.com/tags/maven)[://](http://mvnrepository.com/tags/maven)[mvnrepository.com/tags/maven](http://mvnrepository.com/tags/maven) 

  ![20180726224342](http://www.znsd.com/znsd/courses/uploads/dfba033b7f282edb88771a65d24006d9/20180726224342.png)

### 使用Maven整合SSM

- 使用Maven搭建SSM框架。 

### 总结

- 什么是Maven？Maven可以做什么？
- 自动化构建的过程是什么？
- 如何配置Maven的环境？
- Maven的目录结构是什么？
- pom.xml的作用是什么？什么是坐标和仓库？
- Maven的生命周期包括哪些？

### 总结 

- 在Eclipse里面使用创建jar项目。
- 在Eclipse里面创建Web项目。
- 三种打包方式的作用？.jar，.war，.pom
- 什么是依赖的传递性，依赖传递的原则是什么？
- 统一版本管理。
- 继承的作用？如何使用继承。
- 聚合的作用是什么？
- 配置web项目的一键发布的步骤。