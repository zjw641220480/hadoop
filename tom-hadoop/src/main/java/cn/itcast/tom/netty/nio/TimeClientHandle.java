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
		byte[] req = "Netty是基于Java NIO的网络应用框架.Netty是一个NIO client-server(客户端服务器)框架，使用Netty可以快速开发网络应用，例如服务器和客户端协议。Netty提供了一种新的方式来使开发网络应用程序，这种新的方式使得它很容易使用和有很强的扩展性。Netty的内部实现时很复杂的，但是Netty提供了简单易用的api从网络处理代码中解耦业务逻辑。Netty是完全基于NIO实现的，所以整个Netty都是异步的 网络应用程序通常需要有较高的可扩展性，无论是Netty还是其他的基于Java NIO的框架，都会提供可扩展性的解决方案。Netty中一个关键组成部分是它的异步特性.".getBytes();
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
