package cn.itcast.tom.zookeeper.distributed;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

/**
 *
 * <p>Title:DistributedServer.java</p>
 * <p>Description:多服务节点</p>
 * @author TOM
 * @date 2017年4月10日下午5:47:58
 */
public class DistributedServer {

	//zookeeper的主机地址和端口,需要和zookeeper配置文件中的zoo.cfg文件中的保持一致,要ip都ip,要主机名都主机名
	private static final String connectString = "mini01:2181,mini02:2181,mini03:2181";
	//超时时间
	private static final int sessionTimeout = 2000;

	private static ZooKeeper zooKeeper = null;
	
	private final static String parentNode = "/servers"; 
	/**
	 * 
	 * @MethodName:getConnect
	 * @Description:
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
	public static void main(String[] args) throws KeeperException, InterruptedException {
		//获取zk连接
		DistributedServer distributedServer = new DistributedServer();
		distributedServer.getConnect();
		//利用zk连接向服务器注册信息
		zooKeeper.create(parentNode+"/server01", args[0].getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
		System.out.println("上线的服务端节点为:\t"+args[0]);
		//启动业务程序
		distributedServer.handleBusiness(args[0]);
	}
	/**
	 * 
	 * @MethodName:handleBusiness
	 * @Description:业务逻辑
	 * @Time:2017年4月10日下午6:13:08
	 * @author:Tom
	 */
	public void handleBusiness(String serverName){
		while(true){
			System.out.println("正在运行的节点为:\t"+serverName);
			try {
				Thread.sleep(3000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
