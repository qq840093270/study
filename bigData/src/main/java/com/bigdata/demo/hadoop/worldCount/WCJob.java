package com.bigdata.demo.hadoop.worldCount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputCommitter;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.File;
import java.io.IOException;

/**
 * @Classname WCMapper
 * @Description 词频统计
 * @Date 2021/1/18 18:12
 * @Created zzj
 */
public class WCJob {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf = new Configuration();
        Job job = Job.getInstance (conf);
        job.setJarByClass(WCJob.class);
        //指定maper与Reduce类型
        job.setMapperClass(WCMapper.class);
        job.setReducerClass(WCReducer.class);
        //指定输出类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        //指定存放文件路径
        FileInputFormat.setInputPaths(job,new Path("/worlds.txt"));
        FileOutputFormat.setOutputPath(job,new Path("/wc/output/"));

        boolean b =  job.waitForCompletion(true);
        System.exit(b?0:-1);
    }

}
