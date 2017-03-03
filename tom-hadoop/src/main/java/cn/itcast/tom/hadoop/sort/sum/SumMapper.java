package cn.itcast.tom.hadoop.sort.sum;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import cn.itcast.tom.hadoop.sort.SumOutput;

/**
 *
 * <p>Title:SumMapper.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月3日上午10:42:09
 */
public class SumMapper extends Mapper<LongWritable, Text, Text, SumOutput>{
	private Text k;
	private SumOutput output = new SumOutput();
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, SumOutput>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] strs = line.split("\t");
		k = new Text(strs[0]);
		String account = strs[0];
		double income = new Double(strs[1]);
		double expenses =new Double(strs[2]);
		output.set(account, income, expenses);
		context.write(k, output);
	}
	
}
