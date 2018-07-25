麒麟框架
一、简述
unicorn-framework结合springboot、springcloud整合了redis、mybatis、drools，rocketMq、elastic-job、shiro等模块，旨在方便使用者更容易的给自己的应用通过一定的配置便能赋予相应能力，简化整合各种资源的门槛。
二、框架组成：框架目前包含以下14个子模块，每个子模块后续可以进行扩展，包括新模块的添加和规划，后续会持续更新优化。
1、framework-base：框架的集成部分，包括上下文的管理，请求信息的封装等
2、framework-core：框架的核心，定义了一些列框架元素，包括注解、异常定义、异常处理、返回码等。
3、framework-util：框架提供的一些工具类
4、framework-cache：框架基于redis提供分布式缓存、分布式锁、分布式应用session共享等
5、framework-config-server：框架基于springcloud提供分布式配置服务的能力
6、framework-eureka：框架基于springcloud、eureka提供注册中心
7、framework-gateway：框架基于springcloud、zuul提供网关能力
8、framework-mq：框架基于rocketMq提供异步消息机制
9、framework-orm：框架基于mybatis提供数据操作能力
10、framework-drools：规则引擎、提供应用解决复杂规则开发的能力
11、framework-shiro：权限认证
12、framework-elastic-job：分布式任务调度
13、framework-oss：基于阿里云的对象存储服务
14、framework-codegen：基于麒麟框架写的代码生成，方便使用者快速生成项目，并自动生成CRUD等基础功能，解放使用者宝贵的精力去投入业务开发
三、使用
1、framework-cache
配置
redis缓存配置
# REDIS (RedisProperties)
# Redis数据库索引（默认为0）
spring.redis.database=0  
# Redis服务器地址
spring.redis.host=host
# Redis服务器连接端口
spring.redis.port=6379  
# Redis服务器连接密码（默认为空）
#spring.redis.password=
# 连接池最大连接数（使用负值表示没有限制）
spring.redis.pool.max-active=8  
# 连接池最大阻塞等待时间（使用负值表示没有限制）
spring.redis.pool.max-wait=-1  
# 连接池中的最大空闲连接
spring.redis.pool.max-idle=8  
# 连接池中的最小空闲连接
spring.redis.pool.min-idle=0  
# 连接超时时间（毫秒）
spring.redis.timeout=0 
#启用redis存储session
spring.session.store-type=redis
session共享配置
#session配置
#session过期时间设置
unicorn.session.maxInactiveIntervalInSeconds=1800
#响应头部名称设置
unicorn.session.headName=huanuoToken
#cookie名称设置
unicorn.session.cookieName=huanuo
#session命名空间设置
unicorn.session.namespace=internet

2、framework-mq
配置
#rocketmq nameserverAddress设置
 unicorn.rocketmq.nameServerAddress=nameServerAddress
#生产者组设置
 unicorn.rocketmq.producerGroup=
#生产者交易组设置
unicorn.rocketmqproducerTransactionGroup=
#消息发送超时时间设置 （单位：毫秒） 默认 3000毫秒
unicorn.rocketmq.sendMsgTimeout=

