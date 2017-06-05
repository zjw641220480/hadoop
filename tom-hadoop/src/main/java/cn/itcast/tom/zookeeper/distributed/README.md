# top-hadoop
	主要是对zookeeper监控服务端的上下线,并且多个客户端随机选择一个正在工作的服务端;
	DistributedClient:客户端
	DistributedServer:服务端
	
	Zookeeper服务端注册了多种服务,客户端可以模拟随机访问某种服务,
	
	Hadoop使用Zookeeper是多个NameNode使用同一个节点,节点上保存的数据随NameNode的上下线而数据不同