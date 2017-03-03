package cn.itcast.tom.hadoop.sort.sort;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cn.itcast.tom.hadoop.sort.SumOutput;

/**
 *
 * <p>Title:SortMapper.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月3日下午4:18:57
 */
public class SortMapper extends Mapper<LongWritable, Text, SumOutput, NullWritable>{
	private SumOutput k = new SumOutput();
	
	@Override
	protected void map(LongWritable key, Text value,
			Mapper<LongWritable, Text, SumOutput, NullWritable>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] strs = line.split("\t");
		String account = strs[0];
		double income = new Double(strs[1]);
		double expenses =new Double(strs[2]);
		k.set(account, income, expenses);
		context.write(k, NullWritable.get());
	}


}
