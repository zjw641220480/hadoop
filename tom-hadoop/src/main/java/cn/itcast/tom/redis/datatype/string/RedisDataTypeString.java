package cn.itcast.tom.redis.datatype.string;

import redis.clients.jedis.Jedis;

/**
 * 
 * <p>Titile:RedisDataTypeString</p>
 * <p>Description: 操作String类型数据</p>
 * @author TOM
 * @date 2017年6月20日 下午5:17:29
 */
public class RedisDataTypeString {
	/**
	 * 
	 * @MethodName:main
	 * @Description:
	 * @param args
	 * @Time: 2017年6月20日 下午5:17:33
	 * @author: TOM
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		//创建Jedis客户端
		Jedis jedis = new Jedis("127.0.0.1",6379);
		//操作一个String字符串
		jedis.set("name", "zhangsan");//插入一个名字
		System.out.println(jedis.get("name"));
		
		//对键为String类型,而值为数字的时候才可以进行值的增减;
		jedis.set("age", "17");//当redis中已经有对应key的时候,是重置;
		jedis.incr("age");
		System.out.println("年龄增长后的值为:\t"+jedis.get("age"));
		jedis.decr("age");
		System.out.println("年龄自减后的值为:\t"+jedis.get("age"));
		
		//批量设置和批量获取数据
		jedis.mset("aaa","AAA","bbb","BBB","ccc","CCC","ddd","DDD");
		System.out.println(jedis.mget("aaa","ddd","name"));
		
		//设置字段的自动过期
		jedis.setex("wumai", 10, "我们活在仙境中");
		while(jedis.exists("wumai")){
			System.out.println("未过期,获得的数据wumai:\t"+jedis.get("wumai"));
			Thread.sleep(3000);
		}
		
		//对已经存在的字段设置过期时间
		jedis.expire("name", 1);
		Thread.sleep(2000);
		//当已经过期的时候,值为null
		System.out.println(jedis.get("name"));
	}
}
