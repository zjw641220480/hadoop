package cn.itcast.tom.hadoop.mr.item;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 
 * <p>Titile:ItemIdGroupComparator</p>
 * <p>Description: 调用Reducer的时候对数据进行分组用的</p>
 * @author TOM
 * @date 2017年5月24日 下午4:54:30
 */
public class ItemIdGroupComparator extends WritableComparator {
	//传入作为key的bean的class类型,以及指定需要让框架做反射获取实例对象
	public ItemIdGroupComparator() {
		super(OrderBean.class, true);
	}

	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		OrderBean beanA = (OrderBean) a;
		OrderBean beanB = (OrderBean) b;
		//只比较两个bean中的orderId
		return beanA.getItemId().compareTo(beanB.getItemId());
	}
}
