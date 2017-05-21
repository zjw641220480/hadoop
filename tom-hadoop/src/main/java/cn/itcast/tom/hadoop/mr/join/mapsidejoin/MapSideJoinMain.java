package cn.itcast.tom.hadoop.mr.join.mapsidejoin;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MapSideJoinMain {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
		Configuration configuration = new Configuration();
		Job job = Job.getInstance(configuration);
		job.setJarByClass(MapSideJoinMain.class);
		
		job.setMapperClass(MapSideJoinMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(NullWritable.class);
		
		FileInputFormat.setInputPaths(job, new Path("/join/in/order.txt"));
		FileOutputFormat.setOutputPath(job, new Path("/join/out"));
		
		//指定需要缓存一个文件到所有的maptasks运行节点工作目录;
		//job.addArchiveToClassPath(archive);//缓存jar包到task运行节点的classpath中
		//job.addCacheArchive(uri);//缓存压缩包文件到task运行节点的工作目录
		//job.addCacheFile(uri);//缓存普通文件到task运行节点的工作目录
		//job.addFileToClassPath(file);//加缓存普通文件到task运行节点的工作目录;
		
		//将产品表文件缓存到task工作节点的工作目录中去;
		//job.addCacheFile(new URI("file:///D:/join/in/item.txt"));
		
		job.addCacheFile(new URI("hdfs://mini01:9000/join/in/item.txt"));
		
		//设置Reducer的数量为0
		job.setNumReduceTasks(0);
		job.waitForCompletion(true);
		
		
	}
}
