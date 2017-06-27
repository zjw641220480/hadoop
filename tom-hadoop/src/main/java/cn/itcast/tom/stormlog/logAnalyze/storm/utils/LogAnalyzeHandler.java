package cn.itcast.tom.stormlog.logAnalyze.storm.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.google.gson.Gson;

import cn.itcast.tom.stormlog.logAnalyze.storm.constant.LogTypeConstant;
import cn.itcast.tom.stormlog.logAnalyze.storm.dao.LogAnalyzeDao;
import cn.itcast.tom.stormlog.logAnalyze.storm.domain.LogAnalyzeJob;
import cn.itcast.tom.stormlog.logAnalyze.storm.domain.LogAnalyzeJobDetail;
import cn.itcast.tom.stormlog.logAnalyze.storm.domain.LogMessage;
import redis.clients.jedis.ShardedJedis;

public class LogAnalyzeHandler {
	// 定时加载配置文件的标识
	private static boolean reload = false;
	// 用来保存job信息,key为jobType,value为该类别下所有的任务
	private static Map<String, Set<LogAnalyzeJob>> jobMap;
	// 用来保存job的判断条件,key为jobId,value为list,list中封装了很多判断条件
	private static Map<String, Set<LogAnalyzeJobDetail>> jobDetailMap;
	//
	@Resource(name = "logAnalyzeDao")
	private static LogAnalyzeDao logAnalyzeDao;
	static {
		jobMap = logJobMap();
		jobDetailMap = null;
	}
	public static LogMessage parser(String line){
		LogMessage logMessage = new Gson().fromJson(line, LogMessage.class);
		return logMessage;
	}
	/**
	 * 
	 * @MethodName:process
	 * @Description:pv 在redis中是string,key为log:{jobId}:pv:20151116,value=pv数量
	 * 			uv 使用java-bloomFilter计算,https://github.com/maoxiangyi/Java-BloomFilter
	 * @param logMessage
	 * @Time: 2017年6月27日 上午9:38:52
	 * @author: TOM
	 */
	public static void process(LogMessage logMessage){
		if(jobMap ==null||jobDetailMap == null){
			loadDataModel();
		}
		// kafka来的日志：2,req,ref,xxx,xxx,xxx,yy
		Set<LogAnalyzeJob> analyzeJobs = jobMap.get(logMessage.getType()+"");
		for(LogAnalyzeJob analyzeJob:analyzeJobs){
			boolean isMatch = false;//是否匹配
			Set<LogAnalyzeJobDetail> analyzeJobDetails = jobDetailMap.get(analyzeJob.getJobId()+"");
			for(LogAnalyzeJobDetail analyzeJobDetail:analyzeJobDetails){
				//jobDetail,指定和kakfa输入过来的数据中的 requesturl比较
                // 获取kafka输入过来的数据的requesturl的值
				String fieldValueInLog = logMessage.getCompareFieldValue(analyzeJobDetail.getField());
				//1包含,2等于,3正则
				if(analyzeJobDetail.getCompare()==1&&fieldValueInLog.contains(analyzeJobDetail.getValue())){
					isMatch=true;
				} else if(analyzeJobDetail.getCompare()==2&&fieldValueInLog.equals(analyzeJobDetail.getValue())){
					isMatch = true;
				} else {
					isMatch = false;
				}
				if(isMatch){
					//设置pv
					String pvKey = "log:" + analyzeJob.getJobName() + ":pv:" + DateUtils.getDate();
					String uvKey = "log:" + analyzeJob.getJobName() + ":uv:" + DateUtils.getDate();
					ShardedJedis jedis = MyShardedJedisPool.getShardedJedisPool().getResource();
					jedis.incr(pvKey);
					//设置uv
	                jedis.sadd(uvKey, logMessage.getUserName());
	                //优惠策略，使用bloomFilter算法进行优化
				}
			}
		}
	}
	/**
	 * 
	 * @MethodName:loadDataModel
	 * @Description:加载规则和规格明细
	 * @Time: 2017年6月27日 上午9:41:18
	 * @author: TOM
	 */
	public static void loadDataModel(){
		if(jobMap==null){
			jobMap = logJobMap();
		}
		if(jobDetailMap == null){
			jobDetailMap = loadJobDetailMap();
		}
	}
	/**
	 * 
	 * @MethodName:logJobMap
	 * @Description:加载所有的规则任务到Map中,其中key为jobId
	 * @return
	 * @Time: 2017年6月26日 下午4:21:43
	 * @author: TOM
	 */
	private static Map<String, Set<LogAnalyzeJob>> logJobMap() {
		Map<String, Set<LogAnalyzeJob>> map = new HashMap<String, Set<LogAnalyzeJob>>();
		// 加载所有的规则任务;
		Set<LogAnalyzeJob> logAnalyzeJobs = logAnalyzeDao.loadJobList();
		for (LogAnalyzeJob logAnalyzeJob : logAnalyzeJobs) {
			int jobType = logAnalyzeJob.getJobType();
			if (isValidType(jobType)) {
				Set<LogAnalyzeJob> jobSet = map.get(jobType + "");
				if (jobSet == null || jobSet.size() == 0) {
					jobSet = new HashSet<LogAnalyzeJob>();
					jobSet.add(logAnalyzeJob);
					map.put(jobType + "", jobSet);
				}
				jobSet.add(logAnalyzeJob);
				map.put(jobType + "", jobSet);
			}
		}
		return jobMap;
	}

	/**
	 * 
	 * @MethodName:isValidType
	 * @Description:
	 * @param jobType
	 * @return
	 * @Time: 2017年6月26日 下午4:17:05
	 * @author: TOM
	 */
	private static boolean isValidType(int jobType) {
		if (jobType == LogTypeConstant.BUY || jobType == LogTypeConstant.CLICK || jobType == LogTypeConstant.VIEW
				|| jobType == LogTypeConstant.SEARCH) {
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @MethodName:loadJobDetailMap
	 * @Description:加载规则详情到一个Map中,其中jobId为key
	 * @return
	 * @Time: 2017年6月26日 下午8:20:16
	 * @author: TOM
	 */
	private static Map<String,Set<LogAnalyzeJobDetail>> loadJobDetailMap(){
		Map<String,Set<LogAnalyzeJobDetail>> map = new HashMap<String, Set<LogAnalyzeJobDetail>>();
		Set<LogAnalyzeJobDetail> analyzeJobDetails = logAnalyzeDao.loadJobDetailList();
		for(LogAnalyzeJobDetail analyzeJobDetail:analyzeJobDetails){
			int jobId = analyzeJobDetail.getId();
			Set<LogAnalyzeJobDetail> jobDetailSet = map.get(analyzeJobDetail.getId()+"");
			if(jobDetailSet==null||jobDetailSet.size()<1){
				jobDetailSet = new HashSet<LogAnalyzeJobDetail>();
				jobDetailSet.add(analyzeJobDetail);
				map.put(analyzeJobDetail.getId()+"", jobDetailSet);
			}
			jobDetailSet.add(analyzeJobDetail);
			map.put(analyzeJobDetail.getId()+"", jobDetailSet);
		}
		return map;
	}
}
