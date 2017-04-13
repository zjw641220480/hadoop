package cn.itcast.tom.avtiveMQ.simple;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 *
 * <p>Title:ConsumerTool.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年4月12日下午2:29:51
 */
public class ConsumerTool implements MessageListener,ExceptionListener{
	private String user = ActiveMQConnection.DEFAULT_USER;
	private String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	private String subject = "mytopic";
	private Destination destination = null;
	private Connection connection = null;
	private Session session = null;
	private MessageConsumer consumer = null;
	private boolean isconnection = false;
	/**
	 * 
	 * @MethodName:initialize
	 * @Description:初始化
	 * @throws JMSException
	 * @Time:2017年4月12日下午2:30:40
	 * @author:Tom
	 */
	private void initialize() throws JMSException{
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(user, password, url);
		connection = activeMQConnectionFactory.createConnection();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		destination = session.createTopic(subject);
		consumer = session.createConsumer(destination);
	}
	/**
	 * 
	 * @MethodName:consumerMessage
	 * @Description:
	 * @Time:2017年4月12日下午3:08:31
	 * @author:Tom
	 * @throws JMSException 
	 */
	public void consumerMessage() throws JMSException{
		initialize();
		connection.start();
		//只要consumer不关闭,则监听会一直存在;
		consumer.setMessageListener(this);
		connection.setExceptionListener(this);
		isconnection = true;
	}
	public void close() throws JMSException{
		if(consumer!=null){
			consumer.close();
		}
		if(session!=null){
			session.close();
		}
		if(connection!=null){
			connection.close();
		}
	}
	/**
	 * 这边会存在监听,一旦生产者向MQ中放入消息,那么消费者即会获得此消息
	 */
	public void onMessage(Message message) {
		if(message instanceof TextMessage){
			TextMessage textMessage = (TextMessage) message;
			try {
				String msg = textMessage.getText();
				System.out.println("消费的消息为:\t"+msg);
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
	public void onException(JMSException exception) {
		isconnection = false;
	}
	public static void main(String[] args) throws JMSException {
		ConsumerTool consumerTool = new ConsumerTool();
		consumerTool.consumerMessage();
	}
}
