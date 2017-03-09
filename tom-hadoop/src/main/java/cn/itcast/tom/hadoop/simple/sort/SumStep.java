package cn.itcast.tom.hadoop.simple.sort;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import cn.itcast.tom.hadoop.simple.sort.sum.SumMapper;
import cn.itcast.tom.hadoop.simple.sort.sum.SumReducer;



/**
 *
 * <p>Title:SumStep.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月3日上午10:41:49
 */
public class SumStep {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		//构建Job对象
		Job job = Job.getInstance(new Configuration());
		job.setMapperClass(SumMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(SumOutput.class);
		job.setJarByClass(SumStep.class);

		FileInputFormat.setInputPaths(job, new Path("/trade_info.txt"));

		job.setReducerClass(SumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(SumOutput.class);

		FileOutputFormat.setOutputPath(job, new Path("/outsum"));

		//提交任务
		job.waitForCompletion(true);
	}
}
