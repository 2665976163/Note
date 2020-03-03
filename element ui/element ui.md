# element ui

饿了么团队研发的一款快速构建网站的组件.



### 表单开发

重置表单

```html
<!-- 核心代码片段 -->
<!-- HTML -->
<el-form ref="mFormRef">
------
<el-button @click="resetForm('mFormRef')">重置</el-button>
<!-- script -->
methods: {
  resetForm (formName) {
    this.$refs[formName].resetFields()
  }
}
```

**过程解析**

```html
定义ref、创建方法绑定事件、点击触发事件
```

> resetFields()

```html
resetFields	对整个表单进行重置，将所有字段值重置为初始值并移除校验结果	
```





### axios跨域请求

```html
https://www.cnblogs.com/s313139232/p/10598071.html
```

