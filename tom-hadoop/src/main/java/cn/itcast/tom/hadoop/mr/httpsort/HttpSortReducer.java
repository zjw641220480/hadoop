package cn.itcast.tom.hadoop.mr.httpsort;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class HttpSortReducer extends Reducer<FlowSortBean, NullWritable, Text, FlowSortBean>{

	@Override
	protected void reduce(FlowSortBean key, Iterable<NullWritable> values, Reducer<FlowSortBean, NullWritable, Text, FlowSortBean>.Context context)
			throws IOException, InterruptedException {
		context.write(new Text("a"), key);
	}

	
}
