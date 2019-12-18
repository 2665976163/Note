# Zookeeper

简介

```txt
https://baike.baidu.com/item/zookeeper/4836397?fr=aladdin
```

**Zookeeper特点**

```java
1) Zookeeper: 一个领导者(Leader) ，多个跟随者(Follower) 组成的集群。
2)集群中只要有半数以上节点存活，Zookeepex集 群就能正常服务。
3)全局数据一致:每个Server保存一份相同的数据副本，Client无论连接到哪个Server,数据都是一致的。
4)更新请求顺序进行，来自同一个Client的更新请求按其发送顺序依次执行。
5)数据更新原子性，一次数据更新要么成功，要么失败。
6)实时性，在一定时间范围内，Client能读到最新数据。
```



