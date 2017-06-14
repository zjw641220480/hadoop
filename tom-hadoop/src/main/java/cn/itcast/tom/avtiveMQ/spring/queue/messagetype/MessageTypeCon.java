package cn.itcast.tom.avtiveMQ.spring.queue.messagetype;

import javax.annotation.Resource;
import javax.jms.BytesMessage;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.StreamMessage;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;

public class MessageTypeCon implements MessageListener{
	@Resource(name = "jmsTemplate")
	private JmsTemplate jmsTemplate;
	
	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}

	/**
	 * 接受消息
	 */
	public void receive(Destination destination) throws JMSException {
		Message message = jmsTemplate.receive(destination);
		// 如果是文本消息
		if (message instanceof TextMessage) {
			TextMessage tm = (TextMessage) message;
			System.out.println("ConsumerService从队列" + destination.toString() + "收到了消息：\t" + tm.getText());
		}

		// 如果是Map消息
		if (message instanceof MapMessage) {
			MapMessage mm = (MapMessage) message;
			System.out.println("ConsumerService从队列" + destination.toString() + "收到了消息：\t" + mm.getString("name"));
		}

		// 如果是Object消息
		if (message instanceof ObjectMessage) {
			try{
				ObjectMessage om = (ObjectMessage) message;
				System.out.println(om.toString());
				Staff staff = (Staff) om.getObject();
				System.out.println("ConsumerService从队列" + destination.toString() + "收到了消息：\t" + staff);
				System.out.println(staff.getName());
			}catch(Exception e){
				System.out.println(e);
			}
		}

		// 如果是bytes消息
		if (message instanceof BytesMessage) {
			byte[] b = new byte[1024];
			int len = -1;
			BytesMessage bm = (BytesMessage) message;
			while ((len = bm.readBytes(b)) != -1) {
				System.out.println(new String(b, 0, len));
			}
		}

		// 如果是Stream消息
		if (message instanceof StreamMessage) {
			StreamMessage sm = (StreamMessage) message;
			System.out.println(sm.readString());
			System.out.println(sm.readInt());
		}
	}

	@Override
	public void onMessage(Message message) {
		
	}
}
