package cn.itcast.tom.redis.datatype.list;

import java.util.List;

import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;

/**
 * 
 * <p>
 * Titile:RedisDataTypeList
 * </p>
 * <p>
 * Description: Redis实现队列,主要的方法有1:lpush,rpush左边或者右边插入
 * 								2:linsert,从左边开始,某个队员前或后插入
 * 								3:lpop,rpot从左边或者右边起始位置弹出一个
 * 								4:ltrim,从左边某个位置开始截取到某个位置,超出的时候报错,左边小于右边的时候,截取不到数据
 * 								5:lrange:从左边开始遍历
 * </p>
 * 
 * @author TOM
 * @date 2017年6月22日 下午4:24:24
 */
public class RedisDataTypeList {
	public static void main(String[] args) {
		// 创建一个Redis的客户端
		Jedis jedis = new Jedis("127.0.0.1", 6379);
		jedis.del("柜台1");

		// 鸠摩智，虚竹，段誉，乔峰 排队买肯德基,
		// 从队列的左边插入,左边为开始位,右边为结束位
		jedis.lpush("柜台1", "乔峰", "段誉", "虚竹", "鸠摩智");
		for (String name : jedis.lrange("柜台1", 0, -1)) {
			System.out.print(name + "  ");
		}
		System.out.println();

		// 剧情：新来一个人 王语嫣，插队，到第一名。
		jedis.rpush("柜台1", "王语嫣");// 在队列的右边插入
		List<String> list = jedis.lrange("柜台1", 0, -1);
		for (String name : list) {
			System.out.print(name + "  ");
		}
		System.out.println();

		// 剧情：鸠摩智很不高兴，正好慕容复来了，说：慕容兄，你插我前面
		// 在某个队员的前面或者后面插入,在第三个变量的前面或者后面插入
		jedis.linsert("柜台1", BinaryClient.LIST_POSITION.AFTER, "鸠摩智", "慕容复");
		List<String> list1 = jedis.lrange("柜台1", 0, -1);
		for (String name : list1) {
			System.out.print(name + "  ");
		}
		System.out.println();

		// 剧情：插队不文明，为了遏制这种不文明的现象，大决决定打一架。 鸠摩智被打跑了。
		jedis.lpop("柜台1");// 队列在左边起始位置弹出一个;
		for (String name : jedis.lrange("柜台1", 0, -1)) {
			System.out.print(name + "  ");
		}
		System.out.println();

		// 剧情：慕容复一看情况不好，以表哥的身份忽悠王语嫣，把王语嫣打伤。
		jedis.rpop("柜台1");// 队列在右边起始位置弹出一个
		for (String name : jedis.lrange("柜台1", 0, -1)) {
			System.out.print(name + "  ");
		}
		System.out.println();

		// 剧情：在大家打架的时候，我偷偷插队，买了肯德基。
		jedis.rpush("柜台1", "井中月");// 队列在右边插入一个
		for (String name : jedis.lrange("柜台1", 0, -1)) {
			System.out.print(name + "  ");
		}
		System.out.println();

		// 剧情：星宿老怪 突然来了，把 阿紫和游坦之同时弄走了。
		// 从第二个变量截取到第三个变量,左闭右闭;左边小于右边的时候,截取不到数据
		String result = jedis.ltrim("柜台1", 3, 2);
		if ("OK".equals(result)) {
			for (String name : jedis.lrange("柜台1", 0, -1)) {
				System.out.print(name + "  ");
			}
		}

	}
}
