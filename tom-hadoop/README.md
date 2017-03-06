# top-hadoop
mapper:读取一个文件的每一个小块,map中的方法处理的只是文本中的一行;可以一行一次输出,也可以每个map的值输出一次;
reducer:对一个map进行运算,一个map的输出对应的就是reducer的输入;
partitioner:对map中的数据进行分区(输出多个文件);
combiner:在map端对map的结果进行一次处理,(也是继承reducer,--比如过滤数据);
排序需要对象实现WritableComparable对象,然后实现compareTo方法;