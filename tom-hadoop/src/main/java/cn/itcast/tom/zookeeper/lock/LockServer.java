package cn.itcast.tom.zookeeper.lock;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class LockServer {

	// 超时时间
	private static final int SESSION_TIMEOUT = 5000;
	// zookeeper server列表
	private String hosts = "mini01:2181,mini02:2181,mini03:2181";
	private String groupNode = "locks";
	private String subNode = "sub";
	private boolean haveLock = false;

	private ZooKeeper zk;
	// 当前client创建的子节点
	private volatile String thisPath;

	/**
	 * 连接zookeeper
	 */
	public void connectZookeeper() throws Exception {
		zk = new ZooKeeper(hosts, SESSION_TIMEOUT, new Watcher() {

			public void process(WatchedEvent event) {
				try {

					// 子节点发生变化
					if (event.getType() == EventType.NodeChildrenChanged && event.getPath().equals("/" + groupNode)) {
						// thisPath是否是列表中的最小节点
						List<String> childrenNodes = zk.getChildren("/" + groupNode, true);
						String thisNode = thisPath.substring(("/" + groupNode + "/").length());
						// 排序
						Collections.sort(childrenNodes);
						if (childrenNodes.indexOf(thisNode) == 0) {
							doSomething();
							thisPath = zk.create("/" + groupNode + "/" + subNode, null, Ids.OPEN_ACL_UNSAFE,
									CreateMode.EPHEMERAL_SEQUENTIAL);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		// 创建子节点
		thisPath = zk.create("/" + groupNode + "/" + subNode, null, Ids.OPEN_ACL_UNSAFE,
				CreateMode.EPHEMERAL_SEQUENTIAL);

		// wait一小会, 让结果更清晰一些
		Thread.sleep(new Random().nextInt(1000));

		// 监听子节点的变化
		List<String> childrenNodes = zk.getChildren("/" + groupNode, true);

		// 列表中只有一个子节点, 那肯定就是thisPath, 说明client获得锁
		if (childrenNodes.size() == 1) {
			doSomething();
			thisPath = zk.create("/" + groupNode + "/" + subNode, null, Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
		}
	}

	/**
	 * 共享资源的访问逻辑写在这个方法中
	 */
	private void doSomething() throws Exception {
		try {
			System.out.println("gain lock: " + thisPath);
			Thread.sleep(2000);
			// do something
		} finally {
			System.out.println("finished: " + thisPath);
			// 将thisPath删除, 监听thisPath的client将获得通知
			// 相当于释放锁
			zk.delete(this.thisPath, -1);
		}
	}

	public static void main(String[] args) throws Exception {
		LockServer dl = new LockServer();
		dl.connectZookeeper();
		Thread.sleep(Long.MAX_VALUE);
	}
}
