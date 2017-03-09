package cn.itcast.tom.hadoop.simple.sort.sort;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import cn.itcast.tom.hadoop.simple.sort.SumOutput;

/**
 *
 * <p>Title:SortResource.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月3日下午4:19:13
 */
public class SortReducer extends Reducer<SumOutput, NullWritable, Text, SumOutput> {
	private Text key = new Text();
	@Override
	protected void reduce(SumOutput key2, Iterable<NullWritable> v2s,
			Reducer<SumOutput, NullWritable, Text, SumOutput>.Context context) throws IOException, InterruptedException {
		key.set(key2.getAccount());
		context.write(key, key2);
	}

}
