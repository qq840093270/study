# 什么是Hive
## hive简介
Hive: 由Facebook开源解决海量结构化日志的数据统计工具
Hive: 基于Hadoop的一个数据仓库工具，可以将结构化的数据文件映射为一张表，并提供类SQL查询功能
## Hive本质
HSQL(Hive SQL)转换成MapReduce程序
![Image text](https://github.com/qq840093270/study/blob/master/bigData/doc/Hive/images/1.jpg)
## Hive的优缺点
### 优点
（1）操作接口采用类 SQL 语法，提供快速开发的能力（简单、容易上手）。
（2）避免了去写 MapReduce，减少开发人员的学习成本。
（3）Hive 的执行延迟比较高，因此 Hive 常用于数据分析，对实时性要求不高的场合。 
（4）Hive 优势在于处理大数据，对于处理小数据没有优势，因为 Hive 的执行延迟比较高。
（5）Hive 支持用户自定义函数，用户可以根据自己的需求来实现自己的函数
### 缺点
 1）Hive 的 HQL 表达能力有限
 （1）迭代式算法无法表达
 （2）数据挖掘方面不擅长，由于 MapReduce 数据处理流程的限制，效率更高的算法却
 无法实现。
 2）Hive 的效率比较低
 （1）Hive 自动生成的 MapReduce 作业，通常情况下不够智能化
 （2）Hive 调优比较困难，粒度较粗
