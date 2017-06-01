package cn.itcast.tom.hive.function;

import java.util.Map;

import org.apache.commons.collections.map.HashedMap;
import org.apache.hadoop.hive.ql.exec.UDF;

public class HiveProvince extends UDF {
	static Map<String, String> provinceMap = new HashedMap();
	static {
		provinceMap.put("136", "上海");
		provinceMap.put("137", "北京");
		provinceMap.put("138", "天津");
	}

	// Hive方法(函数)
	public String evaluate(String phone) {
		if (null != phone && phone.length() > 3) {// 判断手机号格式是否正确
			String province = provinceMap.get(phone.substring(0, 3));
			if (province != null) {
				return province;
			} else {// 手机号正确但未录入
				return "huoxing";
			}
		}
		return "不合法的手机号";
	}
}