package cn.itcast.tom.hadoop.mr.httpsort;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class HttpSortMapper extends Mapper<LongWritable, Text, FlowSortBean, NullWritable>{

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, FlowSortBean, NullWritable>.Context context)
			throws IOException, InterruptedException {
		FlowSortBean bean = new FlowSortBean();
		String line = value.toString();
		String[] strs = line.split("\t");
		String phone = strs[0];
		int upFlow = new Integer(strs[1]);
		int downFlow =new Integer(strs[2]);
		bean.setUpFlow(upFlow);
		bean.setDownFlow(downFlow);
		bean.setSum(upFlow+downFlow);
		context.write(bean, NullWritable.get());
	}
	
}
