package cn.itcast.tom.netty.simple.server;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 使用Netty构建服务端
 * <p>Title:NettyServer.java</p>
 * <p>Description:NettyServer端,不包括启动程序</p>
 * @author TOM
 * @date 2017年4月14日下午7:05:04
 */
public class NettyServer extends ChannelInboundHandlerAdapter {
	/**
	 * 服务端读写客户端的数据
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("Server段开始读取数据......");
		ByteBuf buf = (ByteBuf) msg;
		byte[] req = new byte[buf.readableBytes()];
		buf.readBytes(req);
		String message = new String(req,"UTF-8");
		System.out.println("接收到的客户端的数据为:\t"+message);
		//向客户端回写数据
		System.out.println("server开始向client发送数据");
		String currentTime = new Date(System.currentTimeMillis()).toString();
		ByteBuf buf2 = Unpooled.copiedBuffer(currentTime.getBytes());
		ctx.write(buf2);
	}
	/**
	 * 服务端读写完成客户端发来的数据
	 */
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		super.channelReadComplete(ctx);
		System.out.println("服务端读/写数据完成......");
	}
	/**
	 * 当服务端出现异常时候
	 */
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
		ctx.close();
	}
	
}
