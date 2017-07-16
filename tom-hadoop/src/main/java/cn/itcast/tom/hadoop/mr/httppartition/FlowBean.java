package cn.itcast.tom.hadoop.mr.httppartition;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 * 这个类中需要注意的有以下几点
 * 	1:此类需要进行网络间数据传输,故需要实现Hadoop的专用序列化机制,实现Writable接口;
 * 	2:类中的各个属性还是使用java自带的各种数据类型即可;
 * 	3:重写其序列化和反序列化的方法;
 * <p>Titile:FlowBean</p>
 * <p>Description: 上下行流量的Bean</p>
 * @author TOM
 * @date 2017年5月17日 下午6:39:39
 */
public class FlowBean implements Writable{
	private int upFlow;
	private int downFlow;
	private int sum;
	public int getUpFlow() {
		return upFlow;
	}
	public void setUpFlow(int upFlow) {
		this.upFlow = upFlow;
	}
	public int getDownFlow() {
		return downFlow;
	}
	public void setDownFlow(int downFlow) {
		this.downFlow = downFlow;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum) {
		this.sum = sum;
	}
	/**
	 * 序列化方法
	 */
	public void write(DataOutput out) throws IOException {
		//write和read调用的方法类型要完全保持顺序和类型的一致
		out.writeInt(upFlow);
		out.writeInt(downFlow);
		out.writeInt(sum);
	}
	/**
	 * 反序列化方法
	 */
	public void readFields(DataInput in) throws IOException {
		//write和read调用的方法类型要完全保持顺序和类型的一致
		this.upFlow = in.readInt();
		this.downFlow = in.readInt();
		this.sum = in.readInt();
	}
	//按照指定格式进行输出
	@Override
	public String toString() {
		return this.upFlow+"\t"+this.downFlow+"\t"+this.sum;
	}
}	
