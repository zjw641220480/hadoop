package cn.itcast.tom.kafka.sendpojo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

public class KafkaConsumerObject {
	public static void main(String[] args) {
        String topic = "test"; // 定义要操作的主题   
        Properties pro = new Properties(); // 定义相应的属性保存 
        pro.setProperty("zookeeper.connect", "mini01:2181"); //这里根据实际情况填写你的zk连接地址
        pro.setProperty("metadata.broker.list", "testlinux:9092"); //根据自己的配置填写连接地址
        pro.setProperty("group.id", "group1");  
        ConsumerConnector consumer = Consumer.createJavaConsumerConnector(new ConsumerConfig(pro));   // 需要定义一个主题的映射的存储集合 
        Map<String,Integer> topicMap = new HashMap<String,Integer>() ;   
        topicMap.put(topic, 1) ; // 设置要读取数据的主题 
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicMap) ;   // 现在只有一个主题，所以此处只接收第一个主题的数据即可   
        KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0) ; // 第一个主题  
        ConsumerIterator<byte[], byte[]> iter = stream.iterator() ; 
        while(iter.hasNext()) { 
//          String msg = new String(iter.next().message()) ;   
        	//下面这一行对拿到的byte数组进行解码为Object对象;
            Member vo = (Member)BeanUtils.byteToObject(iter.next().message());   //接收消息，并将字节数组转换为对象
            System.out.println("接收到消息：" + vo); 
        } 
    }
}
