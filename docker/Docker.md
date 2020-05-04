# Docker

 Docker 是一个[开源](https://baike.baidu.com/item/开源/246339)的应用容器引擎，让开发者可以打包他们的应用以及依赖包到一个可移植的镜像中，然后发布到任何流行的 [Linux](https://baike.baidu.com/item/Linux)或Windows 机器上，也可以实现[虚拟化](https://baike.baidu.com/item/虚拟化/547949)。容器是完全使用[沙箱](https://baike.baidu.com/item/沙箱/393318)机制，相互之间不会有任何接口。 



> 安装Docker

```shell
# 将yum更新到最新版本
yum update
# 安装需要的软件包 yum-util 提供yum-config-manager功能、另外俩个是devicemapper的依赖
yum install -y yum-utils device-mapper-persistent-data lvm2
# 设置yum 源
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
# 安装docker 若出现输入界面都输入y
yum install -y docker-ce
# 查看docker版本 校验是否安装成功
docker -v
```

**配置阿里云镜像**

```url
https://cr.console.aliyun.com/cn-hangzhou/instances/mirrors
```



#### Docker 服务命令

Docker服务命令就是操作关于docker的命令

```shell
# 启动docker
systemctl start docker
# 停止docker
systemctl stop docker
# 重启docker
systemctl restart docker
# 查询docker服务状态
systemctl status docker
# 设置开机自启动docker服务
systemctl enable docker
```





#### Docker镜像命令

docker镜像就相当于Java的类，若想使用某个类就需要实例化，而容器就是docker镜像的实例，同一个镜像可以有多个容器。

> 查询已下载的镜像

```shell
# 查询所有镜像信息
docker images
# 查询所有镜像id
docker images -q
```

> 搜索镜像

```shell
docker search 镜像名称
# 案例演示
docker search redis
```

> 下载镜像

```shell
docker pull 镜像名称:版本号 #若不指定版本号则默认下载最新版
# 案例演示 不指定版本号
docker pull redis
# 案例演示 指定版本号 若不清楚版本号在hub.docker中可以查询指定镜像版本号
docker pull redis:6.0-rc1
```

> 删除镜像

```shell
# 根据id号删除
docker rmi 版本号
# 根据名称+版本号删除
docker rmi 名称:版本号
# 清除所有镜像
docker rmi `docker images -q`

# 案例演示 根据id号删除 若出现俩个相同的id号则无法删除
docker rmi 5e35e350aded
# 案例演示 根据名称+版本号删除
docker rmi redis:6.0-rc1
```





#### Docker容器命令

docker容器是镜像的实例，每个docker容器都是独立的环境，互相不影响。

> 创建容器

```shell
# 交互式 创建时会进入容器，一旦退出容器则容器关闭
docker run -it --name=容器名称 镜像名称:版本号
# 守护式 创建时容器在后台运行，退出容器不会关闭，需要手动关闭容器
docker run -id --name=容器名称 镜像名称:版本号
 #若想进入守护式容器
 docker exec -it 容器名称
# 案例演示 交互式
docker run -it --name=redis1 redis:6.0-rc1
# 案例演示 守护式
docker run -id --name=redis2 redis:6.0-rc1
  #若想进入守护式容器
  docker exec -it redis2 /bin/bash
```

> 查看容器

```shell
# 查看所有运行的容器
docker ps
# 查看所有的容器
docker ps -a
```

> 关闭容器

```shell
# 关闭容器
docker stop 容器名称 || 容器id
# 案例演示
docker stop redis2
```

> 启动容器

```shell
# 启动容器
docker start 容器名称 || 容器id
# 案例演示
docker start redis2
```

> 删除容器

```shell
# 删除容器
docker rm 容器名称 || 容器id
# 删除全部容器
docker rm `docker ps -aq`
# 案例演示
docker rm redis2
```

> 查看某个容器配置

```shell
# 查看某个容器配置
docker inspect 容器名称 || 容器id
```







#### Docker容器数据卷

docker数据卷就是用于保存容器中产生的数据的，若不使用数据卷则容器删除后产生的数据也会不存在，有数据卷不同的容器可以通过指定同一份数据卷达到交互目的.

> 创建镜像并绑定数据卷

```shell
# 创建镜像并绑定数据卷
docker run -it --name=centos1 -v 宿主机路径:容器路径 centos:7
# 案例演示
docker run -it --name=centos1 -v /root/data:/root/data_container centos:7
# 绑定多个数据卷
docker run -it --name=centos1 \
-v /root/data1:/root/data_container1 \
-v /root/data2:/root/data_container2 \
-v /root/data3:/root/data_container3 \
centos:7
# 当数据卷路径文件夹不存在时会自动创建 路径必须是绝对路径
```





Window Tools Docker **阿里云镜像**

```tex
https://blog.csdn.net/qq_21744873/article/details/104264724
```


