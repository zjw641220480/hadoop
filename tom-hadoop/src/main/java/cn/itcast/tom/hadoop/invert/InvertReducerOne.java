package cn.itcast.tom.hadoop.invert;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * <p>Title:InvertReducerOne.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月6日上午10:23:57
 */
public class InvertReducerOne extends Reducer<Text, Text, Text, Text> {
	private Text v2 = new Text();

	@Override
	protected void reduce(Text arg0, Iterable<Text> arg1, Reducer<Text, Text, Text, Text>.Context arg2)
			throws IOException, InterruptedException {
		String result = "";
		for (Text text : arg1) {
			result += text.toString() + "\t";
		}
		v2.set(result);
		arg2.write(arg0, v2);
	}

}
