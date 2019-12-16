# npm

一款前端包管理工具，类似于后台的maven，gradle，主要功能用于管理所需的前端文件，比如jquery，iview等.

**安装地址**

```txt
http://nodejs.cn/download/
```

npm是node.js的一部分，当安装node.js后会安装npm.

**查看版本命令**

```cmd
npm -v || node -v  #用于检测是否安装成功
```



**更新npm**

```cmd
npm install npm@latest -g #-g 代表设置全局，在任意地方输入命令都能执行npm
```

**初始化**

```cmd
npm init -y #生成类似与 pom.xml 的配置文件
```

**安装文件**

```cmd
npm install jquery #install 简写 i : 通过这行命令可以将jquery安装到当前目录
```

**手动删除恢复**

```cmd
npm install
```

**卸载文件**

```cmd
npm uninstall jquery #卸载
```

