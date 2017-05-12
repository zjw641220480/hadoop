package cn.itcast.tom.hadoop.netty.simple.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 *
 * <p>Title:NettyClientStart.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年4月14日下午7:22:00
 */
public class NettyClientStart {
	public void doClient() throws InterruptedException{
		// EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
		EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(eventLoopGroup)//多线程处理
		.channel(NioSocketChannel.class)//指定通道类型为NioServerSocketChannel，一种异步模式，OIO阻塞模式为OioServerSocketChannel
		.remoteAddress("127.0.0.1", 8888)
		.handler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel channel) throws Exception {
				channel.pipeline().addLast(new NettyClient());//注册Handler
			}
		});
		ChannelFuture channelFuture;
		try {
			channelFuture = bootstrap.connect().sync();
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			eventLoopGroup.shutdownGracefully().sync();
		}
	}
	public static void main(String[] args) throws InterruptedException {
		NettyClientStart clientStart = new NettyClientStart();
		clientStart.doClient();
	}
}
