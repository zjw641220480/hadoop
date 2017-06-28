package cn.itcast.tom.stormlog.orderMonitor.bolt;

import java.util.List;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.itcast.tom.stormlog.orderMonitor.domain.PaymentInfo;
import cn.itcast.tom.stormlog.orderMonitor.utils.OrderMonitorHandler;

/**
 * 
 * <p>Titile:PaymentInfoParserBolt</p>
 * <p>Description: 解析订单信息</p>
 * @author TOM
 * @date 2017年6月27日 下午9:21:37
 */
public class PaymentInfoParserBolt extends BaseBasicBolt{
	
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		PaymentInfo paymentInfo = (PaymentInfo) input.getValueByField("paymentInfo");
		if (paymentInfo == null) {
            return;
        }
		List<String> triggerList = OrderMonitorHandler.match(paymentInfo);
		triggerList.add("12");
        triggerList.add("13");
        if (triggerList.size() > 0) {
            collector.emit(new Values(paymentInfo.getOrderId(), triggerList));
        }
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("orderId", "triggerList"));
	}
	
}
