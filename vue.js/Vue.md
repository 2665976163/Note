# Vue.js

**简介：**Vue是JavaScript框架、简化DOM操作、响应式数据驱动.

```html
<!-- 未压缩版 -->
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<!-- 压缩版 -->
<script src="https://cdn.jsdelivr.net/npm/vue"></script>
```





**第一个Vue.js程序**

导入vue.js所需文件、创建Vue对象设置el与data属性、使用简洁的模板将数据渲染到界面上.

```html
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<body>
    <div id="xx">
		{{ message }}
    </div>
</body>
<script>
	var xx = new Vue({
        el: '#xx',
  		data: {
    		message: 'Hello Vue!'
  		}
    })
</script>
```

输出

```txt
Hello Vue！
```

**过程解析**

el称为挂载点，当挂载点内部出现data中的{{ 键 }} 则会被替代为该键的值





## **Vue指令**

 指令 是带有 `v-` 前缀的特殊特性 



**v-text**

```html
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<body>
    <div id="xxx">
    	<div v-test="message01">333</div> <!-- div中的内容为 "你好！v-test" -->
    	<div >{{ message01 }}333</div>	<!-- div中的内容为 "你好！v-test333" -->
    </div>
</body>
<script>
	var c = new Vue({
        el:"#xxx",
        data:{
            message01:"你好！v-test"
        }
    })
</script>
```

v-test与{{ message01 }}的区别就是使用v-test的话div中原有的参数会消失，而{{}}不会



**v-html**

```html
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<body>
    <div v-html="test_1" id="xxx">333</div>
</body>
<script>
	var c = new Vue({
        el:"#xxx",
        data:{
            test_1:"<div>你好</div>"
        }
    })
</script>
```

与v-text的区别就是若内容中存在标签一个以文本方式输出，一个以html代码输出.



**v-on**

为元素绑定事件，方式有俩种：v-on:事件="method" || @事件="method"

```html
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<body>
    <div id="wrap">
		<div v-on:click="myFun1">{{ div1 }}</div>
		<div @dblclick="myFun2">{{ div2 }}</div>
	</div>
</body>
<script>
	var c = new Vue({
		el: "#wrap",
		data: {
			div1: "点击我!",
			div2: "双击我!"
		},
		methods: {
			myFun1: function() {
				alert("v-on 触发！");
			},
			myFun2: function() {
				alert("@ 触发！");
				this.div1 += "biubiubiu"; <!-- this在方法中可以修改data中的数据 -->
			}
		}
	})
</script>
```

this在方法中可以修改data中的数据，从而达到动态更改数据的目的.



**v-show**

主要用于隐藏或显示元素

```html
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<body>
	<div id="wrap">
		<div style="width: 100px; height: 100px; background: red;" v-show="isHide">
			喔！！！
		</div>
		<button @click="myFun1">切换显示</button>
	</div>
</body>
<script>
	var c = new Vue({
		el: "#wrap",
		data: {
			isHide : true
		},
		methods: {
			myFun1: function() {
			this.isHide=!this.isHide;
			}
		}
	})
</script>
```





**v-if**

根据表达式的真假切换元素的显示状态、本质是通过操纵dom元素来切换显示状态、表达式的值为true,元素存在于dom树中，为false,从dom树中移除、频繁的切换v-show,反之使用v-if,前者的切换消耗小

```html
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<body>
	<div id="wrap">
		<div style="width: 100px; height: 100px; background: red;" v-if="isHide">
			喔！！！
		</div>
		<button @click="myFun1">切换显示</button>
	</div>
</body>
<script>
	var c = new Vue({
		el: "#wrap",
		data: {
			isHide : true
		},
		methods: {
			myFun1: function() {
			this.isHide=!this.isHide;
			}
		}
	})
</script>
```

v-if 与 v-show 的值中都可以做布尔值运算，比如v-if="1>2" 或 v-show="2>1"



**v-bind**

设置属性值，俩种方式：v-bind:src="属性值 " || :src="属性值"

```html
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<body>
	<div id="wrap" v-bind:src="imgx"></div>
    <!-- 简化<div id="wrap" :src="imgx"> -->
</body>
<script>
	var wrap = new Vue({
        el:"#wrap",
        data:{
            imgx:"img/xxx.png"
        }
    })
</script>
```







**v-for**

迭代数据，类似jstl中的foreach

```html
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<body>
    <ul id="wrap">
        <li v-for="bean in data">{{ bean.name }}</li>
    </ul>
</body>
<script>
	var wrap = new Vue({
        el:"#wrap",
        data:{
            arr:[{name:"张三"},{name:"李四"},{name:"王五"}]
        }
    })
</script>
```





**v-model**

 双向数据绑定，一方值改变，另外一方也会被改变，相互的.

```html
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<body>
	<div id="content">
		<input v-model="content"/>
		<span @click="setData">{{ content }}</span>
	</div>
</body>
<script>
	var content = new Vue({
		el: "#content",
		data: {
			content: "znsd产业园"
		},
		methods: {
			setData: function() {
				this.content = "深圳znsd";
			}
		}
	})
</script>
```











## 案例演示

**天气搜索案例**

```html
<body>
	<div id="wrap">
		<div><img src="img/favicon.ico.png" />深圳智能时代产业园</div>
		<input @keyup.enter="query" v-model="triy" />
		<br/>
		<span @click="clickQuery('深圳')">深圳</span>
		<ul>
			<li v-for="bean in arr">
				  <div>
                     {{ bean.type }} 
                     {{ bean.date }} 
                     {{ bean.fengxiang }} 
                     {{ bean.high }} 
                     {{ bean.low }}
                 </div>
			</li>
		</ul>
	</div>
</body>
```

JavaScript

```javascript
<script>
	var content = new Vue({
		el: "#wrap",
		data: {
			triy: "",
			arr:[]
		},
		methods: {
			query: function() {
				var current = this;
				console.log(current.triy);
				$.ajax({
					url:"http://wthrcdn.etouch.cn/weather_mini?city="+current.triy,
					type:"GET",
					dataType:"json",
					success:function(data){
						current.arr = data.data.forecast;
						console.log(current.arr);
					}
				});
			},
			clickQuery:function(data){
				this.triy = data;
				this.query();
			}
		}
	})
</script>
```









**音乐播放器案例**

**关键字接口**

```text
https://autumnfish.cn/search?keywords=
```

**歌曲接口**

```txt
https://autumnfish.cn/song/url?id=
```

```javascript
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
		<title>Basic ValidateBox - jQuery EasyUI Demo</title>
		<script src="js/jquery-3.1.1.min.js"></script>
		<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
		<style>
			img {
				width: auto;
				height: auto;
				max-width: 15px;
				max-height: 15px;
			}
			
			* {
				margin: 0px;
				padding: 0px;
			}
			
			ul,
			li {
				list-style: none;
			}
		</style>
	</head>

	<body>
		<div id="wrap">
			<div><img src="img/favicon.ico.png" />深圳智能时代产业园</div>
			<input @keyup.enter="query" v-model="enter" />
			<br/>

			<ul>
				<li v-for="bean in muis">

					<div @click="idQuery(bean.mId)" @dblclick="dbClickQuery" style="width: 800px; border: 1px solid red;">歌手：{{ bean.mName }}  歌曲名称： {{ bean.msName }} 【点击即播放】</div>
				</li>

			</ul>
			<audio :src="srcs" controls="controls" id="xxx">
		</div>
	</body>
	<script>
		var wrap = new Vue({
			el: "#wrap",
			data: {
				enter: "",
				muis: [],
				srcs: ""
			},
			methods: {
				query: function() {
					this.muis = [];
					var ht = "https://autumnfish.cn/search?keywords=" + this.enter;
					var that = this.muis;
					$.get(ht, function(data) {
						var arr = data.result.songs;
						console.log(data);
						for(var i = 0; i < arr.length; i++) {
							var a = arr[i].id; //歌曲id
							var b = arr[i].artists[0].name; //歌手名称
							var c = arr[i].name; //歌曲名称
							var jsons = {
								mId: a,
								mName: b,
								msName: c
							};
							that.push(jsons);
						}
					})
				},
				idQuery: function(id) {
					var that = this;
					$.get("https://autumnfish.cn/song/url?id=" + id, function(datas) {
						that.srcs = datas.data[0].url;
					})
				},
				dbClickQuery:function(){
					$("#xxx")[0].play();
				}

			}
		})
	</script>

</html>
```

