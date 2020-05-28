# Redis Lua

​	Redis 从  2.6.0 版开始就支持 Lua 脚本 ，Redis中有一个内置的 Lua 解析器，Redis提供了俩个用于执行Lua脚本的指令，分别为 EVAL、EVALSHA 

**EVAL**

EVAL "Lua命令" [key的数量] [key] [其他参数]

```shell
eval "return 123" 0 222 333 # eval 大小写不区分
# 控制台输出  123
```

0 代表没有key，222、333都代表其他参数

```shell
eval "return 123" 2 www sss 222 333
```

2 代表 key 有俩个，那么从2之后，后2个就为key，剩余就为其他参数.

**key的数量:**  2  **key:**  www、sss  **其他参数:** 222、333



------

**Lua 脚本获取传入的值**

​	在Lua脚本中可以通过一个全局数组对象 KEYS 获取 Redis 传入的KEY，获取方式：KEYS[1]、KEYS[2] ... ，也可以通过另外一个全局数组对象 ARGV 获取 Redis 传入的其他参数，获取方式：ARGV[1]、ARGV[2] ...

```shell
eval "return {KEYS[1], KEYS[2], ARGV[1], ARGV[2]}" 2 www sss 222 333
```

输出结果：

```shell
 1)  "WWW"
 2)  "SSS"
 3)  "222"
 4)  "333"
```



------

**Lua 脚本执行Redis命令**

可以使用两个不同的Lua函数从Lua脚本调用Redis命令

- `redis.call()`
- `redis.pcall()`

 `redis.call()`类似于`redis.pcall()`，唯一的区别是，如果Redis命令调用将导致错误，`redis.call()`则将引发Lua错误，这将迫使[EVAL](https://redis.io/commands/eval)将错误返回给命令调用者，同时`redis.pcall`将捕获错误并返回Lua表代表错误。

> redis.call('redis命令', [键],[值])   []:代表可有可无，取决于redis命令

```shell
eval "return redis.call('set',KEYS[1],ARGV[1])" 1 foo "张三"
```

> redis.pcall('redis命令', [键],[值])   []:代表可有可无，取决于redis命令

```shell
eval "return redis.pcall('set',KEYS[1],ARGV[1])" 1 foo "张三"
```



## 脚本的原子性

​	Redis使用相同的Lua解释器来运行所有命令。另外，Redis保证以原子方式执行脚本：执行脚本时不会执行其他脚本或Redis命令。这种语义类似于[MULTI](https://redis.io/commands/multi) / [EXEC中的一种](https://redis.io/commands/exec)。从所有其他客户端的角度来看，脚本的效果还是不可见或已经完成。

但是，这也意味着执行慢速脚本不是一个好主意。创建快速脚本并不难，因为脚本开销非常低，但是如果要使用慢速脚本，则应注意，在脚本运行时，没有其他客户端可以执行命令。







**SpringBoot 开发**

key:1 value:100  [商品id:1，库存:100]

设计一个简单的秒杀接口 "/seckill"，key商品Id，value秒杀数量，当10万个线程对/seckill同时间请求通过lua脚本保证线程安全，不出现超卖现象，若只有100个线程进行秒杀可以保证库存被一次秒杀完毕，不会出现库存剩余的现象，导致需要多次秒杀。

**Redis 依赖**

```xml
<!-- 配置使用redis启动器 -->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

> application.yml  redis 基本配置

```yml
spring:
  redis:
    host: 192.168.99.100
    port: 6379
```

Config  lua脚本对象

```java
@Configuration
public class RedisConfig {
    @Bean
    public DefaultRedisScript<List> defaultRedisScript(){
        DefaultRedisScript<List> redisScript = new DefaultRedisScript();
        redisScript.setResultType(List.class);
        // lua 脚本 resource/lua/seckill.lua
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/seckill.lua")));
        return redisScript;
    }
}
```

Controller 秒杀接口

```java
@RestController
public class RedisController {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private DefaultRedisScript<List> defaultRedisScript; // lua 脚本对象

    @GetMapping("/seckill")
    public void test(String key,String value){
        /**
         * List设置lua的KEYS
         */
        List<String> keyList = new ArrayList();
        keyList.add(key);
        /**
         * 调用脚本并执行
         */
		// execute(lua脚本对象,KEYS,ARGV)
        List result = 
            redisTemplate.execute(defaultRedisScript,keyList,new Object[]{value});
        System.out.println(result);
    }
}
```

> lua 脚本

```lua
-- 获取KEYS
local id = KEYS[1]
local count = ARGV[1]
-- 根据商品id获取商品库存并且-1
local thisCount = redis.call('get',id) - 1
-- 库存不为负数则扣减库存
if(thisCount >= 0)
then
-- 扣减库存
redis.call('set',id,thisCount)
end
return thisCount
```



> 其他推荐

```tex
https://www.jianshu.com/p/62cce4ba2b8a
```

