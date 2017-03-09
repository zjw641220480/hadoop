package cn.itcast.tom.hadoop.simple.invert;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * <p>Title:InvertCombinerOne.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月6日上午10:17:52
 */
public class InvertCombinerOne extends Reducer<Text, Text, Text, Text> {
	private Text v2 = new Text();
	private Text k2 = new Text();

	@Override
	protected void reduce(Text arg0, Iterable<Text> arg1, Reducer<Text, Text, Text, Text>.Context arg2)
			throws IOException, InterruptedException {
		String[] str = arg0.toString().split("-->");
		String word = str[0];
		String path = str[1];
		long count = 0;
		for (Text longWritable : arg1) {
			count += new Integer(longWritable.toString());
		}
		k2.set(word);
		v2.set(path + "-->" + count);
		arg2.write(k2, v2);
	}

}
