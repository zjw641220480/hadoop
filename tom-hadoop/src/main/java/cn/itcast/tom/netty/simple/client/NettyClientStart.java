package cn.itcast.tom.netty.simple.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

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
				//半包问题解决的时候客户端和服务端都要指定相同的解决方案
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
				 * 下面是使用特殊分隔符解码器:DelimiterBasedFrameDecoder方式解决半包问题,1024,表示缓存字节
				 * 当在指定字节中还未能找到指定分隔符则会抛出异常
				 */
				//channel.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("&".getBytes())));
				/**
				 * 下面是定长解码器:FixedLengthFrameDecoder,一直没有达到最大缓冲池大小的时候,刽
				 * 这种方式很容易出现乱码
				 */
				channel.pipeline().addLast(new FixedLengthFrameDecoder(30));
				channel.pipeline().addLast(new StringDecoder());
				channel.pipeline().addLast(new StringEncoder());
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
