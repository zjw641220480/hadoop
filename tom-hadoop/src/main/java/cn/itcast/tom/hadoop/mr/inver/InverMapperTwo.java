package cn.itcast.tom.hadoop.mr.inver;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class InverMapperTwo extends Mapper<LongWritable, Text, Text, Text> {
	Text keyOut = new Text();
	Text valueOut = new Text();
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] words = line.split("\t");
		keyOut.set(words[0]);
		valueOut.set(words[1]+"\t"+words[2]);
		context.write(keyOut,valueOut);
	}
}
