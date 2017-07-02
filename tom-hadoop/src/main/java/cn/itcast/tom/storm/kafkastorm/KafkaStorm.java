package cn.itcast.tom.storm.kafkastorm;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.ZkHosts;

/**
 * 
 * <p>Titile:KafkaStorm</p>
 * <p>Description: Kafka和Storm进行结合</p>
 * @author TOM
 * @date 2017年6月20日 上午10:15:53
 */
public class KafkaStorm {
	public static void main(String[] args) {
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kafkaspout", new KafkaSpout(
				new SpoutConfig(new ZkHosts("mini02:9090,mini03:9090"), "topic", "/myKafkaStorm", "kafkaspout")), 1);
		builder.setBolt("mybolt1", new MyBolt()).shuffleGrouping("kafkaspout");
		Config config = new Config();
		config.setDebug(true);
		config.setNumWorkers(2);
		// 3,提交任务
		// StormSubmitter.submitTopology("mywordcount",config,builder.createTopology());
		// 本地模式
		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("mywordcount", config, builder.createTopology());

	}
	
	/**
	 * 
	 * <p>Titile:MyBolt</p>
	 * <p>Description: 订单的Bolt</p>
	 * @author TOM
	 * @date 2017年6月20日 上午11:07:43
	 */
	static class MyBolt extends BaseRichBolt {
		private JedisPool pool;

		@Override
		public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
			// change "maxActive" -> "maxTotal" and "maxWait" -> "maxWaitMillis"
			// in all examples
			JedisPoolConfig config = new JedisPoolConfig();
			// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
			config.setMaxIdle(5);
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
			// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
			config.setMaxTotal(1000 * 100);
			// 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(30);
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);
			/**
			 * 如果你遇到 java.net.SocketTimeoutException: Read timed out
			 * exception的异常信息 请尝试在构造JedisPool的时候设置自己的超时值.
			 * JedisPool默认的超时时间是2秒(单位毫秒)
			 */
			pool = new JedisPool(config, "127.0.0.1", 6379);
		}

		@Override
		public void execute(Tuple input) {
			Jedis jedis = pool.getResource();
			// 获取kafkaSpout发送过来的数据，是一个json
			String string = new String((byte[]) input.getValue(0));
			// 解析json
			OrderInfo orderInfo = (OrderInfo) new Gson().fromJson(string, OrderInfo.class);
			// 整个网站，各个业务线，各个品类，各个店铺，各个品牌，每个商品
			// 获取整个网站的金额统计指标
			// String totalAmount = jedis.get("totalAmount");
			jedis.incrBy("totalAmount", orderInfo.getProductPrice());
			// 获取商品所属业务线的指标信息
			String bid = getBubyProductId(orderInfo.getProductId(), "b");
			// String bAmout = jedis.get(bid+"Amout");
			jedis.incrBy(bid + "Amount", orderInfo.getProductPrice());
			jedis.close();
		}
		/**
		 * 
		 * @MethodName:getBubyProductId
		 * @Description:从redis获取商品所属业务线编号;
		 * @param productId
		 * @param type
		 * @return
		 * @Time: 2017年6月20日 下午4:16:42
		 * @author: TOM
		 */
		private String getBubyProductId(String productId, String type) {
			// key:value
			// index:productID:info---->Map
			// productId-----<各个业务线，各个品类，各个店铺，各个品牌，每个商品>
			Map<String, String> map = new HashMap<>();
			map.put("b", "3c");
			map.put("c", "phone");
			map.put("s", "121");
			map.put("p", "iphone");
			return map.get(type);
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {

		}
	}
}
