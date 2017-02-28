package cn.itcast.tom.hadoop;

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
import org.junit.Before;
import org.junit.Test;

/**
 *
 * <p>Title:HDFSDemo.java</p>
 * <p>Description:Hadoop第一个案例,使用FileSystem工具类来操作Hadoop</p>
 * @author TOM
 * @date 2017年2月27日下午6:09:26
 */
public class HDFSDemo {
	private FileSystem fileSystem;

	@Before
	public void prepareHdfs() throws IOException, URISyntaxException, InterruptedException {
		//创建FileSystem的实现类(工具类)
		fileSystem = FileSystem.get(new URI("hdfs://192.168.8.88:9000"), new Configuration(),"root");
	}
	/**
	 * 
	 * @MethodName:testUpload
	 * @Description:上传
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @Time:2017年2月27日下午6:45:54
	 * @author:Tom
	 */
	@Test
	public void testUpload() throws IllegalArgumentException, IOException {
		//读取本地文件系统的文件,返回输入流
		InputStream inputStream = new FileInputStream(new File("\\mywork\\src.zip"));
		//在HDFS上创建一个文件,返回输出流,
		OutputStream outputStream = fileSystem.create(new Path("/src.zip"));
		//输入-->输出
		IOUtils.copyBytes(inputStream, outputStream, 4096, true);
	}
	/**
	 * 
	 * @MethodName:testDownloadTwo
	 * @Description:更简单方式的上传
	 * @Time:2017年2月27日下午6:48:01
	 * @author:Tom
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testDownloadTwo() throws IllegalArgumentException, IOException{
		Path src = new Path("/in.log");
		Path dst = new Path("in.log");
		fileSystem.copyToLocalFile(false,src,dst,true);
	}
	/**
	 * 
	 * @MethodName:testDownload
	 * @Description:下载
	 * @throws IOException
	 * @throws URISyntaxException
	 * @Time:2017年2月27日下午6:46:01
	 * @author:Tom
	 */
	@Test
	public void testDownload() throws IOException, URISyntaxException {
		InputStream in = fileSystem.open(new Path("/in.log"));
		OutputStream outputStream = new FileOutputStream(new File("in.log"));
		IOUtils.copyBytes(in, outputStream, 4096, true);
	}
	/**
	 * 
	 * @MethodName:testDelete
	 * @Description:HDFS上的删除操作
	 * @Time:2017年2月28日上午10:07:25
	 * @author:Tom
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testDelete() throws IllegalArgumentException, IOException{
		boolean flag = fileSystem.delete(new Path("/src.zip"), true);
		System.out.println(flag);
	}
	/**
	 * 
	 * @MethodName:testCreateDir
	 * @Description:
	 * @Time:2017年2月28日上午10:10:09
	 * @author:Tom
	 * @throws IOException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testCreateDir() throws IllegalArgumentException, IOException{
		boolean flag = fileSystem.mkdirs(new Path("/test/dir"));
		System.out.println(flag);
	}
	/**
	 * 
	 * @MethodName:testDeleteDir
	 * @Description:删除一个文件夹
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @Time:2017年2月28日上午10:20:17
	 * @author:Tom
	 */
	@Test
	public void testDeleteDir() throws IllegalArgumentException, IOException{
		fileSystem.delete(new Path("/test"),true);
	}
}
