package cn.itcast.tom.redis.datatype.string;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

import org.junit.Test;

import com.google.gson.Gson;

import cn.itcast.tom.netty.sendobject.bean.Person;
import redis.clients.jedis.Jedis;

/**
 * 
 * <p>Titile:SaveObjectToRedis</p>
 * <p>Description: 保存对象到redis中;</p>
 * @author TOM
 * @date 2017年6月21日 下午3:54:45
 */
public class SaveObjectToRedis {
	public static void main(String[] args) throws Exception {
		Person person = new Person();
		person.setName("zhangsan");
		person.setAge(12);
		Jedis jedis = new Jedis("127.0.0.1",6379);
		//直接保存对象的toString方法,这种方法不反序列对象
		jedis.set("user:zhangsan:str", person.toString());
		System.out.println(jedis.get("user:zhangsan:str"));
		
		//这边会有问题
		/*jedis.set("user:zhangsan:obj".getBytes(), getBytesByProduct(person));
		byte[] productBytes = jedis.get("user:zhangsan:obj").getBytes();
		Person personFromRedis = getProductByBytes(productBytes);
		System.out.println(personFromRedis.getName()+"\t"+personFromRedis.getAge());*/
		
		//
		jedis.set("user:zhangsan:json", new Gson().toJson(person));
		String personJson = jedis.get("user:zhangsan:json");
		Person pjson = new Gson().fromJson(personJson, Person.class);
		System.out.println(pjson.getName()+"\t"+pjson.getAge());
	}
	/**
     * 将对象转化成Byte数组
     *
     * @param product
     * @return
     * @throws Exception
     */
    private static byte[] getBytesByProduct(Person product) throws Exception {
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(ba);
        oos.writeObject(product);
        oos.flush();
        return ba.toByteArray();
    }
    /**
     * 从字节数组中读取Java对象
     *
     * @param productBytes
     * @return
     * @throws Exception
     */
    private static Person getProductByBytes(byte[] productBytes) throws Exception {
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(productBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteInputStream);
        return (Person) objectInputStream.readObject();
    }
    @Test
    public void testDeserialize() throws IOException, ClassNotFoundException {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        ObjectOutputStream oos = new ObjectOutputStream(baos);  
        BigInteger bi = new BigInteger("0");  
        oos.writeObject(bi);  
        String str = baos.toString();  
        System.out.println(str);  
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(str.getBytes())));  
        Object obj = ois.readObject();  
    }  
    @Test
    public void testDeserializeTwo() throws IOException, ClassNotFoundException {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        ObjectOutputStream oos = new ObjectOutputStream(baos);  
        BigInteger bi = new BigInteger("0");  
        oos.writeObject(bi);  
        byte[] str = baos.toByteArray();  
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(str)));  
        Object obj = ois.readObject();  
       System.out.println(obj);
    } 
}
