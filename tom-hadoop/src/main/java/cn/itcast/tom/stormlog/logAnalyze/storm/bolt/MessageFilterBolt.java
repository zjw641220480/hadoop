package cn.itcast.tom.stormlog.logAnalyze.storm.bolt;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.itcast.tom.stormlog.logAnalyze.storm.domain.LogMessage;
import cn.itcast.tom.stormlog.logAnalyze.storm.utils.LogAnalyzeHandler;

/**
 * 
 * <p>Titile:MessageFilterBolt</p>
 * <p>Description: </p>
 * @author TOM
 * @date 2017年6月26日 下午4:00:00
 */
public class MessageFilterBolt extends BaseBasicBolt{
	/**
	 * 获取spout发送过来的数据进行解析
	 */
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		//获取KafkaSpout发送出来的数据
		String line = input.getString(0);
		//对数据进行解析
		LogMessage logMessage = LogAnalyzeHandler.parser(line);
		if(logMessage==null){
			return;
		}
		collector.emit(new Values(logMessage.getType(),logMessage));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//根据点击内容类型将日志进行区分
		declarer.declare(new Fields("type","message"));
	}
	
}
