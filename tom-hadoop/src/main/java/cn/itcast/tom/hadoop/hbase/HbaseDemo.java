package cn.itcast.tom.hadoop.hbase;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
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
import org.apache.hadoop.hbase.filter.ByteArrayComparable;
import org.apache.hadoop.hbase.filter.CompareFilter.CompareOp;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.FilterList.Operator;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * <p>
 * Title:HbaseDemo.java
 * </p>
 * <p>
 * Description:
 * </p>
 * 
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
		configuration.set("hbase.zookeeper.quorum", "mini01:2181,mini02:2181,mini03:2181");
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
	 * @MethodName:testCreateTable
	 * @Description:可以演示过滤器相关操作
	 * @throws MasterNotRunningException
	 * @throws ZooKeeperConnectionException
	 * @throws IOException
	 * @Time: 2017年6月1日 下午7:11:40
	 * @author: TOM
	 */
	@Test
	public void testCreateTable() throws MasterNotRunningException, ZooKeeperConnectionException, IOException {
		// 创建表管理类
		HBaseAdmin admin = new HBaseAdmin(configuration); // hbase表管理
		// 创建表描述类
		TableName tableName = TableName.valueOf("test3"); // 表名称
		HTableDescriptor desc = new HTableDescriptor(tableName);
		// 创建列族的描述类
		HColumnDescriptor family = new HColumnDescriptor("info"); // 列族
		// 将列族添加到表中
		desc.addFamily(family);
		HColumnDescriptor family2 = new HColumnDescriptor("info2"); // 列族
		// 将列族添加到表中
		desc.addFamily(family2);
		// 创建表
		admin.createTable(desc); // 创建表
	}

	/**
	 * 
	 * @MethodName:init
	 * @Description:初始化配置文件
	 * @Time:2017年3月14日下午8:35:05
	 * @author:Tom
	 */
	@Before
	public void init() {
		configuration = HBaseConfiguration.create();
		configuration.set("hbase.zookeeper.quorum", "mini01:2181,mini02:2181,mini03:2181");// zookeeper的地址和端口;

	}
	
	/**
	 * 
	 * @MethodName:testPut
	 * @Description:向Habse的相关表中批量提交数据
	 * @Time:2017年3月14日下午8:27:00
	 * @author:Tom
	 * @throws IOException
	 */
	@Test
	public void testPut() throws IOException {
		HTable hTable = new HTable(configuration, "test3");
		ArrayList<Put> arrayList = new ArrayList<Put>();
		for (int i = 21; i < 50; i++) {
			Put put = new Put(Bytes.toBytes("1234"+i));
			put.add(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("wangwu"+i));
			put.add(Bytes.toBytes("info"), Bytes.toBytes("password"), Bytes.toBytes(1234+i));
			arrayList.add(put);
		}
		hTable.put(arrayList);
		/*Put put = new Put(Bytes.toBytes("kr0002"));
		put.add(Bytes.toBytes("info"), Bytes.toBytes("name"), Bytes.toBytes("zhangsan"));
		put.add(Bytes.toBytes("info"), Bytes.toBytes("age"), Bytes.toBytes(30));
		hTable.put(put);*/
		hTable.close();
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
	public void get() throws IOException {
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
	 * @MethodName:getBatch
	 * @Description:批量获取数据
	 * @Time: 2017年7月11日 下午2:14:26
	 * @author: TOM
	 * @throws IOException 
	 */
	public void getBatch() throws IOException{
		HTable hTable = new HTable(configuration, "peoples");
		Scan scan = new Scan();
		scan.setStartRow("a1".getBytes());
		scan.setStopRow("a20".getBytes());
		ResultScanner scanner = hTable.getScanner(scan);
		for (Result row : scanner) {
			System.out.println("\nRowkey: " + new String(row.getRow()));
			for (KeyValue kv : row.raw()) {
			     System.out.print(new String(kv.getRow()) + " ");
			     System.out.print(new String(kv.getFamily()) + ":");
			     System.out.print(new String(kv.getQualifier()) + " = ");
			     System.out.print(new String(kv.getValue()));
			     System.out.print(" timestamp = " + kv.getTimestamp() + "\n");
			}
		}
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
	public void getScan() throws IOException {
		HTable hTable = new HTable(configuration, "peoples");
		Scan scan = new Scan(Bytes.toBytes("kr0001"), Bytes.toBytes("kr0005"));
		ResultScanner resultScanner = hTable.getScanner(scan);
		for (Result result : resultScanner) {
			System.out.println(result);
		}
		hTable.close();
	}

	/**
	 * 
	 * @MethodName:delete
	 * @Description:删除给定rowKey的数据
	 * @Time:2017年3月14日下午9:03:04
	 * @author:Tom
	 * @throws IOException
	 */
	@Test
	public void delete() throws IOException {
		HTable hTable = new HTable(configuration, "peoples");
		Delete delete = new Delete(Bytes.toBytes("kr0001"));
		hTable.delete(delete);
		hTable.close();
	}

	/**
	 * 
	 * @MethodName:filter
	 * @Description:过滤器的使用,相似于数据库中的where
	 * @throws IOException
	 * @Time: 2017年6月1日 下午2:37:05
	 * @author: TOM
	 */
	@Test
	public void filter() throws IOException {
		HTable hTable = new HTable(configuration, "peoples");
		Scan scan = new Scan();
		// 创建过滤器(列值过滤器,六种方式)
		SingleColumnValueFilter fileter = new SingleColumnValueFilter(Bytes.toBytes("info"), Bytes.toBytes("name"),
				CompareOp.EQUAL, Bytes.toBytes("zhangsan"));
		//
		scan.setFilter(fileter);
		ResultScanner resultScanner = hTable.getScanner(scan);
		for (Result result : resultScanner) {
			System.out.println(result);
		}
	}

	/**
	 * 
	 * @MethodName:filter
	 * @Description:过滤出来rowkey中以12344开头的各个数据
	 * @Time: 2017年6月1日 下午7:07:03
	 * @author: TOM
	 * @throws IOException 
	 */
	@Test
	public void rowFilter() throws IOException {
		Filter f = new RowFilter(CompareOp.EQUAL, new RegexStringComparator("^12344"));// 匹配以1234开头的rowkey
		HTable hTable = new HTable(configuration, "test3");
		Scan scan = new Scan();
		scan.setFilter(f);
		ResultScanner resultScanner = hTable.getScanner(scan);
		for (Result result : resultScanner) {
			System.out.println(result);
		}
	}
	/**
	 * 过滤器集合
	 * @throws Exception
	 */
	@Test
	public void scanDataByFilter4() throws Exception {
		
		// 创建全表扫描的scan
		Scan scan = new Scan();
		//过滤器集合：MUST_PASS_ALL（and）,MUST_PASS_ONE(or)
		FilterList filterList = new FilterList(Operator.MUST_PASS_ONE);
		//匹配rowkey以wangsenfeng开头的
		RowFilter filter = new RowFilter(CompareOp.EQUAL, new RegexStringComparator("^wangsenfeng"));
		//匹配name的值等于wangsenfeng
		SingleColumnValueFilter filter2 = new SingleColumnValueFilter(Bytes.toBytes("info"),
				Bytes.toBytes("name"), CompareOp.EQUAL,
				Bytes.toBytes("zhangsan"));
		filterList.addFilter(filter);
		filterList.addFilter(filter2);
		// 设置过滤器
		scan.setFilter(filterList);
		HTable hTable = new HTable(configuration, "test3");
		// 打印结果集
		ResultScanner scanner = hTable.getScanner(scan);
		for (Result result : scanner) {
			System.out.println("rowkey：" + Bytes.toString(result.getRow()));
			System.out.println("info:name："
					+ Bytes.toString(result.getValue(Bytes.toBytes("info"),
							Bytes.toBytes("name"))));
			// 判断取出来的值是否为空
			if (result.getValue(Bytes.toBytes("info"), Bytes.toBytes("age")) != null) {
				System.out.println("info:age："
						+ Bytes.toInt(result.getValue(Bytes.toBytes("info"),
								Bytes.toBytes("age"))));
			}
			// 判断取出来的值是否为空
			if (result.getValue(Bytes.toBytes("info"), Bytes.toBytes("sex")) != null) {
				System.out.println("infi:sex："
						+ Bytes.toInt(result.getValue(Bytes.toBytes("info"),
								Bytes.toBytes("sex"))));
			}
			// 判断取出来的值是否为空
			if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("name")) != null) {
				System.out
				.println("info2:name："
						+ Bytes.toString(result.getValue(
								Bytes.toBytes("info2"),
								Bytes.toBytes("name"))));
			}
			// 判断取出来的值是否为空
			if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("age")) != null) {
				System.out.println("info2:age："
						+ Bytes.toInt(result.getValue(Bytes.toBytes("info2"),
								Bytes.toBytes("age"))));
			}
			// 判断取出来的值是否为空
			if (result.getValue(Bytes.toBytes("info2"), Bytes.toBytes("sex")) != null) {
				System.out.println("info2:sex："
						+ Bytes.toInt(result.getValue(Bytes.toBytes("info2"),
								Bytes.toBytes("sex"))));
			}
		}
		
	}
}