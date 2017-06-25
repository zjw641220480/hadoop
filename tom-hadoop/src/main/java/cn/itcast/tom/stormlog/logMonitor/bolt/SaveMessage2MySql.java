package cn.itcast.tom.stormlog.logMonitor.bolt;

import org.apache.log4j.Logger;

import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;
import cn.itcast.tom.stormlog.logMonitor.domain.Record;
import cn.itcast.tom.stormlog.logMonitor.utils.MonitorHandler;

public class SaveMessage2MySql extends BaseBasicBolt {
	private static Logger logger = Logger.getLogger(SaveMessage2MySql.class);
	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		Record record = (Record) input.getValueByField("record");
        MonitorHandler.save(record);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}
	
}
