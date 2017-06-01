package cn.itcast.tom.hadoop.mr.friends;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.jobcontrol.JobControl;

/**
 * 不推荐,可以使用shell脚本
 * <p>Titile:ControlledJobs</p>
 * <p>Description: </p>
 * @author TOM
 * @date 2017年5月24日 下午10:31:24
 */
public class ControlledJobs {
	public static void main(String[] args) throws IOException {
		Configuration conf = new Configuration();
		Job job1 = Job.getInstance(conf);
		Job job2 = Job.getInstance(conf);
		
		//对Job1和Job2的设置;
		
		ControlledJob controlledJob1 = new ControlledJob(job1.getConfiguration());
		ControlledJob controlledJob2 = new ControlledJob(job2.getConfiguration());
		
		controlledJob2.addDependingJob(controlledJob1);
		
		JobControl jobControl = new JobControl("friends");
		jobControl.addJob(controlledJob1);
		jobControl.addJob(controlledJob2);
		
		controlledJob1.setJob(job1);
		controlledJob2.setJob(job2);
		
		Thread thread = new Thread(jobControl);
		thread.start();
		
	}
}
