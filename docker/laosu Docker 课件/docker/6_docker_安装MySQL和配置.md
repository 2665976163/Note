# docker安装MySQL和配置

1.运行容器

​	docker pull mysql:5.7

```shell
docker run --name some-mysql -p 3306:3306 \
-e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:tag
```



2.宿主机里的home目录下新建mysql目录，复制容器里的conf和webapps到宿主机

​	docker cp  容器ID:/etc/mysql/conf.d /home/mysql/conf.d

​	docker cp  容器ID:/var/log/  /home/mysql/log

​	docker cp 容器ID:/var/lib/mysql  /home/mysql/data



3.把容器里的mysql里的conf.d，log，data文件等 挂载到宿主机/home/mysql目录下，方便上传代码，同步持久化日志，以及方便配置mysql，开启、关闭mysql

```shell
docker run -d --name sumysql -p 3306:3306 \
-v /home/mysql/conf.d:/etc/mysql/conf.d \
-v /home/mysql/log:/var/log/ \
-v /home/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=123456 mysql:5.7
```









