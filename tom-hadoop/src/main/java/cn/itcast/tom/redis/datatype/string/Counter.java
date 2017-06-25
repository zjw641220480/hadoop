package cn.itcast.tom.redis.datatype.string;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redis.clients.jedis.Jedis;

/**
 * 
 * <p>Titile:Counter</p>
 * <p>Description: 使用redis实现计数器</p>
 * @author TOM
 * @date 2017年6月21日 下午3:51:37
 */
public class Counter {
	public static void main(String[] args) {
		ExecutorService service = Executors.newFixedThreadPool(4);
		service.submit(new Arena("counter", "天龙八部"));
		service.submit(new Arena("counter", "九阳真经"));
		service.submit(new Arena("counter", "九阴真经"));
		service.submit(new BaoMu("counter"));
	}

	/**
	 * 
	 * <p>Titile:Arena</p>
	 * <p>Description:每个擂台比赛一次,计数器加一 </p>
	 * @author TOM
	 * @date 2017年6月21日 下午3:51:19
	 */
	static class Arena implements Callable<String> {
		private Random random = new Random();
		private String redisKey;
		private Jedis jedis;
		private String arenaName;

		public Arena(String redisKey, String arenaName) {
			this.redisKey = redisKey;
			this.arenaName = arenaName;

		}

		@Override
		public String call() throws Exception {
			jedis = new Jedis("127.0.0.1", 6379);
			String[] da = new String[] { "郭靖", "黄蓉", "令狐冲", "杨过", "林冲", "鲁智深", "小女龙", "虚竹", "独孤求败", "张三丰", "王重阳", "张无忌",
					"王重阳", "东方不败", "逍遥子", "乔峰", "虚竹", "段誉", "韦小宝", "王语嫣", "周芷若", "峨眉师太", "慕容复" };
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int p1 = random.nextInt(da.length);
				int p2 = random.nextInt(da.length);
				while (p1 == p2) { // 如果两个大侠出场名字一样，换一个人
					p2 = random.nextInt(da.length);
				}
				System.out.println("在擂台" + arenaName + "上   大侠" + da[p1] + " VS " + da[p2]);
				jedis.incr(redisKey);
			}
		}

	}

	/**
	 * 
	 * <p>Titile:BaoMu</p>
	 * <p>Description:比赛了多少场,通过此类进行报幕; </p>
	 * @author TOM
	 * @date 2017年6月21日 下午3:51:00
	 */
	static class BaoMu implements Callable<String> {
		private Jedis jedis;
		private String redisKey;

		public BaoMu(String redisKey) {
			this.redisKey = redisKey;
		}

		@Override
		public String call() throws Exception {
			jedis = new Jedis("127.0.0.1", 6379);
			while (true) {
				try {
					Thread.sleep(2000);
					System.out.println("===================当前总共比武次数为：" + jedis.get(redisKey));
				} catch (Exception e) {
					System.out.println("擂台被损坏..." + e);
				}
			}
		}
	}
}
