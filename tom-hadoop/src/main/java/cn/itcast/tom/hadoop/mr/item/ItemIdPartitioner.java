package cn.itcast.tom.hadoop.mr.item;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class ItemIdPartitioner extends Partitioner<OrderBean, NullWritable>{

	@Override
	public int getPartition(OrderBean bean, NullWritable value, int numReducerTasks) {
		//相同ID的订单Bean,会发往相同的Partitioner,而且产生的分区数,是会跟用户设置的reducer task数保持一致
		return (bean.getItemId().hashCode() & Integer.MAX_VALUE) % numReducerTasks;
	}

}
