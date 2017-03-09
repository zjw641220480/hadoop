package cn.itcast.tom.hadoop.simple.invert;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * <p>Title:Invert.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月6日上午10:25:55
 */
public class Invert {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		//构建Job对象
		Job job = Job.getInstance(new Configuration());
		job.setMapperClass(InvertMapperOne.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setReducerClass(InvertReducerOne.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setCombinerClass(InvertCombinerOne.class);
		
		job.setJarByClass(Invert.class);
		FileInputFormat.setInputPaths(job, new Path("/invert"));
		FileOutputFormat.setOutputPath(job, new Path("/invertoutone"));
		
		job.waitForCompletion(true);
	}
}
