package cn.itcast.tom.stormlog.orderMonitor.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import cn.itcast.tom.stormlog.orderMonitor.dao.OrderMonitorDao;
import cn.itcast.tom.stormlog.orderMonitor.domain.Condition;
import cn.itcast.tom.stormlog.orderMonitor.domain.PaymentInfo;
import cn.itcast.tom.stormlog.orderMonitor.domain.Trigger;

public class OrderMonitorHandler {
	// 封装rule规则,该规则有很多判断条件,key为ruleId(uuid),value为多个条件组成的list
	private static Map<String, List<Condition>> ruleMap;
	@Resource(name = "orderMonitorDao")
	private static OrderMonitorDao orderMonitorDao;
	// 加载业务人员配置的所有规则信息,每个规则由多个条件组成;
	static {
		loadRuleMap();
	}

	public static void saveTrigger(String orderId, List<String> ruleIdList) {
        List<Trigger> list = new ArrayList<>();
        for (String ruleId : ruleIdList) {
            list.add(new Trigger(orderId, ruleId));
        }
        orderMonitorDao.saveTrigger(list);

    }
	public static void savePaymentInfo(PaymentInfo paymentInfo) {
		orderMonitorDao.savePayment(paymentInfo);
    }
	public static List<String> match(PaymentInfo paymentInfo) {
		List<String> triggerRuleIdList = new ArrayList<String>();
		if (ruleMap == null || ruleMap.size() == 0) {
			loadRuleMap();
		}
		Set<String> ruleIds = ruleMap.keySet();
		for (String ruleId : ruleIds) {
			List<Condition> conditions = ruleMap.get(ruleId);
			boolean isTrigger = ConditionMatch.match(paymentInfo, conditions);
			if (isTrigger) {
				triggerRuleIdList.add(ruleId);
			}
		}
		return triggerRuleIdList;
	}

	private static void loadRuleMap() {
		if (ruleMap == null || ruleMap.size() == 0) {
			ruleMap = loadRules();
		}
	}

	private static Map<String, List<Condition>> loadRules() {
		Map<String, List<Condition>> map = new HashMap<String, List<Condition>>();
		List<Condition> conditions = orderMonitorDao.loadRules();
		for (Condition condition : conditions) {
			List<Condition> conditionsByRuleId = ruleMap.get(condition.getRuleId());
			if (conditionsByRuleId == null || conditionsByRuleId.size() == 0) {
				conditionsByRuleId = new ArrayList<Condition>();
				conditionsByRuleId.add(condition);
				map.put(condition.getRuleId(), conditionsByRuleId);
			}
			conditionsByRuleId.add(condition);
			map.put(condition.getRuleId(), conditionsByRuleId);
		}
		return map;
	}
}
