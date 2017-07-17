package cn.itcast.tom.kafka.sendpojo;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

/**
 * 
 * <p>Titile:ObjectEncoder</p>
 * <p>Description:Object编码为字节数组 </p>
 * @author TOM
 * @date 2017年7月17日 下午7:58:53
 */
public class ObjectEncoder implements Encoder<Member>{
	public ObjectEncoder(VerifiableProperties verifiableProperties){  
    }
	@Override
	public byte[] toBytes(Member member) {
		return BeanUtils.objectToByte(member);
	}
}
