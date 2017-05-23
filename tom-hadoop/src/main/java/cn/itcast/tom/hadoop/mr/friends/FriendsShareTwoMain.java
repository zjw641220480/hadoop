package cn.itcast.tom.hadoop.mr.friends;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 
 * <p>
 * Titile:FriendsShareTwoMain
 * </p>
 * <p>
 * Description:使用两个MapperReducer进行构建
 * </p>
 * 
 * @author TOM
 * @date 2017年5月22日 下午8:07:40
 */
public class FriendsShareTwoMain {
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException, URISyntaxException {
		
		Job job = Job.getInstance(new Configuration());
		
		
		/*job.setMapperClass(FriendsShareMapper.class);
		job.setReducerClass(FriendsShareReducer.class);*/
		
		job.setMapperClass(FriendsShareMapperTwo.class);
		job.setReducerClass(FriendsShareReducerTwo.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setJarByClass(FriendsShareTwoMain.class);

		// 指定job的输入原始文件所在目录
		FileInputFormat.setInputPaths(job, new Path("/friends/out/part-r-00000"));
		// 指定输出结果所在目录
		FileOutputFormat.setOutputPath(job, new Path("/friends/outfinally"));

		// 提交,将job中配置的相关参数,以及job所用的java类所在的jar包,提交给yarn去运行;
		boolean flag = job.waitForCompletion(true);
		if (!flag) {
			FileSystem fileSystem = FileSystem.get(new URI("hdfs://mini01:9000"), new Configuration(), "hadoop");
			fileSystem.delete(new Path("/friends/out"), true);
		}
	}
}
