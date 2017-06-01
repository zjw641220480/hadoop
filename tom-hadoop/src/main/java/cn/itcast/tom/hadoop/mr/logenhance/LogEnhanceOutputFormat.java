package cn.itcast.tom.hadoop.mr.logenhance;

import java.io.IOException;

import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * mapTask或者reducerTask在最终输出的时候,先调用OutputFormat的getRecordWriter方法拿到一个
 * RecordWriter,然后再调用RecordWriter的write方法,将数据写出
 * <p>Titile:LogEnhanceOutputFormat</p>
 * <p>Description: </p>
 * @author TOM
 * @date 2017年5月24日 下午7:47:35
 */
public class LogEnhanceOutputFormat extends FileOutputFormat<Text, NullWritable>{
	
	@Override
	public RecordWriter<Text, NullWritable> getRecordWriter(TaskAttemptContext job) throws IOException, InterruptedException {
		FileSystem fileSystem = FileSystem.get(job.getConfiguration());
		Path pathLog = new Path("/logenhance/outlog/log.txt");
		
		Path pathUrl = new Path("/logenhance/outurl/url.txt");
		FSDataOutputStream dataOutputStreamLog = fileSystem.create(pathLog);
		FSDataOutputStream dataOutputStreamUrl = fileSystem.create(pathUrl);
		
		return new LogEnhanceRecordWriter(dataOutputStreamLog,dataOutputStreamUrl);
	}

	class LogEnhanceRecordWriter extends RecordWriter<Text, NullWritable>{
		FSDataOutputStream outputStreamLog = null;
		FSDataOutputStream outputStreamUrl = null;
		public LogEnhanceRecordWriter(FSDataOutputStream outputStreamLog,FSDataOutputStream outputStreamUrl) {
			super();
			this.outputStreamLog = outputStreamLog;
			this.outputStreamUrl = outputStreamUrl;
		}
		
		@Override
		public void write(Text key, NullWritable value) throws IOException, InterruptedException {
			String result = key.toString();
			//如果要写出的数据是待爬清单文件,则写入待爬清单文件
			if(result.contains("tocrawl")){
				outputStreamUrl.write(result.getBytes());
			}else{//如果要写出的数据是增强日志,则写入增强日志文件;
				outputStreamLog.write(result.getBytes());
			}
		}

		@Override
		public void close(TaskAttemptContext context) throws IOException, InterruptedException {
			if(outputStreamLog!=null){
				outputStreamLog.close();
			}
			if(outputStreamUrl!=null){
				outputStreamUrl.close();
			}
			
		}
		
	}
}
