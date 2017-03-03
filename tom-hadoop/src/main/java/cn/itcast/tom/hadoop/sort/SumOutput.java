package cn.itcast.tom.hadoop.sort;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;

/**
 *
 * <p>Title:SumOutput.java</p>
 * <p>Description:</p>
 * @author TOM
 * @date 2017年3月3日下午2:59:46
 */
public class SumOutput implements Writable, WritableComparable<SumOutput> {

	private String account;

	private double income;

	private double expenses;

	private double surplus;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	public double getExpenses() {
		return expenses;
	}

	public void setExpenses(double expenses) {
		this.expenses = expenses;
	}

	public double getSurplus() {
		return surplus;
	}

	public void setSurplus(double surplus) {
		this.surplus = surplus;
	}

	public void set(String account, double income, double expenses) {
		this.account = account;
		this.income = income;
		this.expenses = expenses;
		this.surplus = income - expenses;
	}

	public void write(DataOutput out) throws IOException {
		out.writeUTF(account);
		out.writeDouble(income);
		out.writeDouble(expenses);
		out.writeDouble(surplus);
	}

	public void readFields(DataInput in) throws IOException {
		this.account = in.readUTF();
		this.income = in.readDouble();
		this.expenses = in.readDouble();
		this.surplus = in.readDouble();
	}

	public int compareTo(SumOutput o) {
		if (this.income == o.income) {
			return this.expenses > o.expenses ? -1 : 1;
		} else {
			return this.income > o.income ? 1 : -1;
		}
	}

	@Override
	public String toString() {
		//return "SumOutput [account=" + account + ", income=" + income + ", expenses=" + expenses + ", surplus="
		//		+ surplus + "]";
		return this.income+"\t"+this.expenses+"\t"+this.surplus;
	}

}
