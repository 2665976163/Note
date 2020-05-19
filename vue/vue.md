# vue

> vue 安装

```cmd
npm install -g @vue/cli
```

若在vue安装时中断了请执行以下操作再重新尝试安装

```cmd
npm cache clean --force
```

> 图形化界面 3.0 新增

```cmd
vue ui
```

> 查询vue版本

```cmd
vue -V
```



restful 风格

导入 qs 库

```shell
yarn add qs
```

请求案例

```vue
export default {
  methods: {
    test1() {
      this.$http.post("test",this.$qs.stringify({id: '123222'}))
      //this.$http.put("test",qs.stringify({id: '123222'}))
      //this.$http.get("test?id=123123")
      //this.$http.delete("test?id=123123")
    }
  }
}
```

存值 取值

```md
const thisUser = JSON.parse(window.sessionStorage.getItem('userBean'))
window.sessionStorage.setItem('userBean', JSON.stringify({ u_Id: 1 }))
```

