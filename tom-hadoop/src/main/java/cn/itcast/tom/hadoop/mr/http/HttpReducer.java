package cn.itcast.tom.hadoop.mr.http;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class HttpReducer extends Reducer<Text, FlowBean, Text, FlowBean>{

	@Override
	protected void reduce(Text key, Iterable<FlowBean> value, Reducer<Text, FlowBean, Text, FlowBean>.Context context)
			throws IOException, InterruptedException {
		int up = 0;
		int down = 0;
		for(FlowBean flowBean:value){
			up = up + flowBean.getUpFlow();
			down = down + flowBean.getDownFlow();
		}
		FlowBean bean = new FlowBean();
		bean.setUpFlow(up);
		bean.setDownFlow(down);
		bean.setSum(up+down);
		context.write(key, bean);
	}
	
}
