package cn.itcast.tom.hadoop.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.ipc.Delayable;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * <p>Title:HbaseDemo.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月13日下午9:15:24
 */
public class HbaseDemo {
	
	/**
	 * 
	 * @MethodName:main
	 * @Description:Hbase创建表
	 * @param args
	 * @Time:2017年3月13日下午9:15:42
	 * @author:Tom
	 * @throws IOException 
	 * @throws ZooKeeperConnectionException 
	 * @throws MasterNotRunningException 
	 */
	public static void main(String[] args) throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		Configuration configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.quorum", "centos04:2181,centos05:2181,centos06:2181");
		HBaseAdmin admin = new HBaseAdmin(configuration);
		HTableDescriptor hTableDescriptor = new HTableDescriptor(TableName.valueOf("peoples"));
		HColumnDescriptor columnDescriptor_info = new HColumnDescriptor("info");
		HColumnDescriptor columnDescriptor_data = new HColumnDescriptor("data");
		columnDescriptor_data.setMaxVersions(3);
		hTableDescriptor.addFamily(columnDescriptor_info);
		hTableDescriptor.addFamily(columnDescriptor_data);
		admin.createTable(hTableDescriptor);
		admin.close();
		System.out.println("HbaseDemo.main()");
	}
	private Configuration configuration = null;
	/**
	 * 
	 * @MethodName:init
	 * @Description:初始化配置文件
	 * @Time:2017年3月14日下午8:35:05
	 * @author:Tom
	 */
	@Before
	public void init(){
		configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.quorum", "centos04:2181,centos05:2181,centos06:2181");
	}
	/**
	 * 
	 * @MethodName:testPut
	 * @Description:
	 * @Time:2017年3月14日下午8:27:00
	 * @author:Tom
	 * @throws IOException 
	 */
	@Test
	public void testPut() throws IOException{
		HTable hTable = new HTable(configuration, "peoples");
		Put put = new Put(Bytes.toBytes("kr0001"));
		put.add(Bytes.toBytes("info"), Bytes.toBytes("name"),Bytes.toBytes("张三丰"));
		put.add(Bytes.toBytes("info"), Bytes.toBytes("age"),Bytes.toBytes(30));
		hTable.put(put);
		hTable.close();
	}
	/**
	 * 
	 * @MethodName:testPutAll
	 * @Description:批量插入
	 * @Time:2017年3月14日下午8:39:50
	 * @author:Tom
	 */
	public void testPutAll(){
		
	}
	/**
	 * 
	 * @MethodName:get
	 * @Description:获取数据
	 * @Time:2017年3月14日下午8:51:55
	 * @author:Tom
	 * @throws IOException 
	 */
	@Test
	public void get() throws IOException{
		HTable hTable = new HTable(configuration, "peoples");
		Get get = new Get(Bytes.toBytes("kr0001"));
		Result result = hTable.get(get);
		byte[] bytes = result.getValue(Bytes.toBytes("info"), Bytes.toBytes("name"));
		String name = Bytes.toString(bytes);
		System.out.println(result);
		System.out.println(name);
		hTable.close();
	}
	/**
	 * 
	 * @MethodName:getScan
	 * @Description:批量查询
	 * @Time:2017年3月14日下午8:57:29
	 * @author:Tom
	 * @throws IOException 
	 */
	@Test
	public void getScan() throws IOException{
		HTable hTable = new HTable(configuration, "peoples");
		Scan scan = new Scan(Bytes.toBytes("kr0001"),Bytes.toBytes("kr0005"));
		ResultScanner resultScanner = hTable.getScanner(scan);
		for(Result result : resultScanner){
			System.out.println(result);
		}
		hTable.close();
	}
	/**
	 * 
	 * @MethodName:delete
	 * @Description:删除
	 * @Time:2017年3月14日下午9:03:04
	 * @author:Tom
	 * @throws IOException 
	 */
	@Test
	public void delete() throws IOException{
		HTable hTable = new HTable(configuration, "peoples");
		Delete delete = new Delete(Bytes.toBytes("kr0001"));
		hTable.delete(delete);
		hTable.close();
	}
}
