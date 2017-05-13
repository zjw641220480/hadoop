package cn.itcast.tom.netty.sendstring.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 
 * @ClassName:EchoClientHandler
 * @Description:客户端的数据处理类,客户端数据的处理
 * @Time:2017年5月12日
 * @author:Tom
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	/**
	 * 客户端连接服务器后被调用
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("客户端连接服务器，开始发送数据……");
		byte[] req = "QUERY TIME ORDER".getBytes();//消息
		ByteBuf firstMessage = Unpooled.buffer(req.length);//发送类
		firstMessage.writeBytes(req);//发送
		ctx.writeAndFlush(firstMessage);//flush
	}

	/**
	 * 客户端读取服务端返回的数据
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
			throws Exception {
		System.out.println("client 读取server数据..");
		// 服务端返回消息后
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String body = new String(req, "UTF-8");
		System.out.println("服务端数据为 :" + body);
	}

	/**
	 * 读取数据异常时候被调用
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		System.out.println("client exceptionCaught..");
		// 释放资源
		ctx.close();
	}
}
