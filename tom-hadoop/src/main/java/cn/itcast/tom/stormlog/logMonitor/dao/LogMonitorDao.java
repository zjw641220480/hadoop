package cn.itcast.tom.stormlog.logMonitor.dao;

import java.util.List;

import cn.itcast.tom.stormlog.logMonitor.domain.App;
import cn.itcast.tom.stormlog.logMonitor.domain.Record;
import cn.itcast.tom.stormlog.logMonitor.domain.Rule;
import cn.itcast.tom.stormlog.logMonitor.domain.User;

public interface LogMonitorDao {
	// 查询所有规则
	public List<Rule> getRuleList();

	// 查询所有应用的消息
	public List<App> getAppList();

	// 查询所有用户的信息
	public List<User> getUserList();

	// 保存规则触发消息
	public void saveRecord(Record record);

	//根据UserId查询User
	public User getUserByUserId(String userId);
}
