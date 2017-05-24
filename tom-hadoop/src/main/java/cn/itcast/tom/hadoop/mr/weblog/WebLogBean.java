package cn.itcast.tom.hadoop.mr.weblog;

/**
 * 
 * <p>Titile:WebLogBean</p>
 * <p>Description: POJO,不是直接把此对象进行输出,而是将其转换为了Text类型进行输出,故不需要再实现Writable接口</p>
 * @author TOM
 * @date 2017年5月23日 下午10:22:19
 */
public class WebLogBean{
	private String remote_addr;//记录客户端的IP地址
	private String remote_user;//记录客户端用户名称,忽略属性"-"
	private String time_local;//记录访问时间与时区
	private String request;//记录请求的url和http协议
	private String status;//记录请求状态,成功是200
	private String body_bytes_sent;//记录发送给客户端文件主题内容大小
	private String http_referer;//用来记录从哪个页面连接访问过来的
	private String http_user_agent;//记录客户浏览的相关信息;
	
	private boolean valid = true;

	public String getRemote_addr() {
		return remote_addr;
	}

	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}

	public String getRemote_user() {
		return remote_user;
	}

	public void setRemote_user(String remote_user) {
		this.remote_user = remote_user;
	}

	public String getTime_local() {
		return time_local;
	}

	public void setTime_local(String time_local) {
		this.time_local = time_local;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBody_bytes_sent() {
		return body_bytes_sent;
	}

	public void setBody_bytes_sent(String body_bytes_sent) {
		this.body_bytes_sent = body_bytes_sent;
	}

	public String getHttp_referer() {
		return http_referer;
	}

	public void setHttp_referer(String http_referer) {
		this.http_referer = http_referer;
	}

	public String getHttp_user_agent() {
		return http_user_agent;
	}

	public void setHttp_user_agent(String http_user_agent) {
		this.http_user_agent = http_user_agent;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("\001").append(this.remote_addr);
		sb.append("\001").append(this.remote_user);
		sb.append("\001").append(this.time_local);
		sb.append("\001").append(this.request);
		sb.append("\001").append(this.status);
		sb.append("\001").append(this.body_bytes_sent);
		sb.append("\001").append(this.http_referer);
		sb.append("\001").append(this.http_user_agent);
		return sb.toString();
	}

}
