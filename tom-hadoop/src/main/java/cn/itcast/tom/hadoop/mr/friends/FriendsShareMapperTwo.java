package cn.itcast.tom.hadoop.mr.friends;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class FriendsShareMapperTwo extends Mapper<LongWritable, Text, Text, Text>{
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		String[] friend_persons = line.split("\t");
		String friend = friend_persons[0];
		String persons[] = friend_persons[1].split(",");
		Arrays.sort(persons);
		for(int i=0;i<persons.length-2;i++){
			for(int j=i+1;j<persons.length-1;j++){
				//发出<人-人,好友>,这样相同的"人--人"对的所有好友就会到同一个reducer中
				context.write(new Text(persons[i]+"--"+persons[j]), new Text(friend));
			}
		}
	}
}
