# top-hadoop
	
	一个服务端/客户端中装配多个Handler时候的处理流程,Handler中有in有out
	服务端装配顺序为in1-->out1-->out2-->in2
	服务端执行顺序为in1-->in2-->out2-->out1
	in类型的Handler执行的时候按照装配顺序来走,
	out类型的Handler执行的时候反着转配顺序来走,
	out不能都放置在in的后面,最后一个装配的只能是in类型的Handler
	
	
	
	
	