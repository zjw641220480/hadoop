package cn.itcast.tom.kafka.sendpojo;

import java.io.Serializable;
import java.util.Date;

public class Member implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int age;
	private Date birthday;
	private double soruce;

	public Member() {
		super();
	}

	public Member(String name, int age, Date birthday, double soruce) {
		super();
		this.name = name;
		this.age = age;
		this.birthday = birthday;
		this.soruce = soruce;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public double getSoruce() {
		return soruce;
	}

	public void setSoruce(double soruce) {
		this.soruce = soruce;
	}

	@Override
	public String toString() {
		return "Member [name=" + name + ", age=" + age + ", birthday=" + birthday + ", soruce=" + soruce + "]";
	}
}
