# top-hadoop
	FilterBolt:第一个bolt,主要对过来的日志进行初步过滤
	过滤的规则:
		格式是否正确.
		日志所属App是否已经授权
	PrepareRecordBolt:第二个bolt,这个是核心的bolt,
	主要是根据日志内容,给相关负责人发送短信和邮件
	SaveMessage2MySql:保存message信息到数据库(redis)
	这三个的核心功能实现都在cn.itcast.tom.stormlog.logMonitor.utils.MonitorHandler