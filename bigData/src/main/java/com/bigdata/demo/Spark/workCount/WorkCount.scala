package com.bigdata.demo.Spark.workCount

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Classname WorkCount
 * @Description TODO
 * @Date 2021/2/17 13:20
 * @Created zzj
 */
object WorkCount {
    def main(args: Array[String]): Unit = {
      //TODO 建立spark连接
      val sparkConf = new SparkConf().setMaster("local").setAppName("WorkCount")
      val sc =new SparkContext(sparkConf)
      //TODO 执行业务操作
      //1.读取文件 获取一行一行的数据 hello world
      val lines:RDD[String] = sc.textFile("bigData/testDatas/spark")
      //2.将一行数据进行拆分，形成一个一个单词
      val words:RDD[String] = lines.flatMap(_.split(" "))
      //3.将数据根据单词进行分组，便于统计
      val wordGroup:RDD[(String,Iterable[String])] = words.groupBy(word=>word)
      //4.对分组后的数据进行转换
      val wordToCount = wordGroup.map{
        case (word,list) =>(word,list.size)
      }
      //5.将转换结果采集到控制台打印出来
      val array:Array[(String,Int)] = wordToCount.collect()
      array.foreach(println)
      //TODO 关闭连接
      sc.stop()
    }

}
