package cn.itcast.tom.netty.simple.client;

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
public class NettyClient extends SimpleChannelInboundHandler<ByteBuf> {
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
		byte[] req = "QUERY TIME ORDER".getBytes();//消息
		ByteBuf firstMessage = Unpooled.buffer(req.length);//发送类
		firstMessage.writeBytes(req);//发送
		ctx.writeAndFlush(firstMessage);//flush
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
	

}
