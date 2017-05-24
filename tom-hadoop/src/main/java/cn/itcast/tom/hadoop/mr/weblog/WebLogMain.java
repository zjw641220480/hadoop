package cn.itcast.tom.hadoop.mr.weblog;

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


public class WebLogMain {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
		Job job = Job.getInstance(new Configuration());
		// 指定本程序的jar包所在的本地路径
		job.setJarByClass(WebLogMain.class);

		// 指定本业务job要使用的mapper业务类
		job.setMapperClass(WebLogMapper.class);

		// 指定mapper的输出类型
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);

		// 指定job的输入原始文件所在目录
		FileInputFormat.setInputPaths(job, new Path("/log/in/web.log"));
		// 指定输出结果所在目录
		FileOutputFormat.setOutputPath(job, new Path("/log/out"));

		// 提交,将job中配置的相关参数,以及job所用的java类所在的jar包,提交给yarn去运行;
		boolean flag = job.waitForCompletion(true);
		if (!flag) {
			FileSystem fileSystem = FileSystem.get(new URI("hdfs://mini01:9000"), new Configuration(), "hadoop");
			fileSystem.delete(new Path("/log/out"), true);
		}
	}
}
