package cn.itcast.tom.hadoop.mr.join;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class JoinReducer extends Reducer<Text, InfoBean, InfoBean, NullWritable>{
	
	
	@Override
	protected void reduce(Text pid, Iterable<InfoBean> beans,
			Reducer<Text, InfoBean, InfoBean, NullWritable>.Context context) throws IOException, InterruptedException {
		//分别把每一个key对应的值输出到下面两个容器中,(需要关注Mapper的输出)
		InfoBean infoBean = new InfoBean();
		List<InfoBean> infoBeans = new ArrayList<InfoBean>();
		for(InfoBean bean:beans){
			if("1".equals(bean.getFlag())){//商品信息,多个订单使用同一个商品信息
				try {
					BeanUtils.copyProperties(infoBean,bean);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {//订单信息,每个订单信息都不同
				InfoBean in = new InfoBean();
				try {
					BeanUtils.copyProperties(in, bean);
					infoBeans.add(in);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
		//拼接两类数据形成新的
		for(InfoBean bean:infoBeans){//循环订单信息
			bean.setPname(infoBean.getPname());
			bean.setCategary_id(infoBean.getCategary_id());
			bean.setPrice(infoBean.getPrice());
			context.write(bean, NullWritable.get());
		}
		
	}
	
}
