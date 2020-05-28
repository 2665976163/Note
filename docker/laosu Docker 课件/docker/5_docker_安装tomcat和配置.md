# docker安装tomcat和配置

1.运行容器

​	docker pull tomcat:8.5

```shell
docker run -d --name 'tom1' -p 7899:8080 \
tomcat:8.5
```



2.宿主机理的home目录下新建tomcat目录，复制容器里的conf和webapps到宿主机

​	docker cp  容器ID:/usr/local/tomcat/webapps /home/tomcat

​	docker cp 容器ID:/usr/local/tomcat/conf /home/tomcat



3.把容器里的tomcat里的webapps，logs，conf挂载到宿主机/home/tomcat目录下，方便上传代码，同步持久化日志，以及方便配置tomcat，开启、关闭tomcat

```shell
docker run -d --name 'tom2' -p 7898:8080 --privileged=true \
-v /home/tomcat/webapps:/usr/local/tomcat/webapps \
-v /home/tomcat/logs:/usr/local/tomcat/logs \
-v /home/tomcat/conf:/usr/local/tomcat/conf \ #有问题；测试可能要去掉
tomcat:8.5
```



4.配置tomcat的server.xml文件

```html
在<host>和</host>之间插入如下语句。
<Context    path="/"   docBase="/usr/local/tomcat/webapps/项目名称"   debug="0"    privileged="true">      
</Context>
```







