package cn.itcast.tom.hadoop.mr.friends;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FriendsShareMapper extends Mapper<LongWritable, Text, Text, Text>{
	Text keyOut = new Text();
	Text valueOut = new Text();
	//A:B,C,D,F,E,O
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] person_friends = line.split(":");
		String person = person_friends[0];
		String[] friends = person_friends[1].split(",");
		for(String friend:friends){
			keyOut.set(friend);
			valueOut.set(person);
			//输出<好友,人>
			context.write(keyOut, valueOut);
		}
	}
}
