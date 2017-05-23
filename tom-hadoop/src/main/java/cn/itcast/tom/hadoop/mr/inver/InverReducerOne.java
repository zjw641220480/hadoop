package cn.itcast.tom.hadoop.mr.inver;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 
 * <p>Titile:InverReducerOne</p>
 * <p>Description: </p>
 * @author TOM
 * @date 2017年5月22日 下午6:52:07
 */
public class InverReducerOne extends Reducer<Text, IntWritable, Text, IntWritable>{
	Text keyOut = new Text();
	IntWritable valueOut = new IntWritable();
	/**
	 * 这个运算主要就是求和,归并成一个文本,那么其就可以作为一个Combiner,而不需要单独称为一个Reducer
	 */
	@Override
	protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		int count = 0;
		for(IntWritable value:values){
			count += value.get();
		}
		valueOut.set(count);
		context.write(key, valueOut);
	}
}
