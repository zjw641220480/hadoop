package cn.itcast.tom.hadoop.hadooprpc.sever;

import cn.itcast.tom.hadoop.hadooprpc.common.NameNodeMetaData;

/**
 * 
 * <p>Titile:NameNodeMetaDataImpl</p>
 * <p>Description: 服务端需要实现具体规则</p>
 * @author TOM
 * @date 2017年5月16日 下午4:06:47
 */
public class NameNodeMetaDataImpl implements NameNodeMetaData {

	public String getMetaData(String path) {
		
		return path+":3 {block_1,block_2,block_3}.......";
	}

}
