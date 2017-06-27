package cn.itcast.tom.stormlog.logAnalyze.storm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.utils.Utils;
import cn.itcast.tom.stormlog.logAnalyze.storm.bolt.MessageFilterBolt;
import cn.itcast.tom.stormlog.logAnalyze.storm.bolt.ProcessMessage;
import cn.itcast.tom.stormlog.logAnalyze.storm.spout.RandomSpout;

/**
 * 
 * <p>Titile:LogAnalyzeTopologyMain</p>
 * <p>Description: 点击流日志分析系统</p>
 * @author TOM
 * @date 2017年6月26日 下午3:37:18
 */
public class LogAnalyzeTopologyMain {
	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kafka-spout", new RandomSpout(), 2);
		builder.setBolt("MessageFilter-bolt", new MessageFilterBolt(),3).shuffleGrouping("kafka-spout");
		builder.setBolt("ProcessMessage-bolt", new ProcessMessage(),3).fieldsGrouping("MessageFilter-bolt",new Fields("type"));
		Config topologConf = new Config();
        if (args != null && args.length > 0) {
            topologConf.setNumWorkers(2);
            try {
				StormSubmitter.submitTopologyWithProgressBar(args[0], topologConf, builder.createTopology());
			} catch (AlreadyAliveException | InvalidTopologyException e) {
				e.printStackTrace();
			}
        } else {
            topologConf.setMaxTaskParallelism(3);
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology("LogAnalyzeTopologyMain", topologConf, builder.createTopology());
            Utils.sleep(10000000);
            cluster.shutdown();
        }
	}
}
