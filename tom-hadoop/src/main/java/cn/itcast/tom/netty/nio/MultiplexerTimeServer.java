package cn.itcast.tom.netty.nio;

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
 * <p>Description:简单NIO中的服务端</p>
 * @author TOM
 * @date 2017年4月12日下午7:21:48
 */
public class MultiplexerTimeServer {

	private Selector selector;

	private ServerSocketChannel channel;

	private boolean stop;

	public MultiplexerTimeServer(int port) throws IOException {
		//打开一个selector(选择器,监听器)
		selector = Selector.open();
		//创建一个channel
		channel = ServerSocketChannel.open();//服务端打开一个通道,主要也是对通道的操作;
		channel.configureBlocking(false);//设置则此通道非阻塞模式 
		channel.socket().bind(new InetSocketAddress(port), 1024);//服务端Socket绑定的端口
		//将这个channel注册到selector上(接收请求),每一个channel有其对应的key值
		//注册的是接收请求的事件(监听)
		channel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("The time server is start in port : " + port);
		System.out.println(channel.hashCode());
	}

	public void stop() {
		this.stop = true;
	}
	/**
	 * 
	 * @MethodName:doServer
	 * @Description:主要是对键的筛选,
	 * @throws IOException
	 * @Time: 2017年6月17日 下午6:18:40
	 * @author: TOM
	 */
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
					System.out.println(key.toString());
					//下面方法是对键的使用,解析
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
	}

	private void handleInput(SelectionKey key) throws IOException {
		if (key.isValid()) {
			// 处理新接入的请求消息,建立SocketChannel连接,打通通道,
			if (key.isAcceptable()) {//连接建立,开始监听,注册读取事件
				//下面被注释的根本是不需要的,经验证,这个ServerSocketChannel和上面使用ServerSocketChannel.open()方法打开的同一个(他们的hashCode值一样);
				// Accept the new connection
				//ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
				//System.out.println(ssc.hashCode());
				SocketChannel sc = channel.accept();
				sc.configureBlocking(false);
				// Add the new connection to the selector
				// 此时管道注册可读的的事件
				sc.register(selector, SelectionKey.OP_READ);
			}
			if (key.isReadable()) {
				// Read the data
				//拿到可读的管道监听,
				SocketChannel sc = (SocketChannel) key.channel();
				//从中读取到数据,并写入缓存,缓存大小为(1024)
				ByteBuffer readBuffer = ByteBuffer.allocate(512);
				int readBytes = sc.read(readBuffer);
				if (readBytes > 0) {
					//判断传递过来的数据是否完整;
					readBuffer.flip();
					//创建byte数组,大小为完整的readBuffer长度
					byte[] bytes = new byte[readBuffer.remaining()];
					//把readBuffer中的数据写入到bytes中;
					readBuffer.get(bytes);
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
		    //标明数据写入完全的标志
		    writeBuffer.flip();
		    channel.write(writeBuffer);
		}
	    }

	public static void main(String[] args) throws IOException {
		MultiplexerTimeServer server = new MultiplexerTimeServer(8080);
		server.doServer();
	}
}
