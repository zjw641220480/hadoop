package cn.itcast.tom.stormlog.logAnalyze.storm.dao;

import java.util.Set;

import cn.itcast.tom.stormlog.logAnalyze.storm.domain.LogAnalyzeJob;
import cn.itcast.tom.stormlog.logAnalyze.storm.domain.LogAnalyzeJobDetail;

public interface LogAnalyzeDao {

	Set<LogAnalyzeJob> loadJobList();

	Set<LogAnalyzeJobDetail> loadJobDetailList();

}
