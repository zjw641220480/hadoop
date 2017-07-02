package cn.itcast.tom.hadoop.hdfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * <p>
 * Titile:HDFSStreamAccess
 * </p>
 * <p>
 * Description:用流的方式操作HDFS上的文件,可以实现读取指定偏移量范围的数据
 * </p>
 * 
 * @author TOM
 * @date 2017年5月15日 下午8:14:46
 */
public class HDFSStreamAccess {
	FileSystem fileSystem = null;
	Configuration configuration = null;

	@Before
	public void init() throws IOException, URISyntaxException, InterruptedException {
		//
		/**
		 * 客户端在操作HDFS的时候,是由一个身份的,默认情况下hdfs客户端会从jvm中获取指定参数作为自己的用户身份:-DHADOOP_USER_NAME=hadoop
		 * 
		 */
		configuration = new Configuration();
		// 使用这种方式是最简单的方式,用哪个用户启动hadoop,那么用哪个用户进行相关操作
		fileSystem = FileSystem.get(new URI("hdfs://mini01:9000"), configuration, "hadoop");
	}

	/**
	 * 
	 * @MethodName:testWrite_Upload
	 * @Description:使用流的方式上传文件
	 * @Time: 2017年5月15日 下午8:16:13
	 * @author: TOM
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@Test
	public void testWrite_Upload() throws IllegalArgumentException, IOException {
		FSDataOutputStream dataOutputStream = fileSystem.create(new Path("/stream/HTTP_20130313143750.txt"));
		FileInputStream fileInputStream = new FileInputStream(new File("src\\main\\java\\HTTP_20130313143750.txt"));
		IOUtils.copyBytes(fileInputStream, dataOutputStream, configuration);
	}

	/**
	 * 
	 * @MethodName:testRead_Download
	 * @Description:使用流的方式下载
	 * @Time: 2017年5月15日 下午8:28:06
	 * @author: TOM
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 */
	@Test
	public void testRead_Download() throws IllegalArgumentException, IOException, InterruptedException {
		FSDataInputStream dataInputStream = fileSystem.open(new Path("/stream/HTTP_20130313143750.txt"));
		String read = "";
		while ((read = dataInputStream.readLine()) != null) {
			Thread.sleep(100);
			System.out.println(read);
		}
	}

	/**
	 * 
	 * @MethodName:testRandomAccess
	 * @Description:读取时候,偏移量的设置
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws InterruptedException
	 * @Time: 2017年5月16日 下午3:28:17
	 * @author: TOM
	 */
	@Test
	public void testRandomAccess() throws IllegalArgumentException, IOException, InterruptedException {
		FSDataInputStream dataInputStream = fileSystem.open(new Path("/stream/HTTP_20130313143750.txt"));
		// 偏移30个字节量
		dataInputStream.seek(30);
		String read = "";
		while ((read = dataInputStream.readLine()) != null) {
			Thread.sleep(100);
			System.out.println(read);
		}
	}

	/**
	 * 
	 * @MethodName:testRandomAccessOut
	 * @Description:输出直接打印到屏幕
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @Time: 2017年5月16日 下午3:27:59
	 * @author: TOM
	 */
	@Test
	public void testRandomAccessOut() throws IllegalArgumentException, IOException {
		FSDataInputStream dataInputStream = fileSystem.open(new Path("/stream/HTTP_20130313143750.txt"));
		// 偏移30个字节量
		dataInputStream.seek(30);
		IOUtils.copyBytes(dataInputStream, System.out, 1024);
	}

	/**
	 * 
	 * @MethodName:testBlock
	 * @Description:获取一个文件的所有block位置信息，然后读取指定block中的内容
	 * @Time: 2017年6月16日 下午4:14:11
	 * @author: TOM
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void testBlock() throws IllegalArgumentException, IOException {
		FSDataInputStream in = fileSystem.open(new Path("/stream/HTTP_20130313143750.txt"));
		// 拿到文件信息
		FileStatus[] listStatus = fileSystem.listStatus(new Path("/stream/HTTP_20130313143750.txt"));
		// 获取这个文件的所有block的信息
		BlockLocation[] fileBlockLocations = fileSystem.getFileBlockLocations(listStatus[0], 0L,
				listStatus[0].getLen());
		// 第一个block的长度
		long length = fileBlockLocations[0].getLength();
		// 第一个block的起始偏移量
		long offset = fileBlockLocations[0].getOffset();
		System.out.println(length);
		System.out.println(offset);
		
		//获取第一个block写入输出流
//		IOUtils.copyBytes(in, System.out, (int)length);
		byte[] b = new byte[4096];
		
		FileOutputStream os = new FileOutputStream(new File("/block0"));
		while(in.read(offset, b, 0, 4096)!=-1){
			os.write(b);
			offset += 4096;
			if(offset>=length) return;
		};
		os.flush();
		os.close();
		in.close();

	}
}
