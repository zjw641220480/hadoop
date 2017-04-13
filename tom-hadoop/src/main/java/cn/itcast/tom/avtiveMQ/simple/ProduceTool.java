package cn.itcast.tom.avtiveMQ.simple;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * <p>Title:ProduceTool.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年4月12日下午1:49:43
 */
public class ProduceTool {
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	private String subject = "mytopic";
	private Destination destination = null;
	private Connection connection = null;
	private Session session = null;
	private MessageProducer producer = null;
	/**
	 * 
	 * @MethodName:initialize
	 * @Description:初始化
	 * @throws JMSException
	 * @Time:2017年4月12日下午1:59:03
	 * @author:Tom
	 */
	private void initialize() throws JMSException{
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(user, password, url);
		connection = activeMQConnectionFactory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(subject);
		producer = session.createProducer(destination);
		producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
	}
	/**
	 * 
	 * @MethodName:produceMessage
	 * @Description:发送信息
	 * @param message
	 * @throws JMSException
	 * @Time:2017年4月12日下午2:03:38
	 * @author:Tom
	 */
	public void produceMessage(String message) throws JMSException{
		//后面if判断中的代码也需要像Mybatis中的SqlSession一样,给抽离出来
		if(session==null){
			initialize();
		}
		TextMessage textMessage = session.createTextMessage(message);
		connection.start();
		System.out.println("开始发送信息");
		System.out.println("发送的信息为\t"+message);
		producer.send(textMessage);
		System.out.println("信息发送完成");
	}
	public void close() throws JMSException{
		if(producer!=null){
			producer.close();
		}
		if(session!=null){
			session.close();
		}
		if(connection!=null){
			connection.close();
		}
	}
	public static void main(String[] args) throws JMSException {
		ProduceTool produceTool = new ProduceTool();
		String message="test";
		for(int i=0;i<10;i++){
			produceTool.produceMessage(message+"\t"+i);
		}
		produceTool.close();
		System.out.println();
	}
}
