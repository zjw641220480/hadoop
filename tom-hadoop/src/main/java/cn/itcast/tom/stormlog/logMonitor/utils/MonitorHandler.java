package cn.itcast.tom.stormlog.logMonitor.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import backtype.storm.command.list;
import cn.itcast.tom.redis.datatype.list.DateUtil;
import cn.itcast.tom.stormlog.logMonitor.dao.LogMonitorDao;
import cn.itcast.tom.stormlog.logMonitor.domain.App;
import cn.itcast.tom.stormlog.logMonitor.domain.Message;
import cn.itcast.tom.stormlog.logMonitor.domain.Record;
import cn.itcast.tom.stormlog.logMonitor.domain.Rule;
import cn.itcast.tom.stormlog.logMonitor.domain.User;
import cn.itcast.tom.stormlog.logMonitor.mail.MailInfo;
import cn.itcast.tom.stormlog.logMonitor.mail.MessageSender;
import cn.itcast.tom.stormlog.logMonitor.sms.SMSBase;

/**
 * 
 * <p>Titile:MonitorHandler</p>
 * <p>Description:日志监控核心类 </p>
 * @author TOM
 * @date 2017年6月24日 上午9:38:39
 */
public class MonitorHandler {
	private static Logger logger = Logger.getLogger(MonitorHandler.class);
	// 定义一个map,其中appid为key,以该AppId下的所有Rule为value
	private static Map<String, List<Rule>> ruleMap;
	// 定义一个map,其中appid为key,以该appid下的所有user为value
	private static Map<String, List<User>> userMap;
	// 定义一个List,封装所有的应用信息
	private static List<App> appList;
	// 定义一个List,封装所有的用户信息
	private static List<User> userList;
	// 定义加载配置文件标识
	private static boolean reload = false;
	// 定时加载配置文件标识
	private static long nextReload = 01;
	// 锁
	private static Lock lock = new ReentrantLock();
	@Resource(name = "logMonitorDao")
	private static LogMonitorDao logMonitorDao;
	static {
		load();
	}

	/**
	 * 
	 * @MethodName:load
	 * @Description:加载相关信息
	 * @Time: 2017年6月24日 上午10:47:31
	 * @author: TOM
	 */
	public static void load() {
		try {
			lock.lock();
			if (userList == null) {
				userList = logMonitorDao.getUserList();
			}
			if (appList == null) {
				appList = logMonitorDao.getAppList();
			}
			if (ruleMap == null) {
				ruleMap = loadRuleMap();
			}
			if (userMap == null) {
				userMap = loadUserMap();
			}
		} finally {
			lock.unlock();
		}
	}

	/**
	 * 
	 * @MethodName:parser
	 * @Description:解析输入日志,将数据按照一定的规则分割 判断日志是否合法,主要校验日志所属应用的appid是否存在,日志格式暂时不做检验,而且此方法并不对日志内容做处理
	 * @param line
	 *            一条日志
	 * @return
	 * @Time: 2017年6月24日 上午10:48:18
	 * @author: TOM
	 */
	public static Message parser(String line) {
		// 日志内容分为两个部分,由5个$$$$$符号作为分隔符,第一部分为appid,第二部分为日志内容
		String[] messageArr = line.split("\\$\\$\\$\\$\\$");
		// 对日志进行校验
		if (messageArr.length != 2) {
			return null;
		}
		if (StringUtils.isBlank(messageArr[0]) || StringUtils.isBlank(messageArr[1])) {
			return null;
		}
		// 检验当前日志所属Appid是否经过授权
		if (apppIdisValid(messageArr[0].trim())) {
			Message message = new Message();
			message.setAppId(messageArr[0].trim());
			message.setLine(messageArr[1].trim());
			return message;
		}
		return null;
	}

	/**
	 * 
	 * @MethodName:trigger
	 * @Description:对日志是否触发了规则进行初步判断,日志中是否出现了要进行规则判断的关键词;后期再对此关键词进行解析
	 * @param message
	 * @return
	 * @Time: 2017年6月24日 上午10:58:42
	 * @author: TOM
	 */
	public static boolean trigger(Message message) {
		if (ruleMap == null) {
			load();
		}
		System.out.println(message.getAppId());
		List<Rule> ruleListByAppId = ruleMap.get(message.getAppId() + "");
		for (Rule rule : ruleListByAppId) {
			// 如果日志中包含过滤过的关键词,即为匹配成功
			// 这里是最核心的地方了,也可以是正则表达式
			if (message.getLine().contains(rule.getKeyword())) {
				message.setRuleId(rule.getId() + "");
				message.setKeyword(rule.getKeyword());
				return true;
			}
		}
		return false;
	}

	/**
	 * 定时加载配置信息
     * 配合reloadDataModel模块一起使用。
     * 主要实现原理如下：
     * 1，获取分钟的数据值，当分钟数据是10的倍数，就会触发reloadDataModel方法，简称reload时间。
     * 2，reloadDataModel方式是线程安全的，在当前worker中只有一个线程能够操作。
     * 3，为了保证当前线程操作完毕之后，其他线程不再重复操作，设置了一个标识符reloaded。
     *      在非reload时间段时，reloaded一直被置为true；
     *      在reload时间段时，第一个线程进入reloadDataModel后，加载完毕之后会将reloaded置为false。
	 * @MethodName:scheduleLoad
	 * @Description:定时更新规则信息
	 * @Time: 2017年6月24日 上午11:05:49
	 * @author: TOM
	 */
	public static void scheduleLoad() {
		String date = DateUtils.getDateTime();
		int nowMin = Integer.parseInt(date.split(":")[1]);
		if (nowMin % 10 == 0) {// 每10分钟加载一次
			reloadDataModel();
		} else {
			// 理解成默认是需要实时加载的;到整点的时候整好加载,其他并发度,此时不能进行加载了;
			// 这里最好还是直接搞一个定时任务得了,难以理解的话
			reload = true;
		}
	}

	

	/**
	 * 
	 * @MethodName:notifly
	 * @Description:告警模块,用来发送短信和邮件
	 * @param appId
	 * @param message
	 * @Time: 2017年6月25日 下午2:55:18
	 * @author: TOM
	 */
	public static void notifly(String appId, Message message) {
		// 通过appId获取应用的相关负责人
		List<User> users = userMap.get(appId);
		// 发送邮件
		if (sendMail(appId, users, message)) {
			message.setIsEmail(1);
		}
		// 发送短信
		// 发送短信
		if (sendSms(appId, users, message)) {
			message.setIsPhone(1);
		}
	}

	/**
	 * 
	 * @MethodName:save
	 * @Description:保存到数据库(最好是保存到redis,或者一个队列中,最后分批次保存到数据库中)
	 * @param record
	 * @Time: 2017年6月25日 下午4:21:34
	 * @author: TOM
	 */
	public static void save(Record record) {
		logMonitorDao.saveRecord(record);
	}

	/**
	 * 
	 * @MethodName:apppIdisValid
	 * @Description:查看appid是否经过授权
	 * @param appId
	 * @return
	 * @Time: 2017年6月24日 上午10:54:28
	 * @author: TOM
	 */
	private static boolean apppIdisValid(String appId) {
		try {
			for (App app : appList) {
				if (app.getId() == Integer.parseInt(appId)) {
					return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * 
	 * @MethodName:loadRuleMap
	 * @Description:封装应用于规则之间的Map,一个应用多个规则
	 * @return
	 * @Time: 2017年6月24日 上午10:26:05
	 * @author: TOM
	 */
	private static Map<String, List<Rule>> loadRuleMap() {
		Map<String, List<Rule>> ruleMap = new HashMap<String, List<Rule>>();
		List<Rule> ruleList = logMonitorDao.getRuleList();
		// 将代表rule的List转化为一个map,转化的逻辑是
		// 从rule.getAppId中获取每一个rule对应的appid,使用appid作为key,然后将rule对象作为value传入map中
		// ruleMap<appid,ruleList>一个appid的规则消息,保存到一个list中
		int appid = 0;
		for (Rule rule : ruleList) {
			appid = rule.getAppId();
			List<Rule> listByAppId = ruleMap.get(appid + "");
			if (listByAppId == null) {
				listByAppId = new ArrayList<Rule>();
				listByAppId.add(rule);
				ruleMap.put(appid + "", listByAppId);
			} else {
				listByAppId.add(rule);
				ruleMap.put(appid + "", listByAppId);
			}
		}
		return ruleMap;
	}

	/**
	 * 
	 * @MethodName:loadUserMap
	 * @Description:封装应用于用户之间的map,一个应用可以被多个人员所管理(监控的通知对象);
	 * @return
	 * @Time: 2017年6月24日 上午10:27:05
	 * @author: TOM
	 */
	private static Map<String, List<User>> loadUserMap() {
		// 以应用的appid为key,以应用的所有负责人userList对象为value
		Map<String, List<User>> userMap = new HashMap<String, List<User>>();
		// app中保存有此应用的监控人员,多个监控人员使用逗号隔开
		List<App> appList = logMonitorDao.getAppList();
		for (App app : appList) {
			int appId = 0;
			List<User> userList = userMap.get(appId + "");
			appId = app.getId();
			String[] users = app.getUserId().split(",");
			for (String userId : users) {
				if (userList == null || userList.size() < 1) {
					userList = new ArrayList<User>();
					userList.add(logMonitorDao.getUserByUserId(userId));
					userMap.put(appId + "", userList);
				} else {
					userList.add(logMonitorDao.getUserByUserId(userId));
					userMap.put(appId + "", userList);
				}
			}
		}
		return userMap;
	}

	/**
	 * 
	 * @MethodName:listToStringFormat
	 * @Description:将List转化为String,各个元素之间使用逗号进行隔开
	 * @param list
	 * @return
	 * @Time: 2017年6月25日 下午3:52:10
	 * @author: TOM
	 */
	private static String listToStringFormat(List<String> list) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			if (i == list.size() - 1) {
				stringBuilder.append(list.get(i));
			} else {
				stringBuilder.append(list.get(i)).append(",");
			}
		}
		return stringBuilder.toString();
	}

	/**
	 * 
	 * @MethodName:sendMail
	 * @Description:
	 * @param appId
	 * @param users
	 * @param message
	 * @return
	 * @Time: 2017年6月25日 下午3:51:31
	 * @author: TOM
	 */
	private static boolean sendMail(String appId, List<User> users, Message message) {
		// 用来存储需要发送人的邮件地址
		List<String> receive = new ArrayList<String>();
		for (User user : userList) {
			receive.add(user.getEmail());
		}
		// 获取应用名称
		for (App app : appList) {
			if (app.getId() == Integer.parseInt(appId)) {
				message.setAppName(app.getName());
				break;
			}
		}
		if (receive.size() >= 1) {
			String date = DateUtils.getDateTime();
			String content = "系统【" + message.getAppName() + "】在 " + date + " 触发规则 " + message.getRuleId() + " ，过滤关键字为："
					+ message.getKeyword() + "  错误内容：" + message.getLine();
			// 创建要发送多封邮件,随后发送
			MailInfo mailInfo = new MailInfo("系统运行日志监控", content, receive, null);
			return MessageSender.sendMail(mailInfo);
		}
		return false;
	}

	/**
	 * 
	 * @MethodName:sendSms
	 * @Description:
	 * @return
	 * @Time: 2017年6月25日 下午3:48:26
	 * @author: TOM
	 */
	private static boolean sendSms(String appId, List<User> users, Message message) {
		List<String> mobileList = new ArrayList<String>();
		for (User user : users) {
			mobileList.add(user.getMobile());
		}
		for (App app : appList) {
			if (app.getId() == Integer.parseInt(appId.trim())) {
				message.setAppName(app.getName());
				break;
			}
		}
		String content = "系统【" + message.getAppName() + "】在 " + DateUtils.getDateTime() + " 触发规则 " + message.getRuleId()
				+ ",关键字：" + message.getKeyword();
		return SMSBase.sendSms(listToStringFormat(mobileList), content);
	}
	/**
	 * 
	 * @MethodName:reloadDataModel
	 * @Description:定时加载更新
	 * @Time: 2017年6月25日 下午4:42:20
	 * @author: TOM
	 */
	private static void reloadDataModel() {
		lock.lock();
		try {
			if (reload) {// true的时候加载
				long start = System.currentTimeMillis();
				userList = logMonitorDao.getUserList();
				appList = logMonitorDao.getAppList();
				ruleMap = loadRuleMap();
				userMap = loadUserMap();
				reload = false;
				nextReload = 0l;
				logger.info(
						"配置文件reload完成，时间：" + DateUtils.getDateTime() + " 耗时：" + (System.currentTimeMillis() - start));
			}
		} finally {
			lock.unlock();
		}

	}
}
