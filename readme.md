#Akka 技术实例

##async-proxy
异步服务代理，入口点mainEntrance
启动dispatchActor作为分发
业务层实现了echo actor和costTime actor
后者为长耗时demo

###已完成功能：
* actor实现
* 消息分发
* 回调

###待完成功能
* dead letter box
* 基于akka http的外部服务调用
* 集群管理

##async-client
异步服务client

实现了远程调用，远程回调
通过clientApplication运行后直接触发远程调用任务


###已完成功能
* 远程actor调用
* 基于path的远程actor回调

###待完成功能
* 错误处理

##流处理示例
演示Akka的流处理功能，实现数据队列

###已完成功能
无

###待完成功能
* 持久化流数据
* 远程流数据拉取
* 多client
