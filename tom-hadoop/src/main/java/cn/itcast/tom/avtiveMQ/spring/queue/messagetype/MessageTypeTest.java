package cn.itcast.tom.avtiveMQ.spring.queue.messagetype;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring/applicationContext-messagetype.xml")
public class MessageTypeTest {
	/**
	 * 队列名queue1
	 */
	@Resource(name="queueDestination")
	private Destination queueDestination;
	
	
	/**
	 * 队列消息生产者
	 */
	@Autowired
	@Qualifier("producerService")
	private MessageTypePro producer;

	/**
	 * 队列消息生产者
	 */
	@Autowired
	@Qualifier("consumerService")
	private MessageTypeCon consumer;
	
	/**
	 * 测试生产者向queue1发送消息
	 */
	@Test
	public void testProduce() {
		String msg = "Hello world!";
		producer.sendMessage(msg);
	}
	@Test
	public void testProduceMapMessage(){
		producer.sendMapMessage();
	}
	@Test
	public void testpRroduceObjectMessage(){
		producer.sendObjectMessage();
	}
	@Test
	public void testProduceBytesMessage(){
		producer.sendBytesMessage();
	}
	@Test
	public void testProduceStreamMessage(){
		producer.sendStreamMessage();
	}
	/**
	 * 测试消费者从queue1接受消息
	 * @throws JMSException 
	 */
	@Test
	public void testConsume() throws JMSException {
		producer.sendObjectMessage();
		while(true){
			consumer.receive(queueDestination);
		}
	}
}
