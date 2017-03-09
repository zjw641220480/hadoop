package cn.itcast.tom.hadoop.simple.rpc;

import java.io.IOException;

import org.apache.hadoop.HadoopIllegalArgumentException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.apache.hadoop.ipc.RPC.Server;

/**
 *
 * <p>Title:RpcServer.java</p>
 * <p>Description:RPC协议</p>
 * @author TOM
 * @date 2017年2月28日上午10:31:10
 */
public class RpcServer implements Bizable{
	public String sysHi(String name){
		return "HI~"+ name;
	}
	/**
	 * 
	 * @MethodName:main
	 * @Description:
	 * @param args
	 * @throws ClassNotFoundException
	 * @throws HadoopIllegalArgumentException
	 * @throws IOException
	 * @Time:2017年2月28日上午10:48:40
	 * @author:Tom
	 */
	public static void main(String[] args) throws ClassNotFoundException, HadoopIllegalArgumentException, IOException {
		Configuration conf = new Configuration();
		Server server = new RPC.Builder(conf).setProtocol(Bizable.class).setInstance(new RpcServer()).setBindAddress("192.168.8.100").setPort(9527).build();
		server.start();
	}
}
