package com.bigdata.demo.hadoop.worldCount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @Classname WCMapper
 * @Description 词频统计
 * @Date 2021/1/18 18:12
 * @Created zzj
 */
public class WCMapper extends Mapper<LongWritable,Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        System.out.println(key+","+value+","+context);
        String line = value.toString();
        String[] words = line.split(",");
        for (String word:words) {
            context.write(new Text(word),new IntWritable(1));
        }
    }
}
