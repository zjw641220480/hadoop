package cn.itcast.tom.avtiveMQ.spring.queue.consum;

import javax.jms.Destination;

public interface ConsumerService {
	public void receive(Destination destination);
}
