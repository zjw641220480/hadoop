package cn.itcast.tom.hadoop.mr.http;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class HttpMapper extends Mapper<LongWritable, Text, Text, FlowBean>{

	FlowBean bean = new FlowBean();
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, FlowBean>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] elements = line.split("\t");
		int length = elements.length;
		String phone = elements[1];
		bean.setUpFlow(new Integer(elements[length-2].trim()));
		bean.setDownFlow(new Integer(elements[length-3].trim()));
		//这里对象不会出现所有的值都是最后一次设置的,没写一次便序列化一次;
		context.write(new Text(phone), bean);
	}
	
}
