package cn.itcast.tom.redis.datatype.map;

import java.util.Map;

import redis.clients.jedis.Jedis;

/**
 * 
 * <p>Titile:RedisDataTypeMap</p>
 * <p>Description: redis存储map</p>
 * @author TOM
 * @date 2017年6月21日 下午4:35:30
 */
public class RedisDataTypeMap {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1",6379);
		jedis.del("daxia:jingzhongyue");
		//创建一个对象,map的底层也是使用HashSet,故可以使用此种方式来记忆
		jedis.hset("daxia:jingzhongyue", "姓名", "zhangsan");
		jedis.hset("daxia:jingzhongyue", "年龄", "18");
		jedis.hset("daxia:jingzhongyue", "技能", "杀人无形");
		
		//打印对象
		Map<String,String> jingzhongyue = jedis.hgetAll("daxia:jingzhongyue");
		for(String key : jingzhongyue.keySet()){
			System.out.println(jingzhongyue.get(key));
		}
		System.out.println(jedis.hget("daxia:jingzhongyue", "姓名"));
		//给大侠的年龄加10
		String age=jedis.hget("daxia:jingzhongyue", "年龄");
		System.out.println("当前初始的年龄为:\t"+age);
		jedis.hincrBy("daxia:jingzhongyue", "年龄", 10);
		System.out.println("增长10岁之后的年龄\t"+jedis.hget("daxia:jingzhongyue", "年龄"));
	}
}
