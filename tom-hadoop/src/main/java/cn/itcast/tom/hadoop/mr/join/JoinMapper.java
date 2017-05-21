package cn.itcast.tom.hadoop.mr.join;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

/**
 * 
 * <p>Titile:JoinMapper</p>
 * <p>Description: Mapper阶段两张表的数据进行拆分,并进行标记</p>
 * @author TOM
 * @date 2017年5月21日 下午3:42:12
 */
public class JoinMapper extends Mapper<LongWritable, Text, Text, InfoBean>{

	private InfoBean infoBean = new InfoBean();
	
	@Override
	protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, InfoBean>.Context context)
			throws IOException, InterruptedException {
		String line = value.toString();
		FileSplit inputSplit = (FileSplit) context.getInputSplit();
		String name = inputSplit.getPath().getName();
		String pid = "";
		//通过文件名判断是哪种数据
		if(name.startsWith("order")){
			String[] order_fields = line.split(",");
			pid = order_fields[2];
			infoBean.set(Integer.parseInt(order_fields[0]), order_fields[1], pid, Integer.parseInt(order_fields[3]), "", 0, 0, "0");
		}else{
			String[] order_fields = line.split(",");
			pid = order_fields[0];
			infoBean.set(0, "", pid, 0, order_fields[1], Integer.parseInt(order_fields[2]), Float.parseFloat(order_fields[3]), "1");
		}
		//这里注意key的值,依据key的值,两张表的数据之间进行了关联
		context.write(new Text(pid), infoBean);
	}
	
}
