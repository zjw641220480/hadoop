package cn.itcast.tom.storm.simple.wordcount;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class StormWordCount {
	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		//1,准备一个TopologyBuilder
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("mySpout", new MySpout(),2);
		builder.setBolt("mySplitBolt", new MySplitBolt(),2).shuffleGrouping("mySpout");
		//下面这种方式,每个线程收到的内容是固定的
		builder.setBolt("myCountBolt", new MyCountBolt(), 2).fieldsGrouping("mySplitBolt", new Fields("word"));
		//下面这种方式,每个线程收到的内容是随机的;
		//builder.setBolt("myCountBolt", new MyCountBolt(), 4).shuffleGrouping("mySplitBolt");

		//2,创建一个Configuration,用来指定topology需要的Worker数量
		Config config = new Config();
		config.setDebug(true);
		config.setNumWorkers(2);
		//3,提交任务
		//StormSubmitter.submitTopology("mywordcount",config,builder.createTopology());
		//本地模式
		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("mywordcount", config, builder.createTopology());
	}
	
	public static class MySpout extends BaseRichSpout{
		SpoutOutputCollector collector;
		
		//初始化方法
		public void open(Map arg0, TopologyContext arg1, SpoutOutputCollector collector) {
			this.collector = collector;
		}
		//Storm框架在在while(true),在不停的调用nextTuple方法
		//下发给后面Bolt的内容
		public void nextTuple() {
			collector.emit(new Values("i love li mei mei"));
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//定义下发Tuple的标识(仅仅只是标识而已,每个字段一个标识,可以被后面的Blot用来获取内容)
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("line"));
		}
		
	}
	
	public static class MySplitBolt extends BaseRichBolt{
		OutputCollector collector;
		//初始化方法
		public void prepare(Map arg0, TopologyContext topologyContext, OutputCollector outputCollector) {
			this.collector = outputCollector;
		}
		//被Strom框架循环调用
		//第一个类型的Bolt,只是用来切分单词,并对切分的单词计数
		public void execute(Tuple tuple) {
			String value = tuple.getStringByField("line");
			String[] arrWords = value.split(" ");
			for(String word:arrWords){
				collector.emit(new Values(word,1));
			}
		}


		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("word","num"));
		}
		
	}
	
	public static class MyCountBolt extends BaseRichBolt{
		OutputCollector collector;
		Map<String,Integer> map = new HashMap<String, Integer>();;
		public void execute(Tuple tuple) {
			String word = tuple.getString(0);
			Integer num = tuple.getInteger(1);
			System.out.println(Thread.currentThread().getName()+"\t-------------------\t"+word);
			
			if(map.containsKey(word)){
				Integer count = map.get(word);
				map.put(word, count+num);
			}else{
				map.put(word, num);
			}
			System.out.println("count========================="+map);
		}

		public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
			this.collector = outputCollector;
		}

		public void declareOutputFields(OutputFieldsDeclarer arg0) {
			//不输出
		}
		
	}
}
