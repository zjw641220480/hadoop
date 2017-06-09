package cn.itcast.tom.storm.ackfail;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.topology.TopologyBuilder;

/**
 * Created by maoxiangyi on 2016/4/25.
 */
public class MyAckFailTopology {

    public static void main(String[] args) throws Exception {
        TopologyBuilder topologyBuilder = new TopologyBuilder();
        topologyBuilder.setSpout("mySpout", new MySpout(), 1);
        topologyBuilder.setBolt("mybolt1", new MyBolt1(), 1).shuffleGrouping("mySpout");

        Config conf = new Config();
        String name = MyAckFailTopology.class.getSimpleName();
        if (args != null && args.length > 0) {
            String nimbus = args[0];
            conf.put(Config.NIMBUS_HOST, nimbus);
            conf.setNumWorkers(1);
            StormSubmitter.submitTopologyWithProgressBar(name, conf, topologyBuilder.createTopology());
        } else {
            LocalCluster cluster = new LocalCluster();
            cluster.submitTopology(name, conf, topologyBuilder.createTopology());
            Thread.sleep(60 * 60 * 1000);
            cluster.shutdown();
        }
    }
}
