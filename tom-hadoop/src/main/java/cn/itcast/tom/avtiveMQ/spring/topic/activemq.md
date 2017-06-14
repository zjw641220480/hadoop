# top-hadoop
	使用Spring方式定义ActiveMq订阅;
	cn.itcast.tom.avtiveMQ.spring.topic.TopicProvider,使用Sring方式的消息生产者
	cn.itcast.tom.avtiveMQ.spring.topic.TopicMessageListener,实现MessageListener中的相应方法来接收消息,使用的是监听机制;
	代码基本上都是一致的;
	
	Spring配置文件中需要配置
	1:connectionFactory,连接工厂
	2:topicDestination,消息主题
	3:topicJmsTemplate,模板类,里面持有连接工厂和消息队列,生产者经常会使用到;
	4:topicProvider,消息生产者,里面持有模板类;
	5,topicMessageListenerA:消息监听类,实现了MessageListener,
	6:topicJmsContainerA:消息主题监听容器;