package cn.itcast.tom.stormlog.logMonitor.bolt;

import java.util.Map;

import org.apache.log4j.Logger;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import cn.itcast.tom.stormlog.logMonitor.domain.Message;
import cn.itcast.tom.stormlog.logMonitor.utils.MonitorHandler;

/**
 * 
 * <p>Titile:FilterBolt</p>
 * <p>Description: 过滤规则消息</p>
 * @author TOM
 * @date 2017年6月24日 上午9:25:52
 */
public class FilterBolt extends BaseBasicBolt {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(FilterBolt.class);

	@Override
	public void prepare(Map stormConf, TopologyContext context) {
		super.prepare(stormConf, context);
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector outputCollector) {
		//获取Kafka发送过来的数据
		String line = input.getString(0);
		//获取Kafka发送的数据,是一个byte的时候
		//byte[] value = (byte[]) input.getValue(0);
		//将数组转化为字符串
		//String line = new String(value);
		//对数据进行解析
		// appid	content
		Message message = MonitorHandler.parser(line);
		if(message==null){
			return;
		}
		if(MonitorHandler.trigger(message)){
			outputCollector.emit(new Values(message.getAppId(),message));
		}
		//定时更新规则信息
        MonitorHandler.scheduleLoad();
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		//下发字段的说明
		declarer.declare(new Fields("appId", "message"));		
	}

}
