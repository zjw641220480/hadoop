package cn.itcast.tom.hadoop.mr.httpsort;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
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
public class HttpSortMain {
	public static void main(String[] args)
			throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {

		Job job = Job.getInstance(new Configuration());

		// 指定本程序的jar包所在的本地路径
		job.setJarByClass(HttpSortMain.class);

		// 指定本业务job要使用的mapper业务类
		job.setMapperClass(HttpSortMapper.class);
		// 指定本业务job要使用的reducer业务类
		job.setReducerClass(HttpSortReducer.class);

		// 指定mapper的输出类型
		job.setMapOutputKeyClass(FlowSortBean.class);
		job.setMapOutputValueClass(NullWritable.class);

		// 指定reducer的输出类型
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowSortBean.class);

		// 指定job的输入原始文件所在目录
		FileInputFormat.setInputPaths(job, new Path("/http/httpout/part-r-00000"));
		// 指定输出结果所在目录
		FileOutputFormat.setOutputPath(job, new Path("/http/httpoutsort"));


		// 提交,将job中配置的相关参数,以及job所用的java类所在的jar包,提交给yarn去运行;
		boolean flag = job.waitForCompletion(true);
		if (!flag) {
			FileSystem fileSystem = FileSystem.get(new URI("hdfs://mini01:9000"), new Configuration(), "hadoop");
			fileSystem.delete(new Path("/http/httpoutsort"), true);
		}
	}
}
