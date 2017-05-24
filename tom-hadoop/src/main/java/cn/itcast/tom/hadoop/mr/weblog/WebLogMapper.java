package cn.itcast.tom.hadoop.mr.weblog;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WebLogMapper extends Mapper<LongWritable,Text,Text,NullWritable>{
	Text keyOut = new Text();
	NullWritable valueOut = NullWritable.get();
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, Text, NullWritable>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		WebLogBean bean = WebLogParser.parser(line);
		//可以插入一个静态资源(js,css,png......)
		keyOut.set(bean.toString());
		context.write(keyOut, valueOut);
	}
}
