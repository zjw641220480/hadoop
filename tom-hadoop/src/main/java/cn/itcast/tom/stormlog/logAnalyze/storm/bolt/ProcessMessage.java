package cn.itcast.tom.stormlog.logAnalyze.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import cn.itcast.tom.stormlog.logAnalyze.storm.domain.LogMessage;
import cn.itcast.tom.stormlog.logAnalyze.storm.utils.LogAnalyzeHandler;

public class ProcessMessage extends BaseBasicBolt{

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		LogMessage logMessage = (LogMessage) input.getValueByField("message");
		LogAnalyzeHandler.process(logMessage);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

}
