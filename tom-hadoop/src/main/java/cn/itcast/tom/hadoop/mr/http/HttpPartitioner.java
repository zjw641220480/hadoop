package cn.itcast.tom.hadoop.mr.http;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;
/**
 * 
 * <p>Titile:HttpPartitioner</p>
 * <p>Description:结果进行分区 ,分区的数量,这个是需要自己来确定的;</p>
 * @author TOM
 * @date 2017年5月18日 下午4:31:09
 */
public class HttpPartitioner extends Partitioner<Text, FlowBean>{
	private static List<Integer> CUCC = new ArrayList<Integer>();
	private static List<Integer> CMCC = new ArrayList<Integer>();
	private static List<Integer> CTCC = new ArrayList<Integer>();
	static{
		CUCC.add(137);
		CUCC.add(138);
		CUCC.add(139);
		CMCC.add(182);
		CMCC.add(159);
		CMCC.add(150);
		CTCC.add(134);
		CTCC.add(183);
	}
	@Override
	public int getPartition(Text key, FlowBean value, int numPartitions) {
		String phone = key.toString();
		int phoneStart = Integer.valueOf(phone.substring(0,3));
		if(CUCC.contains(phoneStart)){
			return 1;
		}else if(CMCC.contains(phoneStart)){
			return 2;
		}else if(CTCC.contains(phoneStart)){
			return 3;
		}
		return 0;
	}

}
