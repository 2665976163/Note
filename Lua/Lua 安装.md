## Linux 安装 Lua

```shell
# wget 下载 Lua 脚本
wget -c http://www.lua.org/ftp/lua-5.3.0.tar.gz
# 解压
tar -vxf lua-5.3.0.tar.gz
# 安装 wget、make、gcc-c++
# 安装 Lua 依赖插件
yum install readline-devel
# 编译
cd lua-5.3.0
make linux
make install
```

没有wget、make、gcc-c++ ? 立即安装

```shell
# weget 安装
yum install wget
# make 安装
yum -y install gcc automake autoconf libtool make
# gcc-c++ 安装
yum install gcc gcc-c++
```

测试是否安装成功

```shell
lua -v #查看版本信息
```

