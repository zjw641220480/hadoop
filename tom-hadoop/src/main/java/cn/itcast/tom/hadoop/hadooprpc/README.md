# tom-javase
	Hadoop自带RPC的使用
	使用Hadoop自带的IO进行两个进程间交互(两个节点之间交互),并且此案例使用了代理
	Bizable:只需要服务端实现此接口即可,客户端使用代理来远程操作;
	RpcClient:RPC客户端,
	RPCServer:RPC服务端,
	