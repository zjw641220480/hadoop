package cn.itcast.tom.hadoop.simple.rpc;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;

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
		Bizable bizable = RPC.getProxy(Bizable.class, 1l, new InetSocketAddress("192.168.8.100", 9527), conf);
		String result = bizable.sysHi("TOM");
		System.out.println(result);
	}
}
