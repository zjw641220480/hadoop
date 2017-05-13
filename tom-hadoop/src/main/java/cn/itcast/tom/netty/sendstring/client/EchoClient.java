package cn.itcast.tom.netty.sendstring.client;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * @ClassName:EchoClient
 * @Description:Netty框架的使用之客户端,连接服务器 ,写数据到服务器 , 等待接受服务器返回相同的数据,关闭连接
 * @Time:2017年5月12日
 * @author:Tom
 */
public class EchoClient {

	private final String host;
	private final int port;

	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}
	/**
	 * 
	 * @MethodName:doClient
	 * @Description:
	 * @throws Exception
	 * @Time:2017年5月12日下午3:49:24
	 * @author:Tom
	 */
	public void doClient() throws Exception {
		EventLoopGroup nioEventLoopGroup = null;
		try {
			// 客户端引导类
			Bootstrap bootstrap = new Bootstrap();
			// EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
			nioEventLoopGroup = new NioEventLoopGroup();
			/*
			 * 引导类中需要注入的内容包括
			 * 	线程组,group
			 * 	通道类型channel
			 * 	远程地址remoteAddress
			 * 	业务处理类handler
			 */
			bootstrap.group(nioEventLoopGroup)//多线程处理
					.channel(NioSocketChannel.class)//指定通道类型为NioServerSocketChannel，一种异步模式，OIO阻塞模式为OioServerSocketChannel
					.remoteAddress(new InetSocketAddress(host, port))//地址
					.handler(new ChannelInitializer<SocketChannel>() {//服务端对数据的多次处理
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new EchoClientHandler());//注册handler
						}
					});
			// 链接服务器
			ChannelFuture channelFuture = bootstrap.connect().sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			nioEventLoopGroup.shutdownGracefully().sync();
		}
	}

	public static void main(String[] args) throws Exception {
		new EchoClient("localhost", 20000).doClient();
		System.out.println("客户端运行结束");
	}
}
