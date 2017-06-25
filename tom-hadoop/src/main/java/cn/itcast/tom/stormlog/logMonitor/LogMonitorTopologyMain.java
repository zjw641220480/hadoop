package cn.itcast.tom.stormlog.logMonitor;

import org.apache.log4j.Logger;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import cn.itcast.tom.stormlog.logMonitor.bolt.FilterBolt;
import cn.itcast.tom.stormlog.logMonitor.bolt.PrepareRecordBolt;
import cn.itcast.tom.stormlog.logMonitor.bolt.SaveMessage2MySql;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

/**
 * 
 * <p>Titile:LogMonitorTopologyMain</p>
 * <p>Description: 总的启动类</p>
 * @author TOM
 * @date 2017年6月24日 上午9:24:30
 */
public class LogMonitorTopologyMain {
	private static Logger logger = Logger.getLogger(LogMonitorTopologyMain.class);

	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException {
		// 使用TopologyBuilder进行构建驱动类
		TopologyBuilder builder = new TopologyBuilder();
		// 设置Kafka的zookeeper集群
		BrokerHosts brokerHosts = new ZkHosts("mini01:2181,mini02:2181,mini03:2181");
		// 初始化配置信息
		SpoutConfig spoutConfig = new SpoutConfig(brokerHosts, "logmonitor", "/logmonitor", "logmonitor");
		// 在Topology中设置spout
		builder.setSpout("kafka-spout", new KafkaSpout(spoutConfig), 3);
		builder.setBolt("filter-bolt", new FilterBolt(), 3).shuffleGrouping("kafka-spout");
		builder.setBolt("prepareRecord-bolt", new PrepareRecordBolt(), 2).fieldsGrouping("filter-bolt",
				new Fields("appId"));
		builder.setBolt("saveMessage-bolt", new SaveMessage2MySql(), 2).shuffleGrouping("prepareRecord-bolt");

		// 启动topology的配置信息
		Config topologyConfig = new Config();
		// 此方法被设置为true的饿时候,storm会记录下每个组件所发射的每条消息
		// 这在本地测试环境中进行调试topology很有效果,但是线上这么做的话会影响性能
		topologyConfig.setDebug(true);
		// storm的运行有两种模式,本地模式和分布式模式
		if (args != null && args.length > 0) {
			// 定义你希望集群分配多少工作进程来执行这个topology
			topologyConfig.setNumWorkers(2);
			StormSubmitter.submitTopologyWithProgressBar(args[0], topologyConfig, builder.createTopology());
		} else {
			topologyConfig.setMaxTaskParallelism(3);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("logmonitor", topologyConfig, builder.createTopology());
			Utils.sleep(100000);
			cluster.shutdown();
		}

	}
}
