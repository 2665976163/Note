# SpringCloud

SpringCloud整理了许多组件.

## Eureka

Eureka的简单介绍

```tex
https://www.cnblogs.com/knowledgesea/p/11208000.html
```

### 介绍

又称服务中心，管理各种服务功能包括服务的注册、发现、熔断、负载、降级等。

任何一个服务都不能直接去掉用，都需要通过注册中心来调用。通过服务中心来获取服务你不需要关注你调用的项目IP地址，由几台服务器组成，每次直接去服务中心获取可以使用的服务去调用既可。

由于各种服务都注册到了服务中心，就有了很多高级功能条件。比如几台服务提供相同服务来做客户端负载均衡（Ribbon）；监控服务器调用成功率来做断路器（Hystrix），移除服务列表中的故障点；监控服务调用时间来对不同的服务器设置不同的权重、智能路有（Zuul）等等。

Spring Cloud 封装了 Netflix 公司开发的 Eureka 模块来实现服务注册和发现。Eureka 采用了 C-S 的设计架构。**Eureka Server 作为服务注册功能的服务器**，它是服务注册中心。而系统中的**其他微服务，使用 Eureka 的客户端连接到 Eureka Server，并维持心跳连接**。这样系统的维护人员就可以**通过 Eureka Server 来监控系统中各个微服务是否正常运行**。Spring Cloud 的一些其他模块（比如Zuul）就可以通过 Eureka Server 来发现系统中的其他微服务，并执行相关的逻辑。

**Eureka由两个组件组成**：Eureka服务器和Eureka客户端。Eureka服务器用作服务注册服务器。Eureka客户端是一个java客户端，用来简化与服务器的交互、作为轮询负载均衡器，并提供服务的故障切换支持。Netflix在其生产环境中使用的是另外的客户端，它提供基于流量、资源利用率以及出错状态的加权负载均衡。

![Eureka](images/Eureka/Eureka.png)

上图简要描述了Eureka的基本架构，由3个角色组成：

**1、Eureka Server**

- Eureka Server 作为一个独立的部署单元，以 REST API 的形式为服务实例提供了注册、管理和查询等操作。同时，Eureka Server 也为我们提供了可视化的监控页面，可以直观地看到各个 Eureka Server 当前的运行状态和所有已注册服务的情况。

**2、Service Provider**

- 服务提供方
- 将自身服务注册到Eureka，从而使服务消费方能够找到

**3、Service Consumer**

- 服务消费方
- 从Eureka获取注册服务列表，从而能够消费服务

### 快速上手

eureka常见配置解析 [images/Eureka/Eureka常用配置解析.pdf]()

##### **EurekaServer 服务端配置 7000**

若想把当前项目作为EurekaServer端则需要做以下几步

\> 添加eureka-server 的 maven 坐标

```xml
<!-- eureka server：服务端 -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
```

\> 在主启动类上添加注解 @EnableEurekaServer

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerMain {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerMain.class,args);
    }
}
```

\> 在.yml中配置Eureka信息.

```yml
# 设置端口
server:
  port: 7000
# Eureka 配置    
eureka:
  client:
    # false表示不向注册中心注册自己
    register-with-eureka: false
    # false表示自己端就是注册中心，我的职责就是维护服务实例，不需要去检索服务
    fetch-registry: false
    service-url:
      # 表示与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址
      # 若不配置则会有默认配置:localhost:8761，会认为是另一台EurekaServer所以需要配置自己的
      defaultZone: http://localhost:${server.port}/eureka/
```

##### **EurekaClient 客户端配置 8000**

\> 添加eureka-client 的 maven 坐标

```xml
<!-- eureka client：客户端 -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

\> 在主启动类上添加注解 @EnableEurekaClient

```java
@SpringBootApplication
@EnableEurekaClient
public class EurekaClientMain {
    public static void main(String[] args) {
        SpringApplication.run(EurekaClientMain.class,args);
    }
}
```

\> 在.yml中配置Eureka信息.

```yml
# 设置端口
server:
  port: 8000
# 设置服务名称，作为当前项目注册进Eureka中的名称
spring:
  application:
    name: eureka-service
# Eureka 配置    
eureka:
  client:
    # true表示向注册中心[EurekaServer]注册自己，默认为true
    register-with-eureka: true
    # true表示从EurekaServer抓取已有注册信息，默认为true，集群必须为true，才能配合ribbon实现负载均衡
    fetch-registry: true
    service-url:
      # 表示与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址
   	  # 因为当前为client端，这里填写需要注册到EurekaServer端的地址
      defaultZone: http://localhost:7000/eureka/
```

------



##### EurekaServer集群配置

若是单个EurekaServer进行服务治理，该EurekaServer一旦挂掉，那么整个服务都可能会瘫痪.

所以现在实现EurekaServer的集群，达到高可用的目的.

**案例演示**

当前模拟有俩个EurekaServer端，分别端口是7000 与 7001，将这俩个进行互相注册，达到集群的配置.

EurekaServer 7000的配置

```yml
# 设置端口
server:
  port: 7000
# Eureka 配置    
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      # 指向另一台EurekaServer地址
      defaultZone: http://localhost:7001/eureka/
```

EurekaServer 7001的配置

```yml
# 设置端口
server:
  port: 7001
# Eureka 配置    
eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
    service-url:
      # 指向另一台EurekaServer地址
      defaultZone: http://localhost:7000/eureka/
```

若EurekaServer不止俩台或更多，只需要在链接后面加,分割即可

```yml
defaultZone: http://localhost:7010/eureka/,http://localhost:7011/eureka/
```

此时若有EurekaClient端则需要将自己注册给俩个EurekaServer中.

```yml
defaultZone: http://localhost:7000/eureka/,http://localhost:7001/eureka/
```

**智力考察**

![Eureka集群](images/Eureka/Eureka集群.png)

> 答案

```tex
当EurekaServer关闭后消费者确实能访问已注册的微服务提供者，但是若有些微服务提供者还没有注册进EurekaServer时，EurekaServer就挂掉了，那么此时这些微服务提供者将无法注册进EurekaServer，那么就导致消费者无法进行消费，若此时EurekaServer有集群的话，单个EurekaServer被关闭后，这些微服务提供者可以注册进另外一个EurekaServer中，然后消费者可以从另外一个EurekaServer获取该服务，从而消费。
```





##### EurekaClient 集群配置

​	当消费者进行消费时EurekaServer已经搭建了集群，没有那么容易挂掉，但这些EurekaServer调用的都是同一个微服务，比如现在把EurekaServer当作一个中间人，EurekaClient为生产者，因为搭建了集群那么就有多个中间人，当多个消费者进行消费时，若某个中间人挂掉了会有新的中间人顶上，但这个中间人还是会将请求给同一个生产者，此时就是多个中间人对一个生产者，此时生产者压力很大，若高并发则生产者很可能挂掉，所以需要搭建生产者的集群.

生产者 8000

```yml
# 设置端口
server:
  port: 8000
# 设置服务名称，作为当前项目注册进Eureka中的名称
spring:
  application:
    name: eureka-service
# Eureka 配置    
eureka:
  client:
    # true表示向注册中心[EurekaServer]注册自己，默认为true
    register-with-eureka: true
    # true表示从EurekaServer抓取已有注册信息，默认为true，集群必须为true，才能配合ribbon实现负载均衡
    fetch-registry: true
    service-url:
      # 表示与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址
   	  # 因为当前为client端，这里填写需要注册到EurekaServer端的地址
      defaultZone: http://localhost:7000/eureka/,http://localhost:7001/eureka/
```

生产者 8001

```yml
# 设置端口
server:
  port: 8001
# 设置服务名称，作为当前项目注册进Eureka中的名称
spring:
  application:
    name: eureka-service
# Eureka 配置    
eureka:
  client:
    # true表示向注册中心[EurekaServer]注册自己，默认为true
    register-with-eureka: true
    # true表示从EurekaServer抓取已有注册信息，默认为true，集群必须为true，才能配合ribbon实现负载均衡
    fetch-registry: true
    service-url:
      # 表示与Eureka Server交互的地址查询服务和注册服务都需要依赖这个地址
   	  # 因为当前为client端，这里填写需要注册到EurekaServer端的地址
      defaultZone: http://localhost:7000/eureka/,http://localhost:7001/eureka/
```

此时对应的有中间人7000，7001，生产者8000，8001

俩个中间人都包含这俩个生产者，因为这俩个生产者都向这些中间人注册了.

但当消费者请求时，EurekaServer并不清楚请求哪个微服务，从而报错，

此时将暂时引入ribbon实现客户端的负载均衡.

只需要消费者 80 在RestTemplate上加上注解LoadBalanced就可以实现负载均衡，Eureka中包含ribbon，所以不需要再引入ribbon的maven坐标了.

```java
@Configuration
public class ApplicationContextConfig {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

> RestTemplate是什么？

主要功能就是可以发送请求给不同项目，从而从请求中获取想要的数据.

这样当消费者消费时，就会根据ribbon的客户端负载均衡选择某一个微服务.



##### Discovery 服务发现

当多个服务提供者注册进EurekaServer中，消费者想查询这些服务信息该怎么做？

通过该对象可以获取到服务的信息。

```java
import org.springframework.cloud.client.discovery.DiscoveryClient;
@Resource //spring容器已经有该实例不需要配置
private DiscoveryClient discoveryClient;
```

```java
public String getTest(){
  // 获取所有服务
  List<String> services = discoveryClient.getServices();
  for (String service : services) {
	log.info("服务名称="+service);
	// 获取服务下的一组实例
	List<ServiceInstance> instances = discoveryClient.getInstances(service);
	for (int i = 0; i < instances.size(); i++) {
		// 单个实例
		ServiceInstance serviceInstance = instances.get(i);
		log.info("  服务ip地址="+serviceInstance.getHost());
		log.info("  服务端口号="+serviceInstance.getPort());
	}
  }
  return "";
}
```



##### Eureka 自我保护模式

 默认情况下，如果Eureka Server在一定时间内（默认90秒）没有接收到某个微服务实例的心跳，Eureka Server将会移除该实例

官方对于自我保护机制的定义：

> 自我保护模式正是一种针对网络异常波动的安全保护措施，使用自我保护模式能使Eureka集群更加的健壮、稳定的运行。

自我保护机制的工作机制是：**如果在15分钟内超过85%的客户端节点都没有正常的心跳，那么Eureka就认为客户端与注册中心出现了网络故障，Eureka Server自动进入自我保护机制**，此时会出现以下几种情况：

1. Eureka Server不再从注册列表中移除因为长时间没收到心跳而应该过期的服务。
2. Eureka Server仍然能够接受新服务的注册和查询请求，但是不会被同步到其它节点[过期的服务]上，保证当前节点依然可用。
3. 当网络稳定时，当前Eureka Server新的注册信息会被同步到其它节点中。

因此Eureka Server可以很好的应对因网络故障导致部分节点失联的情况，而不会像zookeeper那样如果有一半不可用的情况会导致整个集群不可用而变成瘫痪。

**EurekaClient 客户端配置**

```yml
# 服务续约（心跳）频率，单位：秒，缺省30 
eureka.instance.lease-renewal-interval-in-seconds=10
# 服务失效时间，失效的服务将被剔除。单位：秒，默认：90
eureka.instance.lease-expiration-duration-in-seconds=20
```

EurekaClient 启动注册到服务端每10秒发送一次心跳，20秒内没有发送心跳就会被标记为无效节点

**EurekaServer 服务端配置**

```yml
#false为关闭自我保护，默认为true
eureka.server.enable-self-preservation=false
#清理无效节点,默认60*1000毫秒,即60秒
eureka.server.eviction-interval-timer-in-ms=5000
```

当前EurekaServer关闭了自我保护，从当前EurekaServer启动后每5秒清理一次无效节点



##### Eureka 身份认证

配置security依赖，在eureka服务端配置如下约束

```xml
<!-- 注册服务安全 -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

application.yml

```properties
#开启安全机制设置用户名密码
security:
  basic:
    enabled: true
  user:
    name: admin
    password: 123456
#服务注册地址例子如下
eureka:
  client:
    serviceUrl:
      defaultZone: http://admin:123456@localhost:7070/eureka/
```



##### Eureka 健康检查

人会生病，就像人一样服务有时候也会出现异常情况，我们也需要知道某个服务的健康状况。Eureka有健康检查机制。

我们可以通过http://localhost:7070/info查看服务信息；可以通过http://localhost:7070/health查看健康状况。

配置健康检查

在程序运行过程中，eureka并不能根据你的意向去判断服务是否健康。因此eureka提供出自定义健康检查机制，让程序猿根据自己的意识或者说是业务，来判断服务是否健康。为此eureka提供出了两个接口：HealthIndicator（健康指示器）用来感知服务的状态，HealthCheckHandler（健康检查处理器）用来处理健康结果。

在Eureka客户端加上如下依赖:

```xml
<!--健康检查依赖-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

在客户端编写自定义检查逻辑

```java
@Component
public class MyHealthIndicator implements HealthIndicator{
	public Health health() {
		System.out.println(StudentApplication.canVisitDb);
        if(StudentApplication.canVisitDb) {
            return new Health.Builder(Status.UP).build();
        } else {
            return new Health.Builder(Status.DOWN).build();
        }
    }
}
```

加快更新服务信息

```properties
#更新实例信息的变化到Eureka服务端的间隔时间，单位为秒,默认为30秒
#eureka.client.instanceInfoReplicationIntervalSeconds=10
```

健康信息显示到eureka

```java
@Component
public class MyHealthCheckHandler implements HealthCheckHandler {
    @Autowired
    private MyHealthIndicator halthIndicator;
    public InstanceStatus getStatus(InstanceStatus currentStatus) {
        Status status = halthIndicator.health().getStatus();
        if(status.equals(Status.UP)) {
            return InstanceStatus.UP;
        } else {
            return InstanceStatus.DOWN;
        }
    }
}
```

















## Consul 入门

**服务发现以及注册：**

​	当服务Producer 启动时，会将自己的Ip/host等信息通过发送请求告知 Consul，Consul 接收到 Producer 的注册信息后，每隔一段时间会向 Producer 发送一个健康检查的请求，检验Producer是否健康，但若不配置Consul不会自动移除某个不健康的微服务，只是标记某个微服务不健康。

**服务调用：**

​	当 Consumer 请求Product时，会先从 Consul 中拿到存储Product服务的 IP 和 Port 的临时表(temp table)，从temp table表中任选一个· Producer 的 IP 和 Port， 然后根据这个IP和Port，发送访问请求；temp table表只包含通过了健康检查的 Producer 信息，并且每隔一段时间更新

> 下载Consul window版

```tex
https://releases.hashicorp.com/consul/1.7.2/consul_1.7.2_windows_amd64.zip
```

启动consul[consul.exe所在目录cmd输入以下启动]

```shell
consul agent -dev
```

微服务项目只需要配置以下几步即可注册进consul

**Consul所需依赖**

```xml
<!-- Spring Cloud consul-server consul依赖jar -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
<!-- 健康检查,若不配置该依赖则服务会报错，因为consul会依赖该jar进行健康检查 -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

application.yml

```yml
server:
  port: 8011

spring:
  application:
    name: consul-provider-payment
  cloud:
    consul:
      #host: consul服务器ip，我们consul在本地启动所以为localhost
      host: localhost
      #port: consul端口
      port: 8500
      discovery:
        # 注册进consul中的微服务名称
        service-name: ${spring.application.name}
        # 该服务挂掉后，consul多久可以移除，若不配置则不会删除挂掉的服务，即使不存在某个服务
        health-check-critical-timeout: 30s
```

启动类 注意注解不再是@EnableEurekaClient而是@EnableDiscoveryClient

```java
@SpringBootApplication
@EnableDiscoveryClient
public class ConsulClientMain8011 {
    public static void main(String[] args) {
        SpringApplication.run(ConsulClientMain8011.class,args);
    }
}
```

在微服务之间调用时若使用的RestTemplate记得加上注解@LoadBalanced，否则微服务之间调用会报错。

```java
@Configuration
public class ApplicationConfig {
    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
```

Docker 搭建Consul集群

```http
https://www.cnblogs.com/lonelyxmas/p/10880717.html
```

亲测可试.

Eureka 与 Consul的微服务集群挂掉在还没来得及清除前有一些区别

​	Eureka：照样会尝试调用挂掉的服务，并抛出异常给用户

​	Consul：不会尝试调用挂掉的服务，会调用其他可用的服务















## Ribbon

​	一款客户端的负载均衡，与nginx的关系举例：人要去火车站买票，nginx帮人挑选人最少的火车站，而ribbon帮人挑选人最少的买票窗口。

使用Eureka与Consul都不需要导入Ribbon依赖，因为它们集成了Ribbon，引入它们就顺便引入的Ribbon。

ribbon的负载均衡种类：

![ribbon负载均衡策略](images/Ribbon/ribbon负载均衡策略.png)

**默认为RoundRobinRule（ 轮询）**

自定义负载均衡策略

首先创建一个Java类不能是启动类的同包或子包，主要是不能被@ComponentScan扫描到

这里我们采用的策略为随机

```java
@Configuration
public class MySelfRule {
    @Bean
    public IRule myRule() {
        // 随机
        return new RandomRule();
    }
}
```

然后在主启动类上加个注解【其实只要能被扫到的类上加都是一样的，只不过方面找】

@RibbonClient(name = "微服务名称",configuration= 配置类.class)

```java
@SpringBootApplication
@EnableEurekaClient
@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",configuration= MySelfRule.class)
public class OrderMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OrderMain80.class,args);
    }
}
```

该注解的主要含义就是当对指定的微服务名称进行消费时，会采取指定负载均衡策略，

没有被指定的微服务被消费时还是默认轮询策略。

**此时可能会想若一个项目中对多个不同服务进行消费难道要加好几个RibbonClient吗？**

这时有个注解：@RibbonClients

该注解可以存多个@RibbonClient

```java
@RibbonClients(
    {@RibbonClient(name = "CLOUD-PAYMENT-SERVICE",
                   configuration= MySelfRule.class),
     @RibbonClient(name = "CLOUD-PAYMENT-SERVICE",
                   configuration= MySelfRule.class)})
```

这样可以更细微的对某一个微服务采取指定负载均衡策略。





### **自定义负载均衡策略（轮询）**

**思想**

使用RestTemplate时若微服务为集群状态

- ​	提供微服务名称以及请求路径
- ​    定义注解@LoadBalance开启负载均衡

这样由RestTemplate根据不同的负载均衡策略进行计算访问集群下的某一个微服务

但是若我们直接提供一个准确的微服务地址而不是由注册中心提供的微服务名称那么RestTemplate就会直接访问指定的微服务，那么在这一层我们可以自己的算法挑选出某一个微服务并且将该微服务的地址交给RestTemplate从而达到负载均衡的目的.

![手写负载均衡](images/Ribbon/手写负载均衡.png)



> 接口

```java
package com.znsd.springcloud.myloadbalance;

import org.springframework.cloud.client.ServiceInstance;

public interface ILoadBalance {
    ServiceInstance instances(String url);
}
```

> 实现类

```java
package com.znsd.springcloud.myloadbalance;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URI;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLoadBalance implements ILoadBalance {
    @Resource
    private DiscoveryClient discoveryClient;

    private static AtomicInteger index = new AtomicInteger(0);

    /**
     * 获取微服务实例
     * @param url
     * @return
     */
    @Override
    public ServiceInstance instances(String url) {
        List<ServiceInstance> instances = discoveryClient.getInstances(url);
        int index = getAndIncrement() % instances.size();
        if(instances.size()<1){
            return null;
        }
        return instances.get(index);
    }

    /**
     * 获取本次使用的微服务下标
     * @return
     */
    private int getAndIncrement(){
        int current;
        int next;
        do{
            current = index.get();
            next = current >= Integer.MAX_VALUE ? 0 : current + 1;
        }while (!index.compareAndSet(current,next));
        System.out.println(index);
        return current;
    }

    /**
     * 本次使用的微服务地址
     * @param serverName
     * @return
     */
    public URI getURI(String serverName){
        ServiceInstance instances = instances(serverName);
        URI uri = instances.getUri();
        return uri;
    }
}
```

该类只需要调用getURI方法提供微服务名称，即可返回提供的微服务名称下的某个微服务地址

```java
public String getPort(){
	URI uri = myLoadBalance.getURI("CLOUD-PAYMENT-SERVICE");
	System.out.println(uri);
	return restTemplate.getForObject(uri+"payment/port",String.class);
}
```

接着由RestTemplate直接请求某个准确的微服务地址。











## OpenFeign

 	**Feign**是声明性Web服务客户端。它使编写Web服务客户端更加容易。要使用Feign，请创建一个接口并对其进

行注释。它具有可插入注释支持，包括Feign注释和JAX-RS注释。Feign还支持可插拔编码器和解码器。Spring 

Cloud添加了对Spring MVC注释的支持，并支持使用`HttpMessageConverters`Spring Web中默认使用的注释。

Spring Cloud集成了Eureka和Spring Cloud LoadBalancer，以在使用Feign时提供负载平衡的http客户端。 

------

​	简单点说就是开发微服务，免不了需要服务间调用。Spring Cloud框架提供了RestTemplate和FeignClient两个方式完成服务间调，而我们开始用的RestTemplate会产生很多重复的代码，即使特意整理但还是看上去不是很规范，而使用OpenFeign进行微服务之间调用整体看起来就比较规范了。

RestTemplate调用：

- 创建RestTemplate对象
- 对象与微服务的请求方式一致，如：get、post请求
- 提供对应服务的服务地址、参数、返回类型

OpenFeign调用：

- 主启动类带上`@EnableFeignClients`
- 要想调用某个服务就先为某个服务创建一个接口，且加上注解`@FeignClient("服务名称")`并指定服务名称
- 在此接口中定义方法与对应服务做好映射，需要服务调用时直接注入即可。

所需依赖

```xml
<!-- OpenFeign -->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

application.yml

```yml
server:
  port: 80

eureka:
  client:
    # true表示将自己注册进Eureka Server，默认为true
    register-with-eureka: false
    # 是否从Eureka Server抓取以有的注册信息，默认为true 集群必须设置为true才能配合Ribbon使用负载均衡
    fetch-registry: true
    service-url:
      defaultZone: http://www.eureka7001.com:7001/eureka,http://www.eureka7002.com:7002/eureka
```

主程序入口 加上注解 `@EnableFeignClients`

```java
@SpringBootApplication
@EnableFeignClients
public class OpenFeignMain80 {
    public static void main(String[] args) {
        SpringApplication.run(OpenFeignMain80.class,args);
    }
}
```

接口定义 `@FeignClient("服务名称")`

```java
@Component
@FeignClient("CLOUD-PAYMENT-SERVICE")
public interface PaymentService {
    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id);
}
```

服务调用

```java
@RestController
public class OpenFeignController {
    @Resource
    private PaymentService paymentService;

    @GetMapping("/payment/get/{id}")
    public CommonResult getPaymentById(@PathVariable("id")Long id){
        return paymentService.getPaymentById(id);
    }
}
```

------

**OpenFeign的超时控制**

​		OpenFeign进行服务调用时若1秒[默认]之内服务没有执行完毕，那么OpenFeign则认为这个服务有问题，立即抛出异常，那么在实际中某些服务正常执行时间确实超过1秒，所以我们需要设置OpenFeign的超时时间，从而保证服务调用成功

```yml
# 设置feign客户端超时时间（OpenFeign默认支持Ribbon）
ribbon:
  # 指的是建立连接后从服务器读取到可用资源所用时间 默认为1秒
  ReadTimeout: 5000
  # 指的是建立连接所用的时间，适用于网络状况正常的情况下，两端连接所用的时间
  ConnectTimeout: 5000
```



------

**OpenFeign的日志监控**

OpenFeign级别

```tex
NONE: 默认的，不显示任何日志
BASIC：仅记录请求方法、URL、响应状态码以及执行时间
HEADERS：除了BASIC中定义的信息以外，还有请求和响应的头信息
FULL： 除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据
```

定义配置类

```java
import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Logger.Level feignLoggerLevel(){
        // 除了HEADERS中定义的信息之外，还有请求和响应的正文及元数据
        return Logger.Level.FULL;
    }
}
```

添加yml

```yml
logging:
  level:
    # feign日志以什么级别监控哪个接口
    com.znsd.springcloud.service.PaymentFeignService: debug
```





## Hystrix

**前言**

​		在正常情况下某个微服务下的某个接口执行耗时为3秒，但该微服务其他接口都耗时为1秒，但该微服务3秒的接口被大量用户进行访问，tomcat本来底层就对线程资源有限制，此时tomcat的资源就可能全去处理3秒的接口，而导致原本执行只需要1秒的接口根本访问不了，可能后面服务器还会挂掉，但这些用户就只能干等或者说等待蛮久后系统抛出个异常给用户，这样用户简直想***什么垃圾网站，这种情况就可以使用Hystrix解决这些问题，可以解决一个接口被高并发导致其他接口无法访问的问题，还能解决用户等待某一时间正常耗时接口没有响应而做出的反馈而不是抛出异常或继续等待，并且还能解决更多问题。

一篇不错Hystrix介绍文章

```tex
https://www.cnblogs.com/huangjuncong/p/9026949.html
```

Hystrix**所需依赖**

```xml
<!-- hystrix -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

主启动类加上注解`@EnableCircuitBreaker`开启对Hystrix的支持

```java
@SpringBootApplication
@EnableEurekaClient
@EnableCircuitBreaker
public class HystrixMain8001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixMain8001.class,args);
    }
}
```

### 服务降级

------

以下提供了三种服务降级方式：类中方法详细配置降级、默认降级、实现类降级

**服务降级案例演示**

@HystrixCommand： 能对某个一个接口定制 Hystrix的超时时间

```java
public @interface HystrixCommand {
    // 若指定时间内被注解的方法没有响应则调用该指定的方法进行反馈给调用者
    String fallbackMethod() default "";
	// 设置指定时间
    HystrixProperty[] commandProperties() default {};
}
```

​	当下这个接口用户请求若在3秒之内没有响应给用户则会进行服务降级操作调用fallback方法进行响应给用户，当然不一定要叫做fallback，名称任意定义的，以下这种场景意思就是当用户调用某个接口正常耗时没有响应就给用户另外一种措施，而不是长时间的等待。

步骤：

​	\> 在方法上标明该注解`@HystrixCommand`，并配置降级方法，指定超时时间（默认1秒）

```java
@GetMapping("/payment/error")
@HystrixCommand(fallbackMethod = "fallback", 
				commandProperties = {
					@HystrixProperty(
                        name ="execution.isolation.thread.timeoutInMilliseconds", 
                        value = "3000")
				}
)
public String error(){
	try {
        //模拟正常耗时3秒因为网络波动该请求耗时5秒
		Thread.sleep(5000);
	} catch (InterruptedException e) {
		e.printStackTrace();
	}
	System.out.println("执行完了哦");
	return Thread.currentThread().getName()+",status: error";
}
//服务降级后的紧急措施
public String fallback(){
	return "啊哦！服务器出小差了";
}
```

以上我们仅给可能耗时长的接口进行了服务降级的配置，现在其他服务也需要服务降级的配置，难道我们在每个方法上面都加这么一段东西吗

```java
fallbackMethod = "fallback", 
				commandProperties = {
					@HystrixProperty(
                        name ="execution.isolation.thread.timeoutInMilliseconds", 
                        value = "3000")
				}
```

这样耦合度又高了许多，一个类中逻辑本不多时，被这么一搞又产生很多一致的代码，那么有没有这样一种，就是当前有10个接口，但8个降级的措施一致，只有俩个不一致，能不能就是弄个默认的服务降级后的措施，就是只要标上`@HystrixCommand`但没有进行详细的配置就使用默认的降级措施，若进行了详细配置就使用指定的，当然是有的那么就是我们的`@DefualtProperties`

**@DefualtProperties**

步骤：

​	\> 在类中编写默认的降级方法

​	\> 将该注解标明在类上，指定默认降级方法

​	\> 在被调用的接口上标明@HystrixCommand没有详细配置则使用默认，反之使用指定

主要功能：提供一个默认的降级措施，若标明了`@HystrixCommand`且没有进行详细配置的接口若出了问题那么就使用DefualtProperties指定的默认降级措施，反之就使用详细配置的降级措施

`@DefualtProperties`需要声明在进行接口调用的类上

```java
@RestController
@DefaultProperties(defaultFallback = "defualtfallback")
public class DefualtPropertiesController {
    @Resource
    private OpenFeignService openFeignService;

    @GetMapping("/defualtTest/error")
    @HystrixCommand //这里使用的为默认降级措施，因为没有详细配置
    public String error(){
        return openFeignService.error();
    }


    @GetMapping("/defualtTest/info")
    @HystrixCommand(fallbackMethod = "fallback",
                    commandProperties = {
            			@HystrixProperty(
                            name = "execution.isolation.thread.timeoutInMilliseconds",
                            value = "2000")
    }) //这里进行了详细配置
    public String info(){
        return openFeignService.info();
    }

    public String defualtfallback(){
        return "这是默认的降级措施";
    }
    public String fallback(){
        return "这是特定的降级措施";
    }
}
```

其次该注解还能提供一个默认的接口响应时间，若不配置则默认1秒，若@HystrixCommand指定了时间则使用指定的。

以上的俩种降级方式适合在微服务提供者中使用，但若在微服务消费方使用就比较混乱了，例如：不管是提供服务的方法还是降级措施的方法都写在同一个类中，所以在微服务消费方通过搭配FeignClient注解实现另一种服务降级，但微服务消费者也可以使用以上这俩种降级方式，只是说第三种在某一方面比较全面。

**搭配FeignClient实现微服务降级**

要想在消费者中使用第三种降级方法，首先找到你要为某个微服务提供服务降级的OpenFeign实现的接口，因为消费者是通过该接口进行服务之间调用的

第一步我们要实现该接口，当然不是实现该接口进行服务调用，而是实现该接口的目的是为该接口下的每一个方法提供一个降级措施，并且在FeignClient注解中标明实现类的class，作用是当FeignClient实现的接口调用出问题时，降级会找到标明的类使用出问题方法的对应降级方法。

**案例演示**

步骤：

​	\> 编写实现类，编写降级措施

​	\> FeignClient注解指定实现类

​	\> yml中配置Feign对Hystrix的支持

`OpenFeign`实现的`CLOUD-PROVIDER-HYSTRIX`微服务调用接口

```java
@Component
@FeignClient("CLOUD-PROVIDER-HYSTRIX"，fallback = OpenFeignServiceFallback.class)
public interface OpenFeignService {
    @GetMapping("/payment/error")
    String error();

    @GetMapping("/payment/info")
    String info();
}
```

我们实现该接口

```java
@Component
public class OpenFeignServiceFallback implements OpenFeignService{
    @Override
    public String error() {
        return "这是error的降级措施";
    }

    @Override
    public String info() {
        return "这是info的降级措施";
    }
}
```

application.yml 开启feign对hystrix的支持

```yml
feign:
  hystrix:
    enabled: true
```

------

以上说明了三种服务降级的案例，优先级为：实现类降级 > 类中方法详细配置降级 > 默认降级

但实现类降级也有问题，就是说不能设置超时时间，默认一秒





### 服务熔断

**简介**

​		在微服务架构中,存在着多个微服务,彼此之间可能存在依赖关系,当某个单元出现故障或者网络不通畅时,就会因为依赖关系形成故障蔓延,最终导致整个系统瘫痪,相对于传统架构更加不稳定.为了解决这样的问题,产生了熔断器模式.

**熔断器的作用**

当某个微服务发生故障时,通过熔断器的故障监控,向调用方返回一个错误响应,而不是长时间的等待,这样就不会使线程因调用故障服务被长时间占用不释放,避免了故障在分布式系统中蔓延,造成大面积雪崩效应.

**Hystrix 熔断器工作原理**

服务端的服务降级逻辑会因 Hystrix命令调用依赖服务超时而触发,也就是调用服务超时会进入断路回调逻辑处理.

但熔断器发挥作用需要满足熔断器三个重要条件:

**1.快照时间窗**

熔断器确定是否打开需要统计一些请求和错误数据,统计的时间范围就是快照时间窗,默认10秒.

**2.请求总数下限**

在快照时间窗内,必须满足请求总数下限才有资格熔断.默认为20次,如果说5秒内调用次数不足20次,即是请求服务超时,断路也不会打开.

**3.错误百分比下限**

当请求总数满足要求,但是错误率没有超过下限也不会熔断.默认50%.

因此,熔断器打开的条件是:在10秒快照时间窗期内,至少调用20次服务,并且服务调用错误率超过50%,才会打开熔断器.

不满足以上条件熔断器不会打开,服务调用错误只会触发服务降级,也就是调用fallback函数.每个请求时间延迟就是近似hystrix的超时时间.如果请求时间超过设定超时时间后就会返回fallback.当满足上面条件断路器打开,之后再请求调用的时候将不再调用处理逻辑,而是直接调用降级逻辑,会直接返回fallback,不在等待是否超时.通过断路器实现自动发现错误并将降级逻辑切换为主逻辑,减少响应延迟的效果.

在熔断器打开后,处理逻辑并没有结束.当熔断器打开,对处理逻辑进行熔断之后,hystrix会给处理逻辑设置一个休眠时间窗（默认5秒）,在这个时间窗内,降级逻辑为临时的主逻辑,当休眠时间窗结束,熔断器会释放一次请求到处理逻辑,如果此次请求正常返回,那么熔断器闭合,处理逻辑回复正常工作;如果此次请求依然有问题,熔断器重新设置一个休眠时间窗.

通过以上机制,hystrix 熔断器实现了对依赖资源故障的处理,对降级策略的主动切换及对处理逻辑的主动恢复.这使得我们的微服务在依赖外部服务或资源的时候得到了非常好的保护，同时对于一些具备降级逻辑的业务需求可以实现自动化的切换和恢复，相比于设置开关由监控和运维来进行切换的传统实现方式显得更为智能和高效.

**实现原理图**

![熔断](images/Hystrix/熔断.webp)

Hystrix的几个重要的默认配置，这几个配置在`HystrixCommandProperties` 中

```java
//是否开启熔断（默认为true）源码为static初始化
private static final Boolean default_circuitBreakerEnabled = true;
//1. 快照时间窗（ms）
static final Integer default_metricsRollingStatisticalWindow = 10000;
//2. 最少请求次数
private static final Integer default_circuitBreakerRequestVolumeThreshold = 20;
//3. Hystrix开启则进入休眠时间，期间有请求的话直接调用降级方法
// 5秒后会再次尝试将部分请求发送到指定服务试探
private static final Integer default_circuitBreakerSleepWindowInMilliseconds = 5000;
//4. 错误比例
private static final Integer default_circuitBreakerErrorThresholdPercentage = 50;
```

这几个属性共同组成了熔断器的核心逻辑，即：

1. 每10秒的窗口期内，当请求次数超过20次，且出错比例超过50%，则触发熔断器打开
2. 熔断器开启时这5秒任何请求来都直接调用服务降级的方法
3. 当熔断器5秒后，会尝试放过去一部分流量进行试探

若以上四个核心配置不满足要求时可以像服务降级那样重写超时时间

**案例演示**

```java
// ================= 服务熔断 ==================
@HystrixCommand(fallbackMethod = "paymentCircuitBreaker_fallback", 
                commandProperties = {
            		@HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds",
                                     value = "15") // 快照时间窗
            		@HystrixProperty(name = "circuitBreaker.requestVolumeThreshold",
                                     value = "10"), // 最少请求次数
            		@HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds",
                                     value = "10000"), // 休眠时间
            		@HystrixProperty(name = "circuitBreaker.errorThresholdPercentage",
                                     value = "60") // 失败率达到多少后跳闸
})
```

配置的属性名称与类中的属性名称不一致，若想清楚类中某个属性在配置属性中的名称是怎么样的请看`HystrixCommandProperties`这个类。



### 服务限流

------





### Hystrix 图形化监控

Hystrix图形化可以监控每个微服务的压力，也就是请求，包括： 成功、短路、错误的请求、超时、拒绝、故障、误差率。

```tex
成功：请求正常次数
短路：熔断器开启时，所有请求都直接转给降级，这些被转给降级的请求会被暂时累计在短路中
错误的请求：暂不清楚
拒绝：暂不清楚
故障：当请求抛出异常时，暂时累计在故障中
误差率：指定时间访问成功与失败的概率
```

要想使用Hystrix图形化界面，需要在项目中配置几个东西【一般是创建一个新项目启动Hystrix图形化界面】

**图形化依赖**

```xml
<!--  hystrix图形化依赖 依赖健康检查 -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
</dependency>
<!-- 健康检查 -->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**主启动类加上注解**

```java
@SpringBootApplication
@EnableHystrixDashboard //开启Hystrix的图形化
public class HystrixMain6001 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixMain6001.class,args);
    }
}
```

在需要被监控的项目中加上以下bean配置

```java
@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
public class HystrixMain80 {
    public static void main(String[] args) {
        SpringApplication.run(HystrixMain80.class,args);
    }

    /**
     * 此配置是为了服务监控而配置，与服务容错本身无关，spring cloud升级后的坑
     * ServletRegistrationBean因为springboot的默认路径不是"/hystrix.stream"，
     * 只要在自己的项目里配置上下面的servlet就可以了
     */
    @Bean
    public ServletRegistrationBean getServlet(){
        HystrixMetricsStreamServlet streamServlet 
            = new HystrixMetricsStreamServlet();
        ServletRegistrationBean registrationBean 
            = new ServletRegistrationBean(streamServlet);
        registrationBean.setLoadOnStartup(1);
        registrationBean.addUrlMappings("/hystrix.stream");
        registrationBean.setName("HystrixMetricsStreamServlet");
        return registrationBean;
    }
}
```

不一定需要加在主程序启动类中，可以自己写个Java配置类，将该bean放入spring容器中即可。

Hystrix图形化只能监控支持服务降级的请求接口，若不支持的则无法监控。

当专门用于图形化监控的项目配置好以后可以通过地址访问hystrix图形化界面，我们创建的项目端口为`9001`

```tex
localhost:9001/hystrix
```

![hystrix 图形化](images/Hystrix/hystrix 图形化.png)

此时需要开启被监控的项目，然后在图形化界面输入被监控的项目的ip信息等其他信息就在被监控的项目中制造请求，然后图形化界面就可以察觉到

![hystrix 请求](images/Hystrix/hystrix 请求.png)







## GateWay

​	该项目提供了一个在Spring生态系统之上构建的API网关，Spring Cloud Gateway旨在提供一种简单而有效的方法来路由到API，并为它们提供跨领域的关注点，例如：安全性，监视/指标和弹性。

简单点说就类似于保安，当有请求来时首先得经过它，它会进行一系列的校验处理，若满足条件后才会将请求转发给真正的微服务。

主要功能：统一微服务的请求地址，不向外界暴露微服务真实地址。

Gateway依赖

```xml
<!-- gateway -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

 \> **路由[Route]**：网关的基本构建块。它由**ID**，**目标URI**，**谓词集合**和**过滤器**集合定义。如果聚合谓词为true，则匹配路由。



### 路由的俩种配置

**applicaiton.yml**

```yml
spring:
  cloud:
    gateway:
      routes:
      # 路由器的id，没有固定规则，但要求唯一，建议配合服务名
      - id: test_route1
        # 匹配后提供服务的路由地址
        uri: http://localhost:8001
        # 断言[谓词]，路径相匹配的进行路由
        predicates:
        - Path=/test
```

​		以上就在Gateway中配置了一个路由，当用户请求Gateway时，会校验每个路由的断言，当前配置的断言校验规则为资源路径，若请求Gateway时资源路径为/test那么将满足条件，会将该请求转发给Uri中的路径+当前资源路径`http://localhost:8001/test`.

**Java 配置**

```java
@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        RouteLocatorBuilder.Builder routes = builder.routes();
        routes.route("path_route_atguitu", r ->
                r.path("/test")
                .uri("localhost:8001"));
        return routes.build();
    }
}
```

注意：断言规则不止路径一种，还有很多

![路径校验断言配置](images/GateWay/路径校验断言配置.png)

这些是springcloud官网提供的，不同配置的断言规则案例。

```tex
https://cloud.spring.io/spring-cloud-static/spring-cloud-gateway/2.2.1.RELEASE/reference/html/#the-path-route-predicate-factory
```

### 负载均衡

以前消费者80是通过微服务名称进行调用并且实现负载均衡，但现在80不能直接调用微服务，应该通过Gateway提供的接口进行调用，此时80无法通过ribbon实现负载均衡，因为GateWay只有一个，而GateWay包含了多个微服务，所以GateWay提供了负载均衡

以下配置为Gateway的负载均衡

```yml
spring:
  application:
    name: cloud-getway
  cloud:
    gateway:
      routes:
        # payment_route2 # 路由器的id，没有固定规则，但要求唯一，建议配合服务名
        - id: payment_routh2
          # 匹配后提供服务的路由地址
          uri: lb://CLOUD-PAYMENT-SERVICE
          # 断言，路径相匹配的进行路由
          predicates:
            - Path=/payment/lb/**
```

lb://微服务名称，这样就实现了负载均衡，当80请求Gateway时由Gateway找到微服务名称下的全部微服务经过策略选出一个微服务，将80请求转发给该微服务。



### 动态路由

将路由信息存入redis，通过java的配置方式读取redis中的路由信息，然后存入spring容器中。

### Filter过滤器

全局过滤器，可用于token校验

```java
@Component
public class GatewayFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //判断请求参数的key是否包含一个为token的，若有则放行，否则拒绝访问
        String name = exchange.getRequest().getQueryParams().getFirst("token");
        if(name == null){
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        // 过滤器的优先级
        return 0;
    }
}
```

**全局过滤器配置案例**

```tex
https://www.pianshen.com/article/794881362/
```









## Config

​		每个微服务都有自己的配置文件，如：application.yml但配置中像数据库配置等一些其他配置出现大量相同，一旦此时数据库信息更改了，那么需要更改大量微服务的配置，这显然是不合理的，所以Springcloud 提供了Config，可以将微服务中相同的配置整合成配置文件放置在远程仓库[git]中，Config可以读取远程仓库的配置文件信息，需要配置的微服务只需要去Config中读取即可，这样就能达到一处修改，处处使用的目的。

![Config 执行流程](images/Config/Config 执行流程.png)

**约定**

 当前项目ip：localhost:3344

 git仓库文件：config-dev.yml、config-prod.yml、config-test.yml

**服务端搭建**

------

所需依赖

```xml
<!-- spring cloud config -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

yml 配置

```yml
server:
  port: 3344
spring:
  cloud:
    config:
      server:
        git:
          # git仓库地址
          uri: https://github.com/2665976163/springcloud-config.git
          # 搜索目录
          search-paths:
            - springcloud-config
      # 读取分支
      label: master
```

主启动类加上注解 `@EnableConfigServer`

```java
@SpringBootApplication
@EnableConfigServer
public class ConfigMain3344 {
    public static void main(String[] args) {
        SpringApplication.run(ConfigMain3344.class,args);
    }
}
```

Config服务端只能按照固定格式获取git上的资源，若git上的资源不满足Config的固定格式那么无法获取

![Config 文件](images/Config/Config 文件.png)

Config只能获取按以下形式的固定格式资源.

```path
/{application}/{profile}[/ {label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties
```

**application:**  代表文件名称、**profile:**  代表环境、**label:**  代表分支.

**案例演示**

根据以上图已知git上有三个配置文件，我们Config服务端配置了该git仓库的地址，当有请求按照Config的固定格式请求某个资源时，Config会去git上拉取该资源，并进行返回资源内容，接下来我们只**演示前三种固定格式的写法**，因为都是大同小异。

config-dev.yml

```shell
localhost:3344/config/dev/master #[]包裹的可加可不加，默认master分支
```

```shell
localhost:3344/config-dev.yml #默认为master分支下的资源
```

```shell
localhost:3344/master/config-dev.yml
```

以上者三种链接访问即可获取到指定文件的配置信息。

`若突然有一天配置文件类型为txt该怎么获取？:只能通过第一种方式获取！因为第一种不区分文件类型`

**客户端配置**

------

已知git上的配置是由Config服务端读取的，客户端只需要读取Config服务端的配置即可

所需依赖

```xml
<!-- spring cloud config client -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```

bootstrap.yml

```yml
spring:
  cloud:
    # config客户端配置
    config:
      # 分支名称
      label: master
      # 配置文件名称
      name: config
      # 读取后缀名称
      profile: dev
      # 配置中心地址
      uri: http://localhost:3344
```

application.yml为用户级别，bootstrap.yml为系统级别，bootstrap级别更高。

微服务需要Config服务端的某个配置文件信息，只需要在项目添加以上配置文件配置以上信息即可，以上配置可以读取到ip为`http://localhost:3344`的Config服务端中的master分支下的名称为config-dev的文件。

```tex
http://localhost:3344/config/dev/master
```

但这样会有一个问题，就是说git上的配置若被修改了，config服务端是可以立即获取到修改后的信息的，但读取config的微服务若在运行中则不能及时获取到最新配置的，因为微服务在启动时就会将所有配置读取完毕，当中途出现新配置这些微服务是不会立即读取的，只有通过重新启动才能获取到最新配置，但显然不合理。

![Config 资源更新](images/Config/Config 资源更新.png)

**动态刷新**

------

健康检查提供了一个不是很好的解决办法，但可以不重新启动微服务，使微服务读取到最新的配置信息

微服务引入依赖

```xml
<!--健康检查依赖-->
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

需要刷新配置的类加上注解 

```java
@RefreshScope
```

yml配置

```yml
# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

post请求

```tex
微服务地址/actuator/refresh
```

当请求后，被标明`@RefreshScope`注解的类会重新加载最新配置，若没有加的类不会使用最新配置。





## Bus

 **Bus 消息总线**

​	Spring Cloud Bus将轻量级消息代理程序链接到分布式系统的节点。然后可以使用此代理来广播状态更改（例如配置更改）或其他管理指令。一个关键思想是，总线就像是横向扩展的Spring Boot应用程序的分布式执行器。但是，它也可以用作应用之间的通信渠道。该项目为AMQP经纪人或Kafka提供了入门服务。 

原本在微服务读取Config服务端配置只有在微服务启动时会读取最新的，当微服务在运行时git更改了配置信息，此时微服务是读取不到的，开始我们提供了一个健康检查来处理此问题，当git更新后只需要运维发送post请求给每一个微服务，这样每个微服务就能读取到最新的配置信息，问题是若微服务有上百个那么运维需要发送上百次post请求

![post通知更新](images/Bus/post通知更新.png)

微服务读取的都是Config服务端的配置，而且Config服务端可以感知最新的git配置，那么能不能让微服务去订阅Config服务端，然后当运维更改了配置信息，让运维发送一个请求给Config服务端，由Config服务端进行广播，订阅了Config服务端的微服务就可以更新配置。

![post 一对多](images/Bus/post 一对多.png)

那么听完这么多到底怎么订阅、广播？，这里就要引入消息中间件了RabbitMQ

无论是订阅端还是广播端都需要依赖

```xml
<!-- 增加消息总线RabbitMQ支持 -->
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```

**rabbit mq 安装**

------

docker 安装 rabbitmq

```shell
docker pull rabbitmq:3.8.3-management
```

创建并执行容器

```shell
docker run -d --hostname my-rabbit -p 5672:5672 -p 15672:15672 rabbitmq:3.8.3-management
```

访问图形化地址：默认账号：guest，密码guest

```tex
http://192.168.99.100:15672
```

**广播【Config服务端】**

------

yml配置

```yml
spring:
  # rabbitmq 配置
  rabbitmq:
  	# rabbitmq 地址
    host: 192.168.99.100
    # rabbitmq 端口 不是图形化端口
    port: 5672
    # 账号
    username: guest
    # 密码
    password: guest

# RabbitMQ 相关配置，暴露bus刷新配置的端点
management:
  endpoints:
    web:
      exposure:
        include: "bus-refresh"
```



**订阅【微服务】**

------

bootsrap.yml

```yml
spring:
  # rabbitmq 配置
  rabbitmq:
  	# rabbitmq 地址
    host: 192.168.99.100
    # rabbitmq 端口 不是图形化端口
    port: 5672
    # 账号
    username: guest
    # 密码
    password: guest

# 暴露监控端点
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

当Config端【广播】与微服务端【订阅】进行这样配置以后，只需要运维发送一个post请求给Config端即可刷新所有订阅了Config端的微服务配置

```tex
Config端ip:端口/actuator/bus-refresh
```

若只想刷新某个订阅了的微服务配置

```tex
Config端ip:端口/actuator/bus-refresh/微服务名称:端口
```

微服务名称如：

```yml
# 设置服务名称，作为当前项目注册进Eureka中的名称
spring:
  application:
    name: provider-grade-service
```

**注意：**

​	要想微服务配置被刷新，需要刷新的类上必须带着注解`@RefreshScope`，没带的无法刷新配置信息。







## Sleuth

 Spring Cloud Sleuth为[Spring Cloud](https://cloud.spring.io/)实现了分布式跟踪解决方案 

与 zipkin 组合使用可以图形化监控微服务此刻的关系、异常的追踪， 某段时间内请求的耗时等 ，**如图**

**微服务关系依赖**

![关系依赖](images/sleuth/关系依赖.png)

**异常追踪**

![异常](images/sleuth/异常.png)

**某段时间内请求的耗时**

![耗时](images/sleuth/耗时.png)





**快速入门**

------

**Zipkin 配置**

下载zipkin.jar 

```tex
http://dl.bintray.com/openzipkin/maven/io/zipkin/java/zipkin-server/2.12.9/zipkin-server-2.12.9-exec.jar
```

java -jar zipkin-server-2.12.9-exec.jar 运行

输入 [http://localhost:9411](http://localhost:9411/) 进入zipkin 图形化界面

![zipkin界面](images/sleuth/zipkin界面.png)

这样 zipkin 就配置完毕了

**sleuth 配置**

需要链路监控的微服务配置依赖

```xml
<dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

该启动器内包含俩个启动器 zipkin 自身 还有 sleuth 的启动器

微服务yml 配置

```yml
server:
  port: 8001
spring:
  application:
    name: cloud-sleuth-provider
  zipkin:
    # zipkin 端的地址
    base-url: http://localhost:9411
  sleuth:
    sampler:
      # 所设置的值介于 0 到 1 之间，1 则表示全部采集
      probability: 1
```

**模拟场景：**微服务1、2、3、4

1-4都进行了sleuth配置，1调用2->3->4，当用户对1进行调用，可以通过zipkin图形化查找微服务1的调用记录，然后就可以从调用记录中看到1->2->3->4 耗时、调用是否正常等.





# SpringCloud Alibaba

​		Spring Cloud Alibaba为分布式应用程序开发提供了一站式解决方案。它包含开发分布式应用程序所需的所有组件，使您可以轻松地使用Spring Cloud开发应用程序。

​		使用Spring Cloud Alibaba，您只需添加一些注释和少量配置即可将Spring Cloud应用程序连接到Alibaba的分布式解决方案，并使用Alibaba中间件构建分布式应用程序系统。

![特征](images/springcloud alibaba/特征.png)

**入门**

要想后面的技术可以不添加版本号就请在父pom中添加springcloud alibaba 的依赖 

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-alibaba-dependencies</artifactId>
            <version>2.1.0.RELEASE</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```





## Nacos

 一个更易于构建云原生应用的动态服务发现、配置管理和服务管理平台

nacos 支持 spring、springboot、springcloud三种不同环境的服务注册与发现、配置管理

 https://nacos.io/zh-cn/docs/quick-start-spring.html 

官方地址：https://nacos.io/zh-cn/index.html

**Nacos 下载地址**

```tex
https://github.com/alibaba/nacos/releases/tag/1.2.1
```

**Nacos 启动**

```tex
nacos/bin/startup.cmd
```

**Nacos 访问地址**

```tex
http://localhost:8848/nacos
```

账号密码默认：nacos



### **Nacos 注册与发现**

**微服务提供者**

> nacos 服务发现依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

yml 配置

```yml
server:
  port: 8001
spring:
  application:
    name: nacos-provider-server
  cloud:
    nacos:
      discovery:
        # nacos的地址
        server-addr: localhost:8848
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

management 需要导入健康检查包

```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

启动之后 nacos 中服务管理 -> 服务列表 就可以看见自己的服务且相关详细信息。

![服务案例](images/springcloud alibaba/服务案例.png)

Nacos 依赖集成了 Ribbon 不论是负载均衡，还是集群都与之前的注册中心一致。

**消费者**

导入 nacos 服务发现依赖，与以上提供者一致

```xml
<!-- nacos 注册与发现 -->
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

yml 配置

```yml
server:
  port: 9001
spring:
  application:
    name: nacos-consumer-server
  cloud:
    nacos:
      discovery:
        # nacos的地址
        server-addr: localhost:8848
# 健康检查
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

**微服务调用**

```java
@RestController
public class ConsumerController {

    @Resource
    private RestTemplate restTemplate;

    private String URL = "http://nacos-provider-server";

    @GetMapping("/consumer/providerPort")
    public String providerPort(){
        return restTemplate.getForObject(URL+"/provider/port",String.class);
    }
}
```





### Nacos 配置中心

在nacos图形化中创建配置

**点击添加**

![nacos 创建配置 1](images/springcloud alibaba/nacos 创建配置 1.png)

**编辑信息**

![nacos 创建配置 2](images/springcloud alibaba/nacos 创建配置 2.png)

配置完毕后点击发布即可。

项目若需要读取nacos中的依赖可以进行以下配置

**配置依赖**

```xml
<!-- nacos 配置中心 读取nacos中配置的所需依赖 -->
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

**读取nacos 配置 yml**

```yml
server:
  port: 3344

spring:
  # 环境名称 任意名称
  profiles:
    active: dev
  application:
    name: nacos-config-consumer
  cloud:
    nacos:
      config:
        # nacos 服务地址
        server-addr: localhost:8848
        # 配置文件的前缀
        prefix: application
        # 配置文件的后缀 目前只支持 properties 和 yaml 类型
        file-extension: yaml
      discovery:
        # nacos 服务地址
        server-addr: localhost:8848
```

注意，读取配置必须在bootstrap.yml中配置，否则可能会抛出异常

测试**Controller**

```yml
@RestController
@RefreshScope
public class TestReaderConfigController {
    @Value("${info.content}")
    private String content;

    @GetMapping("/info/content")
    public String getContent(){
        return content;
    }
}
```

注意需要刷新的类上必须加上注解 `@RefreshScope` 否则当在服务运行期间，nacos配置更新，服务无法检测到。

另外nacos 还支持分组、分命名

![nacos 分命名分组](images/springcloud alibaba/nacos 分命名分组.png)

**详情**

```tex
https://www.bilibili.com/video/BV18E411x7eT?p=105
```

![组与命名空间](images/springcloud alibaba/组与命名空间.png)

namespace 不配置默认使用 public 命名空间

group 不配置默认为 DEFAULT_GROUP



### Nacos 配置存储

​	在0.7版本之前，在单机模式时nacos使用嵌入式数据库实现数据的存储，不方便观察数据存储的基本情况。0.7版本增加了支持mysql数据源能力，原本nacos的数据存储在内置数据库进行以下配置可以将数据存储在自己的mysql数据库，具体的操作步骤：

- 1.安装数据库，版本要求：5.6.5+
- 2.初始化mysql数据库，数据库初始化文件：nacos-mysql.sql 
- 3.修改nacos/conf/application.properties文件，支持mysql数据源配置（目前只支持mysql），添加mysql数据源的url、用户名和密码。

数据库初始化文件 nacos/conf/nacos-mysql.sql

```properties
#1. 在自己的mysql中创建一个为nacos_config的库
#2. 将nacos-mysql.sql中的sql全部复制，在nacos_config库中运行
```

修改nacos/conf/application.properties 配置

```properties
# 支持mysql数据源配置（目前只支持mysql）
spring.datasource.platform=mysql

db.num=1
# mysql数据源的url
db.url.0=jdbc:mysql://192.168.99.100:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
# 用户名
db.user=root
# 密码
db.password=123
```

重启nacos，若之前有创建配置，当启动完毕之前的配置没有了，因为数据开始从mysql中读取，数据也存储在自己的mysql中。



### Nacos 集群

nacos 集群采用 docker 搭建

**架构图**

![nacos 集群](images/springcloud alibaba/nacos 集群.png)

从以上看出，首先需要多个 nacos，nacos 实例中需要声明其他 nacos实例，nacos1指定了nacos2、nacos3，nacos2指定了nacos1、nacos3 ...，各个nacos指定完其他nacos后，这时需要一个nginx做反向代理，当微服务进行服务注册时只需要填写nginx的地址由nginx将该服务注册的请求转发给某一个nacos，因为在开始nacos做了互相绑定，当一个nacos中产生了新的微服务其他nacos中也会读取过去，即使该nacos挂掉了其他读取过去的微服务也不会被影响。

**nacos 一条龙**

![nacos 集群步骤1](images/springcloud alibaba/nacos 集群步骤1.png)

**下载并启动nacos**

```shell
# 下载 nacos
docker pull nacos/nacos-server:1.1.4
# 创建 nacos 实例并运行 创建三个nacos容器 其余俩个省略
docker run --env MODE=standalone --name nacos1 -d -p 8838:8848 nacos/nacos-server:1.1.4
```

**修改nacos 存储配置** 保证每个nacos实例的配置都是同一份

```properties
# conf/application.properties 添加内容
spring.datasource.platform=mysql

db.num=1
db.url.0=jdbc:mysql://192.168.900.100:3306/nacos_devtest?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true
db.user=root
db.password=123
# 初始化数据库等..
```

https://nacos.io/zh-cn/docs/deployment.html 官网描述配置mysql

**添加集群配置**  nacos/conf 下新建集群配置 cluster.conf 文件

```powershell
# ip:port 指定其他nacos地址[可以包括自己]
192.168.99.100:8838
192.168.99.100:8828
192.168.99.100:8818
```

**重启 nacos 容器**

```shell
docker restart nacos1 #[docker restart nacos2，docker restart nacos3]
```

nacos 配置完毕



**Nginx 一条龙**

![nacos 集群步骤2](images/springcloud alibaba/nacos 集群步骤2.png)

```shell
# 下载 nginx
docker pull nginx
# 创建 nginx 实例并运行
docker run -d -p 1111:80 --name=nginx nginx
# 进入 nginx 容器  
docker exec -it nginx /bin/bash
# 修改配置文件 /etc/nginx/nginx.conf
vi /etc/nginx/nginx.conf
```

nginx.conf 增加内容

```properties
http {
    #gzip  on;
    # 添加 upstream myserver 与 server
    upstream myserver {
    	# nacos1
        server 192.168.99.100:8838;
    	# nacos2
        server 192.168.99.100:8828;
    	# nacos3
        server 192.168.99.100:8818;
    }

    server {
        location / {
          proxy_pass http://myserver;
        }
    }
    include /etc/nginx/conf.d/*.conf;
}
```

**重启nginx**

```properties
docker restart nginx
```

访问 测试nginx是否配置成功

```tex
192.168.99.100:1111/nacos
```

若进入nacos界面则配置完毕.



**微服务 application.yml 注册指定nginx 地址**

```yml
server:
  port: 9001
spring:
  application:
    name: nacos-consumer-server
  cloud:
    nacos:
      discovery:
        server-addr: 192.168.99.100:1111
```

以上就是nacos 集群的全部内容。







## Sentinel