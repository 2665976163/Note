

1.将springboot项目打成jar包

2.编写Dockerfile文件:文件名 laosu_springboot

内容：

```shell
FROM java:8
VOLUME /tmp
ADD myspringboot.jar /springbootpro/myspringboot.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/springbootpro/myspringboot.jar"]
```

3.jar包和laosu_springboot文件一起放入centos同一目录下即可

   命令行进入那个目录

4.执行命令

```shell
docker build -f laosu_springboot -t myspringbootpro .
```

> 注意：laosu_springboot是你那个文件名
>
> ​           myspringbootpro是你构建后的镜像的名称



5.运行镜像

```shell
 docker run -d --name cspringbootpro -p 6666:8080 springbootpro|f390244b8ff2
```

容器的名称：cspringbootpro

镜像的名称：myspringbootpro