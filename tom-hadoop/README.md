# top-hadoop
mapper:读取一个文件的每一个小块,
partitioner:对map中的数据进行分区(输出多个文件);
reducer:对一个map进行运算
combiner:在map端对map的结果进行一次处理,(也是继承reducer,--比如过滤数据);

