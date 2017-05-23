package cn.itcast.tom.hadoop.mr.inver;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class InverReducerTwo extends Reducer<Text, Text, Text, Text>{
	Text valueOut = new Text();
	@Override
	protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String inver = "";
		for(Text value : values){
			inver += value+"\t";
		}
		valueOut.set(inver);
		context.write(key, valueOut);
	}
}
