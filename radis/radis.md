# Radis

开启radis

```cmd
./src/redis-server redis.conf
```

客户端连接radis

```cmd
./redis-cli -h 127.0.0.1 -p 6379
```

查看端口

```cmd
ps -ef | grep redis 
```

关闭radis

```cmd
./redis-cli shutdown
```

