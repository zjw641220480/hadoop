package cn.itcast.tom.hadoop.mr.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 * 
 * <p>Titile:WordCountCombiner</p>
 * <p>Description: resource之前的combiner,不能影响之后的业务逻辑,比如说求平均值就会影响到最后的业务逻辑</p>
 * @author TOM
 * @date 2017年5月19日 下午5:09:45
 */
public class WordCountCombiner extends Reducer<Text, IntWritable, Text, IntWritable>{

	@Override
	protected void reduce(Text key, Iterable<IntWritable> value,
			Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
		int count = 0;
		for(IntWritable v:value){
			count += v.get();
		}
		context.write(key, new IntWritable(count));
	}
	
}
