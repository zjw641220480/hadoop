package cn.itcast.tom.hadoop.simple.rpc.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * <p>Title:MultiplexerTimeServer.java</p>
 * <p>Description:NIO中的服务端</p>
 * @author TOM
 * @date 2017年4月12日下午7:21:48
 */
public class MultiplexerTimeServer {

	private Selector selector;

	private ServerSocketChannel channel;

	private boolean stop;

	public MultiplexerTimeServer(int port) throws IOException {
		selector = Selector.open();
		channel = ServerSocketChannel.open();
		channel.configureBlocking(false);
		channel.socket().bind(new InetSocketAddress(port), 1024);
		channel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("The time server is start in port : " + port);
	}

	public void stop() {
		this.stop = true;
	}

	public void doServer() throws IOException {
		while (true) {

			selector.select(1000);
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = selectionKeys.iterator();
			SelectionKey key = null;
			while (it.hasNext()) {
				key = it.next();
				it.remove();
				try {
					handleInput(key);
				} catch (Exception e) {
					e.printStackTrace();
					if (key != null) {
						key.cancel();
						if (key.channel() != null)
							key.channel().close();
					}
				}
			}
		}
	}

	private void handleInput(SelectionKey key) throws IOException {
		if (key.isValid()) {
			//处理接入的请求消息
			if (key.isAcceptable()) {
				ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				SocketChannel sc = ssc.accept();
				sc.configureBlocking(false);
				ssc.register(selector, SelectionKey.OP_READ);
			}
			if (key.isReadable()) {
				SocketChannel sc = (SocketChannel) key.channel();
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				int readBytes = sc.read(buffer);
				if (readBytes > 0) {
					buffer.flip();
					byte[] bytes = new byte[buffer.remaining()];
					buffer.get(bytes);
					String body = new String(bytes, "UTF-8");
					System.out.println("The time server receive order : " + body);
					String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body)
							? new java.util.Date(System.currentTimeMillis()).toString() : "BAD ORDER";
					doWrite(sc, currentTime);
				} else if (readBytes < 0) {
					// 对端链路关闭
					key.cancel();
					sc.close();
				} else
					; // 读到0字节，忽略
			}
		}
	}

	private void doWrite(SocketChannel channel, String response) throws IOException {
		if (response != null && response.trim().length() > 0) {
			byte[] bytes = response.getBytes();
			ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			channel.write(writeBuffer);
		}
	}

	public static void main(String[] args) throws IOException {
		MultiplexerTimeServer server = new MultiplexerTimeServer(8080);
		server.doServer();
	}
}
