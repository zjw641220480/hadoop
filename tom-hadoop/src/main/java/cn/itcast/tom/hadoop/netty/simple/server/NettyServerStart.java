package cn.itcast.tom.hadoop.netty.simple.server;

import io.netty.channel.ChannelFuture;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * <p>Title:NettyServerStart.java</p>
 * <p>Description:Server的启动程序</p>
 * @author TOM
 * @date 2017年4月14日下午6:47:17
 */
public class NettyServerStart {
	/**
	 * 
	 * @MethodName:doNettyServer
	 * @Description:
	 * @throws InterruptedException
	 * @Time:2017年4月14日下午7:08:58
	 * @author:Tom
	 */
	public void doNettyServer() throws InterruptedException{
		EventLoopGroup eventLoopGroup = null;
		//Server端引导类
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		eventLoopGroup = new NioEventLoopGroup();
		serverBootstrap.group(eventLoopGroup)//装配ServerBootstrap
		.channel(NioServerSocketChannel.class)//指定通道类型为NioServerSocketChannel，一种异步模式，OIO阻塞模式为OioServerSocketChannel
		.localAddress("127.0.0.1", 8888)//设置InetSocketAddress让服务器监听某个端口已等待客户端连接。
		.childHandler(new ChannelInitializer<Channel>() {

			@Override
			protected void initChannel(Channel channel) throws Exception {
				channel.pipeline().addLast(new NettyServer());
			}
		});
		// 最后绑定服务器等待直到绑定完成，调用sync()方法会阻塞直到服务器完成绑定,然后服务器等待通道关闭，因为使用sync()，所以关闭操作也会被阻塞。
		try {
			ChannelFuture channelFuture = serverBootstrap.bind().sync();
			System.out.println("开始监听，端口为：" + channelFuture.channel().localAddress());
			channelFuture.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			eventLoopGroup.shutdownGracefully().sync();
		}
	}
	public static void main(String[] args) {
		NettyServerStart nettyServerStart = new NettyServerStart();
		try {
			nettyServerStart.doNettyServer();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
