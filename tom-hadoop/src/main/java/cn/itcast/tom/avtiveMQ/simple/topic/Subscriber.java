package cn.itcast.tom.avtiveMQ.simple.topic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class Subscriber extends Thread{
	@Override
	public void run() {
		String user = ActiveMQConnection.DEFAULT_USER;
		String password = ActiveMQConnection.DEFAULT_PASSWORD;
		String url = ActiveMQConnection.DEFAULT_BROKER_URL;
		String subject = "MQ.TOPIC";
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
		Connection connection;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			final Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(subject);
			// MessageConsumer负责接受消息
			// 下面是创建普通订阅,
			MessageConsumer consumer = session.createConsumer(topic);
			// 下面是创建持久化订阅
			// MessageConsumer consumer = session.createDurableSubscriber(topic,"bbb"); //持久订阅
			consumer.setMessageListener(new MessageListener() {

				public void onMessage(Message msg) {
					TextMessage message = (TextMessage) msg;
					try {
						String hello = message.getStringProperty("hello");
						System.out.println(Thread.currentThread().getName()+"\t订阅者---SecondSubscriber---收到消息：\t" + hello);
						session.commit();
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			// 为了测试效果，注释掉了两行代码，使Session和connection一直处于打开状态
			// session.close();
			// connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		Thread threadA = new Subscriber();
		Thread threadB = new Subscriber();
		Thread threadC = new Subscriber();
		threadA.setName("threadAAAAAAAAAAAAAA");
		threadB.setName("threadBBBBBBBBBBBBBB");
		threadC.setName("threadCCCCCCCCCCCCCC");
		threadA.start();
		threadB.start();
		threadC.start();
	}
}
