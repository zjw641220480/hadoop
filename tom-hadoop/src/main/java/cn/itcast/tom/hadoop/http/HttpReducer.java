package cn.itcast.tom.hadoop.http;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/**
 *
 * <p>Title:HttpReducer.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月2日下午3:10:28
 */
public class HttpReducer extends Reducer<Text, HttpOutput, Text, HttpOutput> {
	/**
	 * reducer对被shuffle分组后的一行数据进行操作,每一行数据,运行一次reducer方法,
	 */
	@Override
	protected void reduce(Text k2, Iterable<HttpOutput> v2, Reducer<Text, HttpOutput, Text, HttpOutput>.Context context)
			throws IOException, InterruptedException {
		long upPayLoad = 0;
		long downPayLoad = 0;
		long totalPayLoad =0;
		HttpOutput resourcerOutput = new HttpOutput();
		for (HttpOutput mapOutput : v2) {
			upPayLoad += mapOutput.getUpPayLoad();
			downPayLoad += mapOutput.getDownPayLoad();
			totalPayLoad += mapOutput.getTotalPayLoad();
		}
		resourcerOutput.setTelNo(k2.toString());
		resourcerOutput.setDownPayLoad(downPayLoad);
		resourcerOutput.setUpPayLoad(upPayLoad);
		resourcerOutput.setTotalPayLoad(totalPayLoad);
		context.write(k2, resourcerOutput);
	}

}
