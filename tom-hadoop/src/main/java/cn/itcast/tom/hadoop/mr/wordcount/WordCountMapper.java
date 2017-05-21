package cn.itcast.tom.hadoop.mr.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/**
 * KEYIN:默认情况下,是mr框架所读到的一行文本的起始偏移量,Int  --> IntWritable
 * VALUEIN:默认情况下,是mr框架所读到的一行文本内容,String	--> Text
 * 
 * KEYOUT:是用户自定义处理逻辑处理完成后输出数据的key,在此处是单词,String	--> Text
 * VALUEOUT:是用户自定义处理逻辑处理完成后输出数据中的value,在此处是单词次数,Integer --> IntIntWritable
 * 上述数据都需要进行网络传输,需要序列化
 * 在Hadoop中有自己的更加精简的序列化接口,所以不直接使用Java中的数据类型,而是使用IntWritable
 * <p>Titile:WordCountMapper</p>
 * <p>Description: 统计单词出现次数的Mapper</p>
 * @author TOM
 * @date 2017年5月17日 上午8:26:12
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
	/**
	 * map阶段的业务逻辑就写在自定义的map()方法中
	 * maptask会对每一行输入数据调用一次此方法
	 */
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context)
			throws IOException, InterruptedException {
		//将maptask传给我们的文本内容先转换成String
		String line = value.toString();
		//根据空格将这一行切分成单词
		String[] words = line.split(" ");
		//将单词输出为key,value
		for(String word:words){
			//将单词作为key,将次数1作为value,以便于后续的数据分发,可以根据单词分发,以便于相同单词汇总到相同的reducetask
			context.write(new Text(word), new IntWritable(1));
		}
		//<hello,1>
		//切片,归并,汇总;所有hello单词的归并到一个迭代器,mapper和reducer之间省略了归并
	}
	
}
