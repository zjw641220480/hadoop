package cn.itcast.tom.netty.simple.client;

import java.util.Arrays;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 使用Netty构建客户端
 * <p>Title:NettyClient.java</p>
 * <p>Description:NETTY客户端程序,不包括驱动程序</p>
 * @author TOM
 * @date 2017年4月14日下午7:28:17
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	/**
	 * 从服务端接收到数据后调用
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext context, ByteBuf object) throws Exception {
		System.out.println("客户端读取服务端返回的数据");
		ByteBuf buf = (ByteBuf) object; 
		byte[] req = new byte[buf.readableBytes()];
		String message = new String(req,"utf-8");
		System.out.println("服务端返回的数据为\t"+message);
	}
	/**
	 * 客户端连接服务器完成后会被调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端连接服务器，开始发送数据......");
		byte[] req = "Netty是基于Java NIO的网络应用框架.Netty是一个NIO client-server(客户端服务器)框架，使用Netty可以快速开发网络应用，例如服务器和客户端协议。Netty提供了一种新的方式来使开发网络应用程序，这种新的方式使得它很容易使用和有很强的扩展性。Netty的内部实现时很复杂的，但是Netty提供了简单易用的api从网络处理代码中解耦业务逻辑。Netty是完全基于NIO实现的，所以整个Netty都是异步的 网络应用程序通常需要有较高的可扩展性，无论是Netty还是其他的基于Java &NIO的框架，都会提供可扩展性的解决方案。Netty中一个关键组成部分是它的异步特性.&"
				.getBytes();// 消息
		ByteBuf firstMessage = Unpooled.buffer(req.length);// 发送类
		firstMessage.writeBytes(req);// 发送
		ctx.writeAndFlush(firstMessage);// flush
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	

}
