package cn.itcast.tom.hadoop.simple.mr;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 *
 * <p>Title:WcMapper.java</p>
 * <p>Description:第一个Mapper程序,此过程中的输入为<Long,String>,输出为<String,Long></p>
 * @author TOM
 * @date 2017年3月1日下午5:35:51
 */
public class WcMapper extends Mapper<LongWritable , Text, Text, LongWritable>{
	/**
	 * 第一个map方法,
	 */
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, LongWritable>.Context context)
			throws IOException, InterruptedException {
		//接收数据V1,
		//k1 --> 序号(在map中没有用到)   v1 --> value
		String line = value.toString();
		//切分数据
		String[] words = line.split(" ");
		//循环
		for(String word:words){
			//出现一次计一个一;
			//k2 --> word		v2 --> 1
			context.write(new Text(word), new LongWritable(1));
		}
	}
	
}
