package cn.itcast.tom.hadoop.simple.http;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * <p>Title:HttpCountLoad.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月2日下午4:14:53
 */
public class HttpCountLoad {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		//构建Job对象
		Job job = Job.getInstance(new Configuration());
		job.setMapperClass(HttpMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(HttpOutput.class);
		job.setJarByClass(HttpCountLoad.class);
		
		FileInputFormat.setInputPaths(job, new Path("/HTTP_20130313143750.txt"));
		
		job.setReducerClass(HttpReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(HttpOutput.class);
		
		FileOutputFormat.setOutputPath(job, new Path("/outhttp"));
		
		job.setPartitionerClass(HttpProviderPartitioner.class);
		job.setNumReduceTasks(4);
		//提交任务
		job.waitForCompletion(true);
	}
}
