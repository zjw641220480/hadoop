package cn.itcast.tom.zookeeper.distributed;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 *
 * <p>Title:DistributedClient.java</p>
 * <p>Description:一个客户端,随机选用某个服务端</p>
 * @author TOM
 * @date 2017年4月10日下午5:50:58
 */
public class DistributedClient {

	//zookeeper的主机地址和端口,需要和zookeeper配置文件中的zoo.cfg文件中的保持一致,要ip都ip,要主机名都主机名
	private static final String connectString = "mini01:2181,mini02:2181,mini03:2181";
	//超时时间,当服务端下线2秒内,zookeeper并不认为其已经下线;
	private static final int sessionTimeout = 2000;

	private static ZooKeeper zooKeeper = null;
	
	private final static String parentNode = "/servers"; 
	//加volatile,
	private volatile List<String> serverList = new ArrayList<String>();
	/**
	 * 
	 * @MethodName:getConnect
	 * @Description:获取连接,并且在获取连接的同时获取服务端列表
	 * @return
	 * @Time:2017年4月10日下午5:50:32
	 * @author:Tom
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public ZooKeeper getConnect() throws KeeperException, InterruptedException {
		Watcher watch = new Watcher() {
			public void process(WatchedEvent event) {
				//收到事件通知后的回调函数,应该是我们自己的事件处理逻辑
				System.out.println(event.getType() + "\t" + event.getPath());
				try {
					//收到事件后,重新获取服务器节点信息
					/*if(event.getType().getIntValue()>0){//节点变化,才会重新获取服务器列表
						getServerList();
					}*/
					if(event.getType()==EventType.NodeChildrenChanged){//根据监听到的节点变化情况,来调用自己写的各个方法
						getServerList();
					}
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		if(zooKeeper==null){
			try {
				zooKeeper = new ZooKeeper(connectString, sessionTimeout, watch);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//初始化数据节点;父节点是持久类型的
			if(null==zooKeeper.exists("/servers", true)){
				zooKeeper.create(parentNode, "".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
			}
		}
		return zooKeeper;
	}
	/**
	 * 
	 * @MethodName:getServerList
	 * @Description:获取服务器节点列表
	 * @Time:2017年4月10日下午6:17:44
	 * @author:Tom
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	public void getServerList() throws KeeperException, InterruptedException{
		//这一句会启用监听(获取节点的时候启动监听)
		List<String> list = zooKeeper.getChildren(parentNode, true);
		//先创建一个局部的List来存服务器信息;
		List<String> servers = new ArrayList<String>();
		for(String str:list){
			System.out.println("当前服务器节点:\t"+str);
			byte[] data = zooKeeper.getData(parentNode+"/"+str, false, null);
			servers.add(new String(data));
		}
		//把Servers赋值给成员变量serverList,以提供给各个线程使用;
		serverList = servers;
		System.out.println("服务器节点获取完毕");
	}
	/**
	 * 
	 * @MethodName:getServerIndex
	 * @Description:获取一个服务器节点的编号
	 * @return
	 * @Time:2017年4月11日上午11:12:18
	 * @author:Tom
	 */
	public int getServerIndex(){
		Random random = new Random();
		int count = serverList.size();
		return random.nextInt(count);
	}
	public static void main(String[] args) throws KeeperException, InterruptedException {
		//获取zookeeper的连接
		DistributedClient client = new DistributedClient();
		client.getConnect();
		//获取servers的子节点,从中获取服务器信息列表
		client.getServerList();
		
		while(true){
			int index = client.getServerIndex();
			System.out.println("获取到的服务器端节点名称为:\t"+client.serverList.get(index));
			Thread.currentThread().sleep(1000l);
		}
	}
}
