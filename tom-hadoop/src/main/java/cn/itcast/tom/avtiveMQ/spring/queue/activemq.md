# top-hadoop
	使用Spring方式定义ActiveMq队列;
	cn.itcast.tom.avtiveMQ.spring.queue.consum,简单的消费者,使用jmsTemplate来实现接收消息,
	cn.itcast.tom.avtiveMQ.spring.queue.consum2,实现MessageListener中的相应方法来接收消息,使用的是监听机制;
	cn.itcast.tom.avtiveMQ.spring.queue.pro简单的生产者,使用jmsTemplate方式来生产
	
	Spring配置文件中需要配置
	1:connectionFactory,连接工厂
	2:queueDestination,消息队列
	3:jmsTemplate,模板类,里面持有连接工厂和消息队列,
	4:producerService,消息的生产者,持有模板类,生产者经常会用到
	
	5.1:consumerService,消息的消费者,持有模板类,
	
	5.2.1:queueMessageListener,消息队列的监听者
	5.2.2:jmsContainer,消息监听容器,持有连接工厂,监听的队列和消息队列的监听者
	
	