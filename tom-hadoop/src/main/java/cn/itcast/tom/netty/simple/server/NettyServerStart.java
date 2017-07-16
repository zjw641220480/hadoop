package cn.itcast.tom.netty.simple.server;

import io.netty.channel.ChannelFuture;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

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
				//这个是换行符为标识的解码器LineBasedFrameDecoder,支持配置单行最大长度,
				//如果连续读到最大长度后仍然没有发现换行符就会抛出异常,同时忽略掉之前读到的异常码流
				/**
				 * 下面是使用基于包头不固定长度的解码器:LengthFieldBasedFrameDecoder方式解决半包问题
				 * maxFrameLength：解码的帧的最大长度
				 * lengthFieldOffset：长度属性的起始位（偏移位），包中存放有整个大数据包长度的字节，这段字节的其实位置
				 * lengthFieldLength：长度属性的长度，即存放整个大数据包长度的字节所占的长度
				 * lengthAdjustmen：长度调节值，在总长被定义为包含包头长度时，修正信息长度。
				 * initialBytesToStrip：跳过的字节数，根据需要我们跳过lengthFieldLength个字节，以便接收端直接接受到不含“长度属性”的内容
				 * failFast ：为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
				 */
				//channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
				//channel.pipeline().addLast(new LengthFieldPrepender(4));
				/**
				 * 下面是使用特殊分隔符解码器DelimiterBasedFrameDecoder方式解决半包问题,1024,表示缓存字节
				 * 当在指定字节中还未能找到指定分隔符则会抛出异常
				 */
				//channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("&".getBytes())));
				channel.pipeline().addLast(new FixedLengthFrameDecoder(30));
				channel.pipeline().addLast(new StringDecoder());
				channel.pipeline().addLast(new StringEncoder());
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
