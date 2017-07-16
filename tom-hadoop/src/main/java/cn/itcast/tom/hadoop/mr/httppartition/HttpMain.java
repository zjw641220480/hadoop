package cn.itcast.tom.hadoop.mr.httppartition;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 
 * <p>
 * Titile:HttpMain
 * </p>
 * <p>
 * Description: 计算每一个手机号上下行的流量综合
 * </p>
 * 
 * @author TOM
 * @date 2017年5月17日 下午6:41:14
 */
public class HttpMain {
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {

		Configuration configuration = new Configuration();
		//配置程序在本地windows环境运行,注意替换hadoop中bin和lib目录;默认就是在本地运行
		configuration.set("mapreduce.framework.name", "local");
		//这里也可以指定为hdfs的文件目录:hdfs://mini01:9000/http
		//本地模式运行mr程序的时候,输入输出的数据可以在本地也可以在HDFS上,本地的时候可以不用配置,
		configuration.set("fs.defaultFS", "file:///");
		
		/**
		 * 运行集群模式,就是把程序提交到yarn中去运行;
		 * 要想运行为集群模式,以下三个参数要指定集群上的值;
		 */
//		configuration.set("mapreduce.framework.name", "yarn");
//		configuration.set("yarn.resourcemanager.hostname", "mini01");
//		configuration.set("fs.defaultFS", "hdfs://mini01:9000");
		
		Job job = Job.getInstance(configuration);

		// 指定本程序的jar包所在的本地路径
		job.setJarByClass(HttpMain.class);

		// 指定本业务job要使用的mapper业务类
		job.setMapperClass(HttpMapper.class);
		// 指定本业务job要使用的reducer业务类
		job.setReducerClass(HttpReducer.class);

		// 指定mapper的输出类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);

		// 指定reducer的输出类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);
		
		/**
		 * 只要不大于设置的最大切片,就需要进行合并,合并的小文件超出设置的最大切片大小的时候会另外重新起一个切片,合并剩下的文本;
		 * 大小在两者之间也会继续被合并;(大的会被切开)
		 */
		//如果不设置InputFormat,它默认使用的是TextInputFormat.class
		/*job.setInputFormatClass(CombineTextInputFormat.class);
		//最大的切片大小时4M
		CombineTextInputFormat.setMaxInputSplitSize(job, 4194304);
		//最小的切片大小为2M
		CombineTextInputFormat.setMinInputSplitSize(job, 2097152);*/
		
		// 指定job的输入原始文件所在目录
		FileInputFormat.setInputPaths(job, new Path("/http"));
		// 指定输出结果所在目录
		FileOutputFormat.setOutputPath(job, new Path("/http/httpout"));
		
		//设置Reduce的输出压缩
		FileOutputFormat.setCompressOutput(job, true);
		FileOutputFormat.setOutputCompressorClass(job, BZip2Codec.class);
		
		//设置map端压缩
		configuration.setBoolean(Job.MAP_OUTPUT_COMPRESS, true);
		configuration.setClass(Job.MAP_OUTPUT_COMPRESS_CODEC, GzipCodec.class, CompressionCodec.class);
		
		// 指定我们自定义的数据分区器
		job.setPartitionerClass(HttpPartitioner.class);
		//指定相应数量的reducerTask
		job.setNumReduceTasks(4);

		// 提交,将job中配置的相关参数,以及job所用的java类所在的jar包,提交给yarn去运行;
		boolean flag = job.waitForCompletion(true);
		if (!flag) {
			FileSystem fileSystem = FileSystem.get(new URI("hdfs://mini01:9000"), new Configuration(), "hadoop");
			fileSystem.delete(new Path("/http/httpout"), true);
		}
	}
}
