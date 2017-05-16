package cn.itcast.tom.hadoop.hadooprpc.common;

/**
 * 
 * <p>Titile:NameNodeMetaData</p>
 * <p>Description:客户端和服务端都需要遵循的一个沟通法则 </p>
 * @author TOM
 * @date 2017年5月16日 下午4:04:59
 */
public interface NameNodeMetaData {
	public static final long versionID =1l;
	//路径
	public String getMetaData(String path);
}
