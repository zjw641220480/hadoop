# top-hadoop
	最简单的NIO,
	MultiplexerTimeServer:NIO中的服务端
	TimeClientHandle:NIO中的客户端
	传统IO和NIO之间的区别:(同步阻塞式IO)
		传统IO在使用的时候,在服务端和客户端都存在堵塞式方法,在服务端有server.accpt()方法进行阻塞,在客户端有input.read(),output.write()方法进行阻塞;在一个交互过程中,因阻塞而造成并发严重降低;两者建立连接之后,服务端一直在读取流中数据,只有当客户端把流关闭后,此连接断开,多个线程时候,当过多的线程处于阻塞状态,就会对性能有很大影响;同样,客户端在循环读取服务端的回复的时候,客户端也会阻塞;
		传统IO在服务端有一种方式,在循环监听中,使用线程池对每一个过来的请求进行专一性处理,也能达到很好的效果
		异步非阻塞式IO
		在NIO中,服务端会向内核注册一个监听器(selector),等待内核进行回调,替代了while循环,selector监听到客户端的事件通知,再向内核注册一个连接建立的事件,通过内核与客户端进行连接,通过三次握手实现客户端与服务端的连接成功,内核通知服务器连接建立成功,服务器再向内核注册Read监听此连接,也就是说,服务器向内核注册了三个事件(selector,连接,read),当客户端有数据发送到服务端的时候,在服务器内核里面存在TCP缓存,然后通过ByteBuffer与TCP缓存中数据进行交互,然后通知服务器可以read,服务端通过chanel进行读取;服务端不再等待客户端,每一个客户端,在服务端启动一个channel进行维护;在服务端有两种channel(ServerSocketChannel与SocketChannel),而在客户端只有一个SocketChannel,客户端正常停止之后,对服务端没有影响,客户端下次启用还可以使用之前的channel
		在内核中有一个selector,一个ServerSocketChannel,以及许多个SocketChannel;
	
	
	
	