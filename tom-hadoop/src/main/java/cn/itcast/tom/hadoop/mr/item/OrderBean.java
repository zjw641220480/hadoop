package cn.itcast.tom.hadoop.mr.item;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;

public class OrderBean implements WritableComparable<OrderBean>{
	private String itemId;
	private double amount;
	
	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public void write(DataOutput out) throws IOException {
		out.writeUTF(this.itemId);
		out.writeDouble(this.amount);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		this.itemId = in.readUTF();
		this.amount = in.readDouble();
	}

	@Override
	public int compareTo(OrderBean o) {
		int cmp = this.itemId.compareTo(o.getItemId());
		if(cmp==0){
			Double double1 = new Double(this.amount);
			Double double2 = o.getAmount();
			cmp = -double1.compareTo(double2);
		}
		return cmp;
	}

	@Override
	public String toString() {
		return "OrderBean [itemId=" + itemId + ", amount=" + amount + "]";
	}
}
