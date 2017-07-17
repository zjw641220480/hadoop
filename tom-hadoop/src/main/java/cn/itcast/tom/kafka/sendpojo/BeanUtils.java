package cn.itcast.tom.kafka.sendpojo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * <p>
 * Titile:BeanUtils
 * </p>
 * <p>
 * Description:对对象进行序列化/反序列化
 * </p>
 * 
 * @author TOM
 * @date 2017年7月17日 下午7:55:04
 */
public class BeanUtils {
	private BeanUtils() {
	}

	/**
	 * 
	 * @MethodName:objectToByte
	 * @Description:对象转字节数组
	 * @param obj
	 * @return
	 * @Time: 2017年7月17日 下午7:56:44
	 * @author: TOM
	 */
	public static byte[] objectToByte(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bo = null;
		ObjectOutputStream oo = null;
		try {
			bo = new ByteArrayOutputStream();
			oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bo != null) {
					bo.close();
				}
				if (oo != null) {
					oo.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bytes;
	}
	/**
	 * 
	 * @MethodName:byteToObject
	 * @Description:字节数组转对象
	 * @param bytes
	 * @return
	 * @Time: 2017年7月17日 下午7:57:42
	 * @author: TOM
	 */
	public static Object byteToObject(byte[] bytes){
		Object obj = null;
        ByteArrayInputStream bi = null;
        ObjectInputStream oi = null;
        try {
            bi =new ByteArrayInputStream(bytes);
            oi =new ObjectInputStream(bi);
            obj = oi.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(bi!=null){
                    bi.close();
                }
                if(oi!=null){
                    oi.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return obj;
	}
}
