package cn.itcast.tom.hadoop.mr.join.mapsidejoin;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * 
 * <p>Titile:MapSideJoinMapper</p>
 * <p>Description: 将缓存文件加入到map中</p>
 * @author TOM
 * @date 2017年5月21日 下午12:53:42
 */
public class MapSideJoinMapper extends Mapper<LongWritable, Text, Text, NullWritable>{
	//用一个hashMap来加载保存产品信息表
	private Map<String,String> itemInfoMap = new HashMap<String,String>();
	/**
	 * 通过阅读父类Mapper的源码,发现setup方法是在maptask处理数据之前调用一次,可以用来做一些初始化操作;
	 */
	@Override
	protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context)
			throws IOException, InterruptedException {
		//在Main中已经指定了此文件是在HDFS上
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("item.txt")));
		String line = null;
		while((line = bufferedReader.readLine())!= null){
			String fields[] = line.split(",");
			itemInfoMap.put(fields[0], fields[1]);
		}
		bufferedReader.close();
	}
	//由于已经持有完整的产品信息表,所以在map方法中就能实现join逻辑了;
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context)
			throws IOException, InterruptedException {
		String orderLine = value.toString();
		String[] fields = orderLine.split(",");
		String pdName = itemInfoMap.get(fields[2]);
		Text text = new Text();
		text.set(orderLine+"\t"+pdName);
		context.write(text,NullWritable.get());
	}
	
}
