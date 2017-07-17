# top-hadoop
	kafka发送和接收POJO
	cn.itcast.tom.kafka.sendpojo.BeanUtils:对对象进行序列化和反序列化
	cn.itcast.tom.kafka.sendpojo.ObjectEncoder:将对象使用上面方法转换为字节数组进行传输;
	生产者指明序列化的方式:"serializer.class", ObjectEncoder.class.getName()