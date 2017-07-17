package cn.itcast.tom.kafka.sendpojo;

import java.util.Date;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * 
 * <p>Titile:KafkaProducerObject</p>
 * <p>Description: 生产者</p>
 * @author TOM
 * @date 2017年7月17日 下午8:06:27
 */
public class KafkaProducerObject {
	public static void main(String[] args) {
        String topic = "test"; // 定义要操作的主题 
        Properties pro = new Properties(); // 定义相应的属性保存 
        pro.setProperty("zookeeper.connect", "mini01:2181"); //这里根据实际情况填写你的zk连接地址
        //pro.setProperty("metadata.broker.list", "192.168.19.128:9092"); //根据自己的配置填写连接地址
        pro.setProperty("serializer.class", ObjectEncoder.class.getName()); //填写刚刚自定义的Encoder类
        Producer<Integer, Object> prod = new Producer<Integer, Object>(new ProducerConfig(pro)); 
        prod.send(new KeyedMessage<Integer, Object>(topic, new Member("姓名",12,new Date(),12.1)));  //测试发送对象数据
    }
}
