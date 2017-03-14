package cn.itcast.tom.hadoop.cluster;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

/**
 *
 * <p>Title:HDFSDemo_HA.java</p>
 * <p>Description:集群第一个案例</p>
 * @author TOM
 * @date 2017年3月9日下午9:32:05
 */
public class HDFSDemo_HA {
	/**
	 * 
	 * @MethodName:main
	 * @Description:集群首次下载案例
	 * @param args
	 * @throws IOException
	 * @throws URISyntaxException
	 * @Time:2017年3月9日下午9:50:01
	 * @author:Tom
	 */
	public static void main(String[] args) throws IOException, URISyntaxException {
		Configuration conf = new Configuration();
		conf.set("dfs.nameservices", "ns1");
		conf.set("dfs.ha.namenodes.ns1", "nn1,nn2");
		conf.set("dfs.namenode.rpc-address.ns1.nn1", "centos01:9000");
		conf.set("dfs.namenode.rpc-address.ns1.nn2", "centos01:9000");
		conf.set("dfs.client.failover.proxy.provider.ns1",
				"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
		FileSystem fileSystem = FileSystem.get(new URI("hdfs://ns1"), conf);
		InputStream inputStream = fileSystem.open(new Path("/slave"));
		OutputStream outputStream = new FileOutputStream(new File("slaves.sh"));
		IOUtils.copyBytes(inputStream, outputStream, 4096, true);
	}
	/**
	 * 
	 * @MethodName:testUpload
	 * @Description:集群上传
	 * @Time:2017年3月10日下午8:08:45
	 * @author:Tom
	 * @throws URISyntaxException 
	 * @throws IOException 
	 */
	@Test
	public void testUpload() throws IOException, URISyntaxException{
		Configuration conf = new Configuration();
		conf.set("dfs.nameservices", "ns1");
		conf.set("dfs.ha.namenodes.ns1", "nn1,nn2");
		conf.set("dfs.namenode.rpc-address.ns1.nn1", "centos01:9000");
		conf.set("dfs.namenode.rpc-address.ns1.nn2", "centos01:9000");
		conf.set("dfs.client.failover.proxy.provider.ns1",
				"org.apache.hadoop.hdfs.server.namenode.ha.ConfiguredFailoverProxyProvider");
		FileSystem fileSystem = FileSystem.get(new URI("hdfs://ns1"), conf);
		InputStream inputStream = new FileInputStream(new File("in.log"));
		OutputStream outputStream = fileSystem.create(new Path("/in.log"));
		IOUtils.copyBytes(inputStream, outputStream, 4092, true);
	}
}
