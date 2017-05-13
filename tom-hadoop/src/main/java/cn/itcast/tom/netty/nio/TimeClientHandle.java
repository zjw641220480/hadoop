package cn.itcast.tom.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * <p>Title:TimeClientHadnle.java</p>
 * <p>Description:简单NIO中的客户端</p>
 * @author TOM
 * @date 2017年4月12日下午7:41:28
 */
public class TimeClientHandle {
	private String host;
	private int port;
	private Selector selector;
	private SocketChannel socketChannel;

	public TimeClientHandle(String host, int port) throws IOException {
		this.host = host;
		this.port = port;
		this.selector = Selector.open();
		this.socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
		socketChannel.configureBlocking(false);
	}

	public void doClient() throws IOException {
		//建立连接
		doConnect();
		//连接的最大超时时间;
		selector.select(1000);
		Set<SelectionKey> selectedKeys = selector.selectedKeys();
		Iterator<SelectionKey> it = selectedKeys.iterator();
		SelectionKey key = null;
		while (it.hasNext()) {
			key = it.next();
			it.remove();
			try {
				handleInput(key);
			} catch (Exception e) {
				if (key != null) {
					key.cancel();
					if (key.channel() != null)
						key.channel().close();
				}
			}
		}
	}

	private void doConnect() throws IOException {
		if (socketChannel.isConnected()) {
			//已经建立连接之后,注册的就是读取的监听事件Channel
			socketChannel.register(selector, SelectionKey.OP_READ);
			doWrite(socketChannel);
		} else {
			//向服务端注册连接的SocketChannel,所以服务端(ServerSocketChannel)监听到连接后会得到的是SocketChannel
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
		}
	}

	private void doWrite(SocketChannel sc) throws IOException {
		byte[] req = "QUERY TIME ORDER".getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		sc.write(writeBuffer);
		if (!writeBuffer.hasRemaining())
			System.out.println("Send order 2 server succeed.");
	}

	private void handleInput(SelectionKey key) throws IOException {

		if (key.isValid()) {
			// 判断是否连接成功
			SocketChannel sc = (SocketChannel) key.channel();
			if (key.isConnectable()) {
				if (sc.finishConnect()) {
					sc.register(selector, SelectionKey.OP_READ);
					doWrite(sc);
				} else
					System.exit(1);// 连接失败，进程退出
			}
			if (key.isReadable()) {
				ByteBuffer readBuffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					readBuffer.flip();
					byte[] bytes = new byte[readBuffer.remaining()];
					readBuffer.get(bytes);
					String body = new String(bytes, "UTF-8");
					System.out.println("Now is : " + body);
				} else if (readBytes < 0) {
					// 对端链路关闭
					key.cancel();
					sc.close();
				} else
					; // 读到0字节，忽略
			}
		}

	}
	public static void main(String[] args) throws IOException {
		TimeClientHandle clientHandle = new TimeClientHandle("127.0.0.1", 8080);
		clientHandle.doClient();
		System.out.println("TimeClientHandle.main()");
	}
}
