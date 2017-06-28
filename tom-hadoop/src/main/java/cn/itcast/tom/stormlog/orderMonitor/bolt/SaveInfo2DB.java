package cn.itcast.tom.stormlog.orderMonitor.bolt;

import java.util.List;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import cn.itcast.tom.stormlog.orderMonitor.domain.PaymentInfo;
import cn.itcast.tom.stormlog.orderMonitor.utils.OrderMonitorHandler;

public class SaveInfo2DB extends BaseBasicBolt {

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		String firstField = input.getFields().get(0);
		if ("orderId".equals(firstField)) {
			OrderMonitorHandler.saveTrigger(input.getStringByField("orderId"),(List<String>) input.getValueByField("triggerList"));
		}
		if ("paymentInfo".equals(firstField)) {
			OrderMonitorHandler.savePaymentInfo((PaymentInfo) input.getValueByField("paymentInfo"));
		}

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

}
