package cn.itcast.tom.stormlog.logMonitor.bolt;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.itcast.tom.stormlog.logMonitor.domain.Message;
import cn.itcast.tom.stormlog.logMonitor.domain.Record;
import cn.itcast.tom.stormlog.logMonitor.utils.MonitorHandler;

public class PrepareRecordBolt extends BaseBasicBolt {
	private Logger logger = Logger.getLogger(PrepareRecordBolt.class);

	@Override
	public void execute(Tuple input, BasicOutputCollector outputCollector) {
		Message message = (Message) input.getValueByField("message");
		String appId = input.getStringByField("appid");
		// 将触发规则的信息进行通知,直接包括短信和邮件两个;
		MonitorHandler.notifly(appId, message);
		Record record = new Record();
		try {
			// 将此message部分相关信息保存到record中;然后在下一个bolt中进行保存操作
			BeanUtils.copyProperties(record, message);
			outputCollector.emit(new Values(record));
		} catch (Exception e) {

		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("record"));
	}

}
