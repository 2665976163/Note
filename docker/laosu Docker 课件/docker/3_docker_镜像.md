# docker镜像

### 结构：C/S架构



![1588350782600](assets/1588350782600.png)

<1\>Images：镜像（容器的源代码，保存容器运行的各种环境）

​	容器的基石

​	层叠的只读文件系统

​	联合加载（union mount）

<2\>Containers：（通过镜像启动，启动执行阶段[Image是构建打包阶段]）

![1588562704267](assets/1588562704267.png)

docker容器的能力：

​	1、文件系统的隔离：每一个容器都有自己的root文件系统

​	2、进程隔离：每个容器都运行在自己的进程环境中

​	3、网络隔离：容器间的虚拟网络接口和IP地址是分开的

​	4、资源隔离和分组：使用cgroups 将CPU和内存之类的资源独立分配给每个Docker容器

<3\>Registry：仓库

​	共有仓库：Docker hub是默认的仓库

​	私有仓库

### Docker镜像命令

1、存储位置：docker info 【/var/lib/docker/】

2、镜像查看：docker images

```shell
docker imsages [OPTIONS] [REPOSITORY]
-a ,--all=false
-f，--filter=[]
--no-trunc=false
-q，--quiet=false
```

3、TAG

​	centos:7.0

​	centos:latest

4、查看镜像

docker inspect [OPTIONS]  CONTAINER |IMAGE  [CONTAINER|IMAGE...]

-f，--format=false

镜像删除：docker rmi  名称(centos:7.3)

```shell
-f , --force=false 强制删除
--no-prune=false 保留被打标签的父镜像
```

**删除所有的镜像：docker rmi $(docker images centos -q)**

5、搜索镜像: docker search centos

```shell
(1)通过官方网站：https://hub.docker.com/ 

(2)通过命令：docker search IMAGE
--automated=false
--no-trunc=false
-s, --stars=0
示例： docker search -s 3 centos  只查找3星以上
```

6、镜像拉去：docker pull  mysql:5.7（不加:5.7拉取最新版mysql镜像）

7、镜像推送：

第一种：推送到hub.docker

```shell
第一步：登录hub.docker.com

第二步：注册账号

第三步：登录 docker login [userName:laosu666  pwd:???]
docker tag local-image:tagname new-repo:tagname
docker push new-repo:tagname
第四步：docker  push NAME[:TAG]
```

第二种：推送到国内的阿里云【适合一些中小型企业，安全性不高的项目】

```shell
$ sudo docker login --username=乐乐开心果123 registry.cn-hangzhou.aliyuncs.com
$ sudo docker tag [ImageId] registry.cn-hangzhou.aliyuncs.com/laosu666/tom:[镜像版本号]
$ sudo docker push registry.cn-hangzhou.aliyuncs.com/laosu666/tom:[镜像版本号]
```

### 镜像构建

```shell
作用：保存对容器的修改，并再次使用

	 自定义镜像的能力

	 以软件的形式打包并分发服务及其运行环境
```

**方式一：docker commit 通过容器构建镜像**

> 语法：docker commit 运行时NAME|运行时ID  新的镜像名称

```shell
-a，--author="" 
-m，--message="" Commit message
-p，--pause=true  Pause container during commit
```

示例：

```shell
1.docker run -it -p 80 --name commit-testing centos /bin/bash
2.yum install -y nginx
3.docker commit -a "laosu666" -m "nginx" commit-testing laosu666/comm_ngnix 
4.docker images
```

docker run -d -p 80  --name  ngnix_web1   laosu666/comm_ngnix    nginx   -g   "daemon off"

##### 保存镜像为文件

  命令：docker save -o 要保存的文件名 要保存的镜像

##### 导入文件生成镜像

  命令：docker load --input 文件 或者 docker load < 文件名