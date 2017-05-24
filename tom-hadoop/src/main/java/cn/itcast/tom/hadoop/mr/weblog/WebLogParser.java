package cn.itcast.tom.hadoop.mr.weblog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WebLogParser {
	static SimpleDateFormat sd1 = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);

	static SimpleDateFormat sd2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static WebLogBean parser(String line){
		WebLogBean  webLogBean = new WebLogBean();
		String[] arrs = line.split(" ");
		if(arrs.length>11){
			webLogBean.setRemote_addr(arrs[0]);
			webLogBean.setRemote_user(arrs[1]);
			webLogBean.setTime_local(arrs[3].substring(1));
			webLogBean.setRequest(arrs[6]);
			webLogBean.setStatus(arrs[8]);
			webLogBean.setBody_bytes_sent(arrs[9]);
			webLogBean.setHttp_referer(arrs[10]);
			if(arrs.length>12){
				webLogBean.setHttp_user_agent(arrs[11]+""+arrs[12]);
			}else {
				webLogBean.setHttp_user_agent(arrs[11]);
			}
		}else{
			webLogBean.setValid(false);
		}
		return webLogBean;
	}
	
	public String parserDate(String dt){
		String timeString = "";
		try {
			Date parse = sd1.parse(dt);
			timeString = sd2.format(parse);

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timeString;

	}
}
