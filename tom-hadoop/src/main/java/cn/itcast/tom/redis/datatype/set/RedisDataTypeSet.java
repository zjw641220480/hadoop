package cn.itcast.tom.redis.datatype.set;

import java.util.Set;

import redis.clients.jedis.Jedis;

/**
 * 
 * <p>
 * Titile:RedisDataTypeSet
 * </p>
 * <p>
 * Description:Redis中set类型数据
 * </p>
 * 
 * @author TOM
 * @date 2017年6月22日 下午5:59:17
 */
public class RedisDataTypeSet {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		jedis.del("biwu:dengji");
		String[] data = new String[] { "郭靖", "黄蓉", "令狐冲", "杨过", "林冲", "鲁智深", "小女龙", "虚竹", "独孤求败", "张三丰", "王重阳", "张无忌",
				"王重阳", "东方不败", "逍遥子", "乔峰", "虚竹", "段誉", "韦小宝", "王语嫣", "周芷若", "峨眉师太", "慕容复", "郭靖", "乔峰", "王重阳", "王重阳" };
		// 创建并设置一个set的值
		jedis.sadd("biwu:dengji", data);
		// 获取一个set中的所有元素
		Set<String> dataFromRedis = jedis.smembers("biwu:dengji");
		for (String name : dataFromRedis) {
			System.out.print(name + "  ");
		}
		System.out.println();

		// 判断是否包含
		boolean exist = jedis.sismember("biwu:dengji", "王重阳");
		System.out.println("包含王重阳:\t" + exist);

		// set集合中有多少元素
		long totalNum = jedis.scard("biwu:dengji");
		System.out.println("总共有:\t" + totalNum);

		// 此方法也可以做添加使用
		jedis.sadd("biwu:dengji", "zhangsan");
		// 获取一个set中的所有元素
		Set<String> dataFromRedis2 = jedis.smembers("biwu:dengji");
		for (String name : dataFromRedis2) {
			System.out.print(name + "  ");
		}
		System.out.println();

		// 大侠井中月没有来，是因为报名参与另外一个会议 国际武林大会
		String[] daxiaArr = new String[] { "王语嫣", "周芷若", "峨眉师太", "慕容复", "郭靖", "乔峰", "井中月" };
		jedis.sadd("guoji:dengji", daxiaArr); // 国际武林大会登记表
		// 计算两个Set之间的交集
		Set<String> users = jedis.sinter("biwu:dengji", "guoji:dengji");
		for (String name : users) {
			System.out.print(name + " ");
		}
		System.out.println();

		// 计算两个Set之间的并集
		users = jedis.sunion("biwu:dengji", "guoji:dengji");
		for (String name : users) {
			System.out.print(name + " ");
		}
		System.out.println();

		// 计算两个集合的差集
		users = jedis.sdiff("biwu:dengji", "guoji:dengji");
		for (String name : users) {
			System.out.print(name + " ");
		}
		System.out.println();

		// 将两个集合计算出来的差集保存起来，升级为超级Vip
		jedis.sdiffstore("vipdaxia", "biwu:dengji", "guoji:dengji");
		for (String name : jedis.smembers("vipdaxia")) {
			System.out.print(name + " ");
		}
	}
}
