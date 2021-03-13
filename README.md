# Java 版 Mqtt 食用说明

## 食用说明
> 不管三七二十一，拉下来跑一遍就知道怎么肥四啦！

在application.yml文件里配置上host,username,password等信息。运行程序
> ps: 如果你是在本地自己搭建的EMQX,默认情况下是可以匿名访问，所以只要填对你的 host 的就行,账号密码不用理先。

浏览器上分别访问下，先订阅再发布 
* 订阅接口：　`http://localhost:9999/register`
* 发布接口：　`http://localhost:9999/send`
 
 看下控制台日志，正常的话就可以看到日志，也就说明跑起来了：
```
... : 注册订阅者,clientId:[sub_1],topic:[topic]
... : 开始订阅 clientId:[sub_1],topic:[topic]
... : 发送成功 ,clientId:[pub_1], topic:[topic], msg: [hello world] 
... : 接收到消息 Topic: [topic], Message: [hello world]
... : 处理业务逻辑... topic:[topic],message:[hello world]
```

## 安装和配置emqx流程
1. 先装一个mqtt的服务，我用的是EMQX，还不错，也有其他的，自行搜索。
EMQX的各种系统的版本都有，装也方便。我是用docker 跑的，本项目提供了docker-compose.yml文件。
默认情况下EMQX的tcp访问端口是1883,ssl访问端口是8883,网页界面端口是18083,也支持websocket等其他的访问方式。
2. 因为EMQX默认情况下是谁都可以访问的，在生产情况下要在`eqmx.conf`文件里把`allow_anonymous`设置成`false`.
然后选择认证方式配置下账号密码，文章可参考下官方文档。下文有链接。
3. 想要用到ssl/tls 的朋友可以参考下文提供的地址，照着来就可以。

> ps: 1简单的装完可以正常跑通过后，再去配置和测试2,完后再测试3.


## 参考文章
[EMQX安装](https://docs.emqx.cn/broker/latest/getting-started/install.html)

[JWT 认证](https://docs.emqx.cn/broker/latest/advanced/auth-jwt.html)

[EMQ X MQTT 服务器启用 SSL/TLS 安全连接](https://www.emqx.cn/blog/emqx-server-ssl-tls-secure-connection-configuration-guide)

[EMQ X 启用双向 SSL/TLS 安全连接](https://www.emqx.cn/blog/enable-two-way-ssl-for-emqx)
