package cn.itcast.tom.redis.simple;

import redis.clients.jedis.Jedis;

public class RedisCon {
	public static void main(String[] args) {
		Jedis jedis = new Jedis("127.0.0.1",6379);
		String response = jedis.ping();
		System.out.println(response);
	}
}
