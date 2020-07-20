#websocket
## 设想
###ws的session无法序列化到redis，因此在集群中，我们无法将所有WebSocketSession都缓存到redis进行session共享。
###每台服务器都有各自的session。于此相反的是HttpSession，redis可以支持httpsession共享，但是目前没有websocket session共享的方案
###因此走redis websocket session共享这条路是行不通的。
##我的集群方案设计如下
###基于Mq的广播消息模式，websocket本地维护一个Map 存储 sessionKey->webscoketSession,发送ws消息时先通过mq广播出去,
###所有的websocket服务器都监听这个消息，接收到消息如果对于的sessionKey不属于自己则不处理，如果再本地map中能找到
###则通过WebSocketSessionUtils获取到对于的webscoketSession进行消息处理，从而实现了websocket的集群