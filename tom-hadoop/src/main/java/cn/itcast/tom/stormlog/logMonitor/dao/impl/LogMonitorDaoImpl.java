package cn.itcast.tom.stormlog.logMonitor.dao.impl;

import java.util.List;

import cn.itcast.tom.stormlog.logMonitor.dao.LogMonitorDao;
import cn.itcast.tom.stormlog.logMonitor.domain.App;
import cn.itcast.tom.stormlog.logMonitor.domain.Record;
import cn.itcast.tom.stormlog.logMonitor.domain.Rule;
import cn.itcast.tom.stormlog.logMonitor.domain.User;

public class LogMonitorDaoImpl implements LogMonitorDao{

	@Override
	public List<Rule> getRuleList() {
		return null;
	}

	@Override
	public List<App> getAppList() {
		return null;
	}

	@Override
	public List<User> getUserList() {
		return null;
	}

	@Override
	public void saveRecord(Record record) {
		
	}

	@Override
	public User getUserByUserId(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
