package cn.itcast.tom.hadoop.mr.friends;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FriendsShareReducer extends Reducer<Text, Text, Text, Text>{
	Text valueOut = new Text();
	@Override
	protected void reduce(Text friend, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		StringBuffer buffer = new StringBuffer();
		for(Text person:values){
			buffer.append(person).append(",");
		}
		valueOut.set(buffer.toString().substring(0, buffer.length()));
		context.write(friend, valueOut);
	}
}

