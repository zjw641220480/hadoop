package cn.itcast.tom.hadoop.mr;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * <p>Title:WordCount.java</p>
 * <p>Description:第一个mapreduce</p>
 * @author TOM
 * @date 2017年3月1日下午5:34:24
 */
public class WordCount {
	/**
	 * 
	 * @MethodName:main
	 * @Description:第一个案例,
	 * 		1:分析具有的业务逻辑,确定输入和输出数据的样式,
	 * 		2:自定义一个类,此类继承Mapper,重写map方法,实现和map具体相关的逻辑,将新的KV输出;
	 * 		3:自定义一个类,此类继承Reducer,重写reduce,实现和reduce具体相关的逻辑,将最终结果输出;
	 * 		4:将自定义的Mapper和Reduce,使用Job对象进行组装,并执行;
	 * @param args
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InterruptedException
	 * @Time:2017年3月1日下午6:59:43
	 * @author:Tom
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		//构建Job对象
		Job job = Job.getInstance(new Configuration());

		//设置map相关属性
		job.setMapperClass(WcMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(LongWritable.class);

		//一定要将main方法所在的类注入job中
		job.setJarByClass(WordCount.class);
		FileInputFormat.setInputPaths(job, new Path("/in.log"));
		//设置Reducer相关属性
		job.setReducerClass(WcReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		FileOutputFormat.setOutputPath(job, new Path("/out"));

		//提交任务
		job.waitForCompletion(true);
	}
}
