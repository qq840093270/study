package com.bigdata.demo.hadoop.worldCount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * @Classname WCMapper
 * @Description 词频统计
 * @Date 2021/1/18 18:12
 * @Created zzj
 */
public class WCReducer extends Reducer<Text,IntWritable, Text, IntWritable> {
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int count = 0;
        for (IntWritable v:values) {
            count   += v.get();
        }
        context.write(key,new IntWritable(count));
    }
}
