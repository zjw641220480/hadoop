package cn.itcast.tom.hadoop.mr.item;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ItemMain {
	public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {
		Configuration configuration = new Configuration();
		Job job = Job.getInstance(configuration);
		job.setJarByClass(ItemMain.class);
		
		job.setMapperClass(SecondarySortMapper.class);
		job.setMapOutputKeyClass(OrderBean.class);
		job.setMapOutputValueClass(NullWritable.class);
		
		job.setReducerClass(SecondarySortReducer.class);
		job.setOutputKeyClass(OrderBean.class);
		job.setOutputValueClass(NullWritable.class);
		
		//在此设置GroupingComparator类(作用是key虽然不同,但可以指定规则为相同)
		job.setGroupingComparatorClass(ItemIdGroupComparator.class);
		
		//在此设置自定义的partitioner
		job.setPartitionerClass(ItemIdPartitioner.class);
		
		job.setNumReduceTasks(1);
		
		FileInputFormat.setInputPaths(job, new Path("/item/in/order.txt"));
		FileOutputFormat.setOutputPath(job, new Path("/item/out"));
		
		job.waitForCompletion(true);
	}
	static class SecondarySortMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable>{
		OrderBean bean = new OrderBean();
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, OrderBean, NullWritable>.Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			String[] fields = StringUtils.split(line,",");
			bean.setItemId(fields[0]);
			bean.setAmount(Double.parseDouble(fields[2]));
			context.write(bean, NullWritable.get());
		}
	}
	static class SecondarySortReducer extends Reducer<OrderBean,NullWritable,OrderBean,NullWritable>{
		//这里对Reducer的输出,着重关注,为何不再遍历,需要仔细思考
		//到达Reducer的时候,相同id的所有bean已经被看成一组,且金额最大的那个已经排在第一个;
		@Override
		protected void reduce(OrderBean key, Iterable<NullWritable> values,
				Reducer<OrderBean, NullWritable, OrderBean, NullWritable>.Context context)
				throws IOException, InterruptedException {
			context.write(key, NullWritable.get());
		}
	}
	
}
