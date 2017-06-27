package cn.itcast.tom.stormlog.logAnalyze.storm.utils;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * 
 * <p>Titile:MyShardedJedisPool</p>
 * <p>Description: Jedis连接池相关</p>
 * @author TOM
 * @date 2017年6月27日 上午9:58:55
 */
public class MyShardedJedisPool {
	private static ShardedJedisPool jedisPool;
	//静态代码块初始化池子配置
	static{
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//控制一个pool最多可以有多少个状态为idle(空闲的)jedis实例
		poolConfig.setMaxIdle(5);
		//控制一个pool可以分配多少个jedis实例,通过pool.getResource()来获取
		//如果值为-1,则表示不限制,如果pool已经分配了maxActive个实例,则此时pool的状态为exhausted(耗尽)
		//在borrow一个jedis实例时候,是否提前进行validate操作,如果为true,则得到的jedis实例均是可用的
		poolConfig.setMaxTotal(-1);
		//表示当borrow(引入)一个jedis实例,最大的等待时间,如果超过等待时间,则直接抛出JedisConnectionException；
		poolConfig.setMaxWaitMillis(5);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		//创建四个redis实例,并封装在list中
		List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
		list.add(new JedisShardInfo("127.0.0.1",6379));
		//创建具有分片功能的jedis连接池
		jedisPool = new ShardedJedisPool(poolConfig, list);
	}
	public static ShardedJedisPool getShardedJedisPool() {
        return jedisPool;
    }
	/**
	 * 
	 * @MethodName:main
	 * @Description:
	 * @param args
	 * @Time: 2017年6月27日 上午10:18:45
	 * @author: TOM
	 */
	public static void main(String[] args) {
		ShardedJedisPool jedisPool = MyShardedJedisPool.getShardedJedisPool();
		ShardedJedis jedis = jedisPool.getResource();
		
        jedis.set("1","maoxiangyi");
        jedis.set("2","itcast");
        jedis.set("3","传智播客");
        jedis.set("4","java学院");
	}
}
