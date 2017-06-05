package cn.itcast.tom.zookeeper.simple;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * <p>Title:SimpleZkClient.java</p>
 * <p>Description:zookeeper第一个案例</p>
 * @author TOM
 * @date 2017年4月7日下午8:08:54
 */
public class SimpleZkClient {
	//zookeeper的主机地址和端口,需要和zookeeper配置文件中的zoo.cfg文件中的保持一致,要ip都ip,要主机名都主机名
	private static final String connectString = "mini01:2181,mini02:2181,mini03:2181";
	//超时时间
	private static final int sessionTimeout = 2000;
	
	private static ZooKeeper zooKeeper = null;
	@Before
	public void init() throws IOException{
		Watcher watch = new Watcher(){
			
			public void process(WatchedEvent event) {
				//收到事件通知后的回调函数,应该是我们自己的事件处理逻辑
				System.out.print(event.getType()+"\t"+event.getPath());
				System.out.print(event.getType().getIntValue());
				System.out.println("调用了process");
				try {
					getChildren();
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		};
		zooKeeper = new ZooKeeper(connectString, sessionTimeout, watch);
	}
	/**
	 * 
	 * @MethodName:first
	 * @Description:第一个案例
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @Time:2017年4月7日下午8:27:58
	 * @author:Tom
	 */
	@Test
	public void createNode() throws KeeperException, InterruptedException{
		/*
		 * 参数1:要创建节点的路径
		 * 参数2:节点保存的数据
		 * 参数3:节点的权限
		 * 参数4:节点类型
		 */
		String str = zooKeeper.create("/first", "firstData".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println(str);
	}
	/**
	 * 
	 * @MethodName:getChildren
	 * @Description:获得某个节点的信息
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @Time:2017年4月9日下午12:26:48
	 * @author:Tom
	 */
	@Test
	public void getChildren() throws KeeperException, InterruptedException{
		//在这一步的时候会调用该监听
		List<String> list = zooKeeper.getChildren("/", true);
		for(String str:list){
			System.out.println(str);
		}
		System.out.println("SimpleZkClient.getChildren()");
		Thread.sleep(Long.MAX_VALUE);
	}
	/**
	 * 
	 * @MethodName:testGetData
	 * @Description:获取节点保存的数据
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @Time:2017年4月9日下午7:13:26
	 * @author:Tom
	 * @throws UnsupportedEncodingException 
	 */
	@Test
	public void testGetData() throws KeeperException, InterruptedException, UnsupportedEncodingException{
		byte[] bytes = zooKeeper.getData("/first", true, null);
		System.out.println(new String(bytes,"UTF-8"));
	}
	/**
	 * 
	 * @MethodName:testChildrenExist
	 * @Description:判断某个节点是否存在
	 * @throws KeeperException
	 * @throws InterruptedException
	 * @Time:2017年4月9日下午7:10:47
	 * @author:Tom
	 */
	@Test
	public void testChildrenExist() throws KeeperException, InterruptedException{
		Stat stat = zooKeeper.exists("/app1", true);
		System.out.println(stat==null?"not exists":"exists");
	}
	/**
	 * 
	 * @MethodName:deleteChildren
	 * @Description:删除某个节点
	 * @throws InterruptedException
	 * @throws KeeperException
	 * @Time:2017年4月9日下午7:43:31
	 * @author:Tom
	 */
	@Test
	public void testDeleteChildren() throws InterruptedException, KeeperException{
		//参数2:指定要删除的版本,-1表示删除所有版本
		zooKeeper.delete("/first", -1);
	}
	/**
	 * 
	 * @MethodName:testSetData
	 * @Description:
	 * @Time:2017年4月9日下午7:47:50
	 * @author:Tom
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 */
	@Test
	public void testSetData() throws KeeperException, InterruptedException{
		zooKeeper.setData("/eclipse", "eclipseTestSetData".getBytes(), -1);
		byte[] bytes = zooKeeper.getData("/eclipse", true, null);
		System.out.println(new String(bytes));
		
	}
}
