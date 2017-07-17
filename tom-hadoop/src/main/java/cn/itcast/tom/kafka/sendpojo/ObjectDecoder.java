package cn.itcast.tom.kafka.sendpojo;

import kafka.serializer.Decoder;

/**
 * 
 * <p>Titile:ObjectDecoder</p>
 * <p>Description:字节数组编码为Object </p>
 * @author TOM
 * @date 2017年7月17日 下午8:00:37
 */
public class ObjectDecoder implements Decoder<Member>{
	@Override
	public Member fromBytes(byte[] bytes) {
		return (Member) BeanUtils.byteToObject(bytes);
	}
	
}
