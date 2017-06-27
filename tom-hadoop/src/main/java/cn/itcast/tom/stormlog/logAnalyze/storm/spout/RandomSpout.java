package cn.itcast.tom.stormlog.logAnalyze.storm.spout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.google.gson.Gson;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import cn.itcast.tom.stormlog.logAnalyze.storm.domain.LogMessage;

public class RandomSpout extends BaseRichSpout {
	private SpoutOutputCollector collector;
	private TopologyContext context;
	private List<LogMessage> list;

	public RandomSpout() {
		super();
	}
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
		this.context = context;
		this.collector = collector;
		list = new ArrayList();
		list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
        list.add(new LogMessage(1,"http://www.itcast.cn/product?id=1002",
                "http://www.itcast.cn/","maoxiangyi"));
	}
	/*
	 * 发送消息
	 */
	@Override
	public void nextTuple() {
		final Random random = new Random();
		LogMessage msg = list.get(random.nextInt(4));
		this.collector.emit(new Values(new Gson().toJson(msg)));
		try {
			Thread.currentThread().sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 字段说明
	 */
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("paymentInfo"));
	}

}
