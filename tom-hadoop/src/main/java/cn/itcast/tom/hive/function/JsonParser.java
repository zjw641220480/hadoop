package cn.itcast.tom.hive.function;

import java.io.IOException;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonParser extends UDF{
	public String evaluate(String jsonLine)  {
		ObjectMapper objectMapper = new ObjectMapper();
		MovieRateBean bean;
		try {
			bean = objectMapper.readValue(jsonLine, MovieRateBean.class);
			System.out.println(bean.toString());
			return bean.toString();
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
