# top-hadoop
	求一个订单中的最大金额,订单和金额统一起来一起作为reducer的输入
	
	思路1:
	读取一行订单作为map输出的key,金额作为输出的value;在一个key的范围内遍历找出value的最大值,这个是最慢的方式
	思路2:
	订单号和金额直接作为key,在过程中进行排序(Bean中重写compareTo方法),然后在reducer中直接把相同订单号的就认为是同一个key,遍历一个key的第一个的时候,就是该订单中对打的一个了,使用到了GroupingComparatorClass类;