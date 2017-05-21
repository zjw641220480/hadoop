package cn.itcast.tom.hadoop.mr.join;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;

public class InfoBean implements Writable{
	//订单的相关信息
	private int order_id;
	private String dateString;
	private String p_id;
	private int amount;
	//商品的相关信息
	private String pname;
	private int categary_id;
	private float price;
	
	/**
	 * flag = 0 标识这个对象是封装订单表记录(多)
	 * flag = 1标识这个对象是封装产品信息记录(少)
	 */
	private String flag;
	
	public InfoBean() { }
	
	public void set(int order_id, String dateString, String p_id, int amount, String pname, int categary_id, float price,String flag) {
		this.order_id = order_id;
		this.dateString = dateString;
		this.p_id = p_id;
		this.amount = amount;
		//下面是商品信息
		this.pname = pname;
		this.categary_id = categary_id;
		this.price = price;
		this.flag = flag;
	}

	public int getOrder_id() {
		return order_id;
	}


	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}


	public String getDateString() {
		return dateString;
	}


	public void setDateString(String dateString) {
		this.dateString = dateString;
	}


	public String getP_id() {
		return p_id;
	}

	public void setP_id(String p_id) {
		this.p_id = p_id;
	}

	public int getAmount() {
		return amount;
	}


	public void setAmount(int amount) {
		this.amount = amount;
	}


	public String getPname() {
		return pname;
	}


	public void setPname(String pname) {
		this.pname = pname;
	}


	public int getCategary_id() {
		return categary_id;
	}


	public void setCategary_id(int categary_id) {
		this.categary_id = categary_id;
	}


	public float getPrice() {
		return price;
	}


	public void setPrice(float price) {
		this.price = price;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeInt(order_id);
		out.writeUTF(dateString);
		out.writeUTF(p_id);
		out.writeInt(amount);
		out.writeUTF(pname);
		out.writeInt(categary_id);
		out.writeFloat(price);
		out.writeUTF(flag);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.order_id = in.readInt();
		this.dateString = in.readUTF();
		this.p_id = in.readUTF();
		this.amount = in.readInt();
		this.pname = in.readUTF();
		this.categary_id = in.readInt();
		this.price = in.readFloat();
		this.flag = in.readUTF();
	}

	@Override
	public String toString() {
		return "order_id=" + order_id + ", dateString=" + dateString + ", p_id=" + p_id + ", amount=" + amount
				+ ", pname=" + pname + ", categary_id=" + categary_id + ", price=" + price + ", flag=" + flag;
	}
}
