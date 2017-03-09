package cn.itcast.tom.hadoop.simple.sort.sum;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import cn.itcast.tom.hadoop.simple.sort.SumOutput;

/**
 *
 * <p>Title:SumReducer.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月3日上午10:42:26
 */
public class SumReducer extends Reducer<Text, SumOutput, Text, SumOutput> {
	private SumOutput sumOutput = new SumOutput();

	@Override
	protected void reduce(Text key, Iterable<SumOutput> values,
			Reducer<Text, SumOutput, Text, SumOutput>.Context context) throws IOException, InterruptedException {
		double in_sum = 0;
		double out_sum = 0;
		for (SumOutput output : values) {
			in_sum += output.getIncome();
			out_sum += output.getExpenses();
		}
		sumOutput.set("", in_sum, out_sum);
		context.write(key, sumOutput);
	}

}
