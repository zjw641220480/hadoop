package cn.itcast.tom.hadoop.hadooprpc.client;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

import cn.itcast.tom.hadoop.hadooprpc.common.NameNodeMetaData;

/**
 *
 * <p>Title:RpcClient.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年2月28日上午10:49:02
 */
public class RpcClient {
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		NameNodeMetaData nameNodeMetaData = RPC.getProxy(NameNodeMetaData.class, 1l, new InetSocketAddress("127.0.0.1", 9527), conf);
		String metaData = nameNodeMetaData.getMetaData("/tomcat/tomcat.7.0.zip");
		System.out.println("获取到的MetaData具体数据为:\t"+metaData);
		
	}
}
