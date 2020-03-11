### 1、简介
汕大顺手邦（SpareTimeForU ）<br>
是一款互助性质的软件，面向汕大师生群体。主打功能是打包、拿快递、帮买东西。
通过悬赏者发布任务内容和赏金（金钱）、“赏金猎人”通过完成任务获得赏金的形式工作。<br>

实现注册登录修改密码，个人主页，修改资料头像裁剪，即时聊天，发布帖子及回复，消息推送等
### 2、效果
![Alt text](http://cdn.daihuo.xlzrs.top/stfu1.gif "520")
### 3、使用技术
原生Android开发<br>
网络使用OkHttp3处理<br>
列表使用RecycleView处理，使用开源库BaseRecyclerViewAdapterHelper<br>
即时聊天功能集成极光IM SDK<br>
开源库picasso处理图片，以及缓存<br>
butterknife注解View，方法<br>
lottie处理开屏动画等<br>

### 4、运行
可以使用git clone [项目链接复制]，或者复制git链接，使用Android Studio的git拉取。拉取后需创捷一个jks文件，在gradle.properties中添加KEY_PATH=jks的路径，还有其它信息KEY_PASS、ALIAS_NAME、ALIAS_PASS，添加好即可编译
