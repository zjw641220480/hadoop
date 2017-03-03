package cn.itcast.tom.hadoop.http;

import java.io.IOException;
import java.util.List;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 *
 * <p>Title:HttpMapper.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月2日下午3:10:16
 */
public class HttpMapper extends Mapper<LongWritable, Text, Text, HttpOutput> {
	/**
	 * map针对的只是原有数据中的一行,生成的也是这一行数据的内容,这里只是从一行数据中取出来某些数据;(shuffle根据key来进行排序,分组等)
	 * 每一行数据运行一次map数据;
	 */
	@Override
	protected void map(LongWritable key, Text v1, Mapper<LongWritable, Text, Text, HttpOutput>.Context context)
			throws IOException, InterruptedException {
		String value = v1.toString();
		String[] strs = value.split("\t");

		String telNo = strs[1].trim();//手机号
		int upPayLoad = new Integer(strs[8].trim());//上行
		int downPayLoad = new Integer(strs[9].trim());//下行

		HttpOutput mapOutput = new HttpOutput(telNo, upPayLoad, downPayLoad);

		context.write(new Text(telNo), mapOutput);
	}

}
