package cn.itcast.tom.hadoop.invert;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 *
 * <p>Title:InvertMapper.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月6日上午10:10:12
 */
public class InvertMapperOne extends Mapper<LongWritable, Text, Text, Text> {
	private Text text = new Text();
	private Text v2 = new Text();

	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		FileSplit fileSplit = (FileSplit) context.getInputSplit();
		String path = fileSplit.getPath().toString();
		
		String[] str = line.split(" ");
		for (String s : str) {
			text.set(s+"-->"+path);
			v2.set("1");
			context.write(text, v2);
		}
	}

}
