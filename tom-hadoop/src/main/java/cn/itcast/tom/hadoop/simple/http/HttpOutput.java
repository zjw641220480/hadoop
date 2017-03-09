package cn.itcast.tom.hadoop.simple.http;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

/**
 *
 * <p>Title:MapOutput.java</p>
 * <p>Description:Map的输出</p>
 * @author TOM
 * @date 2017年3月2日下午3:13:26
 */
public class HttpOutput implements Writable {
	
	public HttpOutput() {
		super();
	}

	public HttpOutput(String telNo, long upPayLoad, long downPayLoad) {
		super();
		this.telNo = telNo;
		this.upPayLoad = upPayLoad;
		this.downPayLoad = downPayLoad;
		this.totalPayLoad = this.upPayLoad + this.downPayLoad;
	}

	//序列化和反序列化的时候注意两者之间的顺序(order)和类型(type),
	private String telNo;
	private long upPayLoad;
	private long downPayLoad;
	private long totalPayLoad;

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public long getUpPayLoad() {
		return upPayLoad;
	}

	public void setUpPayLoad(long upPayLoad) {
		this.upPayLoad = upPayLoad;
	}

	public long getDownPayLoad() {
		return downPayLoad;
	}

	public void setDownPayLoad(long downPayLoad) {
		this.downPayLoad = downPayLoad;
	}

	public long getTotalPayLoad() {
		return totalPayLoad;
	}

	public void setTotalPayLoad(long totalPayLoad) {
		this.totalPayLoad = totalPayLoad;
	}

	/**
	 * 序列化
	 */
	//Serializable,
	public void write(DataOutput out) throws IOException {
		out.writeUTF(telNo);
		out.writeLong(upPayLoad);
		out.writeLong(downPayLoad);
		out.writeLong(totalPayLoad);
	}

	/**
	 * 反序列化
	 */
	//DeSerializable
	public void readFields(DataInput in) throws IOException {
		this.telNo = in.readUTF();
		this.upPayLoad = in.readLong();
		this.downPayLoad = in.readLong();
		this.totalPayLoad = in.readLong();
	}

	@Override
	public String toString() {
		String out = this.telNo +"\t"+this.upPayLoad+"\t"+this.downPayLoad+"\t"+this.totalPayLoad;
		return out;
	}

}
