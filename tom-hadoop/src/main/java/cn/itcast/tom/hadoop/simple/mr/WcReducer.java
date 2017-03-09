package cn.itcast.tom.hadoop.simple.mr;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * <p>Title:WcReducer.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月1日下午6:09:49
 */
public class WcReducer extends Reducer<Text, LongWritable, Text, LongWritable>{

	@Override
	protected void reduce(Text key, Iterable<LongWritable> v2s,
			Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
		//接收数据
		//定义一个计数器
		long count = 0;
		//循环v2s
		for(LongWritable longWritable : v2s){
			count += longWritable.get();
		}
		//输出
		context.write(key, new LongWritable(count));
	}
}
