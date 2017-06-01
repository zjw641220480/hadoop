# top-hadoop
	以join为案例,解决数据倾斜的问题,
	数据倾斜,主要倾斜在Reducer端了,此种方式主要是在mapper端就实现部分逻辑,这样各个map端就比较均衡,reducer少了部分逻辑,避免了某一个Reducer处理数据量过大的问题(本质上还是没有解决,因为key的值是没怎么变化的;)
	MapSideJoinMain:程序启动的主程序(里面描述了需要缓存的文件的路径)
	MapSideJoinMapper:此程序只有map阶段,没有了reducer阶段
	
	
	
	
	