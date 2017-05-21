package cn.itcast.tom.hadoop.mr.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WordCountReducer extends Reducer<Text, IntWritable, Text, IntWritable>{
	/**
	 * <hello,1>,<hello,1>,<hello,1>,<hello,1>,<hello,1>,<hello,1>
	 * 入参,是一组形同单词key对的key
	 * 程序默认有shuffle过程,不会有Combiner过程
	 */
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values,
			Context context) throws IOException, InterruptedException {
		int count = 0;
		for(IntWritable value:values ){
			count = count + new Integer(value.toString());
		}
		context.write(key, new IntWritable(count));
	}
	
}
