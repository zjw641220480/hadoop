package cn.itcast.tom.hadoop.http;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 *
 * <p>Title:HttpProviderPartitioner.java</p>
 * <p>Description:第一个partitioner</p>
 * @author TOM
 * @date 2017年3月3日上午9:08:29
 */
public class HttpProviderPartitioner extends Partitioner<Text, HttpOutput> {

	private static Map<String, Integer> providerMap = new HashMap<String, Integer>();

	static {
		providerMap.put("135", 1);
		providerMap.put("137", 1);
		providerMap.put("138", 1);
		providerMap.put("139", 1);
		providerMap.put("150", 2);
		providerMap.put("159", 2);
		providerMap.put("182", 3);
		providerMap.put("183", 3);
	}

	@Override
	public int getPartition(Text key, HttpOutput value, int numPartitions) {
		String account = key.toString();
		String sub_tel = account.substring(0, 3);
		Integer code = providerMap.get(sub_tel);
		if(code == null){
			code = 0;
		}
		return code;
	}

}
