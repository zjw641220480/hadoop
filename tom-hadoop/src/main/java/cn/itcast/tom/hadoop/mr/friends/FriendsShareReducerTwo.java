package cn.itcast.tom.hadoop.mr.friends;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class FriendsShareReducerTwo extends Reducer<Text, Text, Text, Text>{

	@Override
	protected void reduce(Text person_person, Iterable<Text> friends, Reducer<Text, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		StringBuffer buffer = new StringBuffer();
		for(Text frend:friends){
			buffer.append(frend).append(",");
		}
		context.write(person_person, new Text(buffer.toString()));
	}
	
}
