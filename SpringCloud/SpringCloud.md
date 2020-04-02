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