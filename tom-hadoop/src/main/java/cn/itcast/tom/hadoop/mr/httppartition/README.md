# tom-hadoop
	此包针对HTTP_20130313143750.txt文件计算各个用户的上行总流量,下行总流量和总流量;
	HttpMapper:执行此文件的Mapper阶段,
	HttpReducer:执行此文件的Reducer阶段;
	FlowBean:Map和Reducer的输出对象;需要实现Hadoop的序列化接口,以及其序列化方法
	HttpMain:执行map和reducer方法;
	HttpPartitioner:对号码进行分区计算的实现类;


