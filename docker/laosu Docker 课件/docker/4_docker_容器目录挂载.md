# docker Volume

我们可以在创建容器的时候，将宿主机的目录和容器内的目录进行映射，这样可以实现宿主机和容器目录的双向数据自动同步

## cp命令

```shell
docker cp /www/znsd 96f7f14e99ab:/www
docker cp  96f7f14e99ab:/www /tmp/
```

## docker挂载

cp命令比较麻烦！！！

我们通过容器目录挂载，能够轻松实现代码上传，配置文件修改，日志同步等需求

尤其做集群的时候，只要搞一份就可以了

语法：

```shell
docker run -it -v /宿主机目录:/容器目录 [-v /宿主机目录2:/容器目录2]  镜像名
```

> 如果你同步的是多级目录，可能出现权限不足
>
> 可能因为Centos7中的安全selinux把权限禁用掉了，我们需要添加 --privileged=true来解决

示例：

docker  run -it  -v /home/hData:/home/cData  -v /home/hData1:/home/cData1  602e111c06b6  /bin/bash



> 特别提醒(挂载只读目录)：

```shell
docker run -it -v /宿主机目录:/容器目录:ro   镜像名
ro表示read only
```













