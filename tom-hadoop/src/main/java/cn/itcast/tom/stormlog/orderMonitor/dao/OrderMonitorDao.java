package cn.itcast.tom.stormlog.orderMonitor.dao;

import java.util.List;

import cn.itcast.tom.stormlog.orderMonitor.domain.Condition;
import cn.itcast.tom.stormlog.orderMonitor.domain.PaymentInfo;
import cn.itcast.tom.stormlog.orderMonitor.domain.Trigger;

public interface OrderMonitorDao {

	List<Condition> loadRules();

	void saveTrigger(List<Trigger> list);

	void savePayment(PaymentInfo paymentInfo);
	
}
