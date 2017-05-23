package cn.itcast.tom.hadoop.mr.inver;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

public class InverMapperOne extends Mapper<LongWritable, Text, Text, IntWritable>{
	//输出的为(文件名-->word,次数)
	//输出(<hello--a,1><hello--b>,2)
	Text keyOut = new Text();
	IntWritable valueOut = new IntWritable(1);
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] words = line.split(" ");
		FileSplit fileSplit = (FileSplit) context.getInputSplit();
		String fName = fileSplit.getPath().getName();
		for(String word:words){
			keyOut.set(word+"\t"+fName);
			context.write(keyOut, valueOut);
		}
	}
}
