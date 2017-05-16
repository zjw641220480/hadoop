package cn.itcast.tom.hadoop.hdfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * <p>Titile:HdfsClientDemo</p>
 * <p>Description: JAVAApi操作HDFS</p>
 * @author TOM
 * @date 2017年5月15日 上午8:29:59
 */
public class HdfsClientDemo {
	FileSystem fileSystem = null;
	Configuration configuration = null;
	@Before
	public void init() throws IOException, URISyntaxException, InterruptedException{
		//
		/**
		 * 客户端在操作HDFS的时候,是由一个身份的,默认情况下hdfs客户端会从jvm中获取指定参数作为自己的用户身份:-DHADOOP_USER_NAME=hadoop
		 * 
		 */
		configuration = new Configuration();
		//使用这种方式是最简单的方式,用哪个用户启动hadoop,那么用哪个用户进行相关操作
		fileSystem = FileSystem.get(new URI("hdfs://mini01:9000"), configuration,"hadoop");
	}
	/**
	 * 
	 * @MethodName:testMkdir
	 * @Description:创建目录,允许级联创建
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @Time: 2017年5月15日 下午7:21:26
	 * @author: TOM
	 */
	@Test
	public void testMkdir() throws IllegalArgumentException, IOException{
		boolean flag = fileSystem.mkdirs(new Path("/work/tmp"));
		System.out.println(flag);
	}
	/**
	 * 
	 * @MethodName:testUpload
	 * @Description:上传文件,当文件已经存在的时候,则会把之前的文件覆盖掉
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @Time: 2017年5月15日 下午3:50:14
	 * @author: TOM
	 */
	@Test
	public void testUpload() throws IllegalArgumentException, IOException{
		//从本地上传到hdfs
		//两个参数分别为本地文件全路径和hdfs路径
		//只要没有后罪名,最后一部分就是上传后文件的名称
		//当文件已经存在的时候,则会将其覆盖掉
		fileSystem.copyFromLocalFile(new Path("D:\\workware\\apache-tomcat-7.0.72-windows-x64.zip"),new Path("/tomcat/tmp/"));
		fileSystem.close();
	}
	/**
	 * 
	 * @MethodName:testLs
	 * @Description:递归列出指定文件夹下的所有文件(文件夹不会被列出),并获取文件相关信息
	 * @Time: 2017年5月15日 下午7:38:54
	 * @author: TOM
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testLs() throws FileNotFoundException, IllegalArgumentException, IOException{
		//boolean参数的意义在于是否在此目录下进行迭代查找文件;
		//这里不使用List,是因为当查找出来的文件量很大的时候,使用List会对客户端照成较大压力
		//而使用迭代器,其不存储数据,只有取数据的逻辑,每次取的时候去服务端校验,不会对客户端照成较大压力;
		RemoteIterator<LocatedFileStatus> remoteIterator =fileSystem.listFiles(new Path("/"), false);
		while(remoteIterator.hasNext()){
			LocatedFileStatus fileStatus = remoteIterator.next();
			System.out.print("Name:\t"+fileStatus.getPath().getName());
			System.out.print("\tBlockSize\t"+fileStatus.getBlockSize());
			System.out.print("\tisdirectory\t"+fileStatus.isDirectory());
			System.out.print("\treplication\t"+fileStatus.getReplication());
			System.out.println();
		}
	}
	/**
	 * 
	 * @MethodName:testLs2
	 * @Description:获取指定目录下的所有文件夹以及文件,不会进行递归查找
	 * @Time: 2017年5月15日 下午7:59:14
	 * @author: TOM
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testLs2() throws FileNotFoundException, IllegalArgumentException, IOException{
		FileStatus[] fileStatus = fileSystem.listStatus(new Path("/"));
		for(FileStatus f:fileStatus){
			System.out.println(f.getPath().getName()+"\t"+f.isDirectory());
		}
	}
	/**
	 * 
	 * @MethodName:testDelete
	 * @Description:删除的方法,即可以删除文件也可以删除文件夹
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @Time: 2017年5月15日 下午7:23:31
	 * @author: TOM
	 */
	@Test
	public void testDelete() throws IllegalArgumentException, IOException{
		//boolean参数意义为,文件夹不为空的时候是否允许删除
		boolean flag = fileSystem.delete(new Path("/stream"),true);
		System.out.println(flag);
	}
	/**
	 * 
	 * @MethodName:testBlockSize
	 * @Description:打印块的相关信息
	 * @Time: 2017年5月16日 下午3:31:45
	 * @author: TOM
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 * @throws FileNotFoundException 
	 */
	@Test
	public void testBlockSize() throws FileNotFoundException, IllegalArgumentException, IOException{
		RemoteIterator<LocatedFileStatus> remoteIterator =fileSystem.listFiles(new Path("/"), false);
		while(remoteIterator.hasNext()){
			LocatedFileStatus fileStatus = remoteIterator.next();
			BlockLocation[] blockLocations = fileStatus.getBlockLocations();
			for(BlockLocation b:blockLocations){
				System.out.print(b.getLength()+"\t");//打印块实际大小
				System.out.print(b.getOffset()+"\t");//打印块起始偏移量
				for(String sname:b.getNames()){//打印块分布的主机IP和端口
					System.out.print(sname+"\t");
				}
				for(String shost:b.getHosts()){//块分布的主机名
					System.out.print(shost+"\t");
				}
				System.out.println();
			}
		}
	}
	@Test
	public void testConf(){
		Iterator<Entry<String, String>> iterable = configuration.iterator();
		while(iterable.hasNext()){
			Entry<String,String> entry = iterable.next();
			System.out.println(entry.getKey()+"\t"+entry.getValue());
		}
	}
}
