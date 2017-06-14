package cn.itcast.tom.avtiveMQ.spring.queue.consum;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;

public class ConsumerServiceImpl implements ConsumerService{
	private JmsTemplate jmsTemplate;
	
	/**
	 * 接受消息
	 */
	public void receive(Destination destination) {
		TextMessage tm = (TextMessage) jmsTemplate.receive(destination);
		try {
			System.out.println("从队列" + destination.toString() + "收到了消息：\t" + tm.getText());
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

}
