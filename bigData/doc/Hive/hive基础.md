# 什么是Hive

## hive简介
Hive: 由Facebook开源解决海量结构化日志的数据统计工具
Hive: 基于Hadoop的一个数据仓库工具，可以将结构化的数据文件映射为一张表，并提供类SQL查询功能
## Hive本质
HSQL(Hive SQL)转换成MapReduce程序
![Hive架构](https://github.com/qq840093270/study/blob/master/bigData/doc/Hive/images/hive架构.png)
![Hive执行流程](https://github.com/qq840093270/study/blob/master/bigData/doc/Hive/images/hive执行流程.jpg)

## hive与传统数据库区别
1. 数据存储位置。Hive是建立在Hadoop之上的，所有的Hive的数据都是存储在HDFS中的。而数据库则可以将数据保存在块设备或本地文件系统中。
2. 数据格式。Hive中没有定义专门的数据格式，由用户指定，需要指定三个属性：列分隔符，行分隔符，以及读取文件数据的方法。数据库中，存储引擎定义了自己的数据格式。所有数据都会按照一定的组织存储。
3. 数据更新。Hive的内容是读多写少的，因此，不支持对数据的改写和删除，数据都在加载的时候中确定好的。数据库中的数据通常是需要经常进行修改。
4. 执行延迟。Hive在查询数据的时候，需要扫描整个表(或分区)，因此延迟较高，只有在处理大数据是才有优势。数据库在处理小数据是执行延迟较低。
5. 索引。Hive没有，数据库有
6. 执行。Hive是MapReduce，数据库是Executor
7. 可扩展性。Hive高，数据库低
8. 数据规模。Hive大，数据库小
## Hive的优缺点
### 优点
1. 操作接口采用类 SQL 语法，提供快速开发的能力（简单、容易上手）。
2. 避免了去写 MapReduce，减少开发人员的学习成本。
3. Hive 的执行延迟比较高，因此 Hive 常用于数据分析，对实时性要求不高的场合。 
4. Hive 优势在于处理大数据，对于处理小数据没有优势，因为 Hive 的执行延迟比较高。
5. Hive 支持用户自定义函数，用户可以根据自己的需求来实现自己的函数
### 缺点
1. Hive 的 HQL 表达能力有限
   + 迭代式算法无法表达
   + 数据挖掘方面不擅长，由于 MapReduce 数据处理流程的限制，效率更高的算法却无法实现。
2. Hive 的效率比较低
   + Hive 自动生成的 MapReduce 作业，通常情况下不够智能化
   + Hive 调优比较困难，粒度较粗
   
## hive Sql
### count()函数不走MR任务
1. 通过hvie sql插入数据会在元数据库中统计数据量，但直接通过hadoop指令put上去后，hive元数据不会改变，统计数据就会错误
2. 通过hive sql中Load语法把文件放进hdfs，会改变元数据，此时统计数据会走MR任务

### 数据类型
1. hive中string类型的yyyy-MM-dd 进行时间操作会自动转换成TIMESTAMP

### DDL
![hive建表语句解析](https://github.com/qq840093270/study/blob/master/bigData/doc/Hive/images/hive建表语句解析.png)

### 外部表和内部表区别
1. hive认为外部表数据不完全拥有，删除时只会删除元数据，而不会删除真实的dhfs中的数据

### truncate只能删除内部表中数据

### hive sql排序
1. order by 全局排序，只有一个Reducer
2. sort by doc/Hive/images/sort知识点.png
![sort by知识点](https://github.com/qq840093270/study/blob/master/bigData/doc/Hive/images/sort知识点.png)
1. distribute by 分区     
2. cluster by 既具有distribute by分区功能也兼并sort by排序功能,只能升序

### 数据直接上传到分区目录，分区表跟数据产生关联
1. 上传数据后修复，使用sql语句  msck repair table xxx
2. 上传数据后添加分区

### 开启动态分区参数设置
1. 开启动态分区功能（默认 true，开启） 
  hive.exec.dynamic.partition=true 	  
2. 设置为非严格模式（动态分区的模式，默认 strict，表示必须指定至少一个分区为 静态分区，nonstrict 模式表示允许所有的分区字段都可以使用动态分区。）
   hive.exec.dynamic.partition.mode=nonstrict 	 
3. 在所有执行 MR 的节点上，最大一共可以创建多少个动态分区。默认 1000
   hive.exec.max.dynamic.partitions=1000   	
4. 在每个执行 MR 的节点上，最大可以创建多少个动态分区。该参数需要根据实际 的数据来设定。比如：源数据中包含了一年的数据，即 day 字段有 365 个值，那么该参数就需要设置成大于 365，如果使用默认值 100，则会报错。
   hive.exec.max.dynamic.partitions.pernode=100   	
5. 整个 MR Job 中，最大可以创建多少个 HDFS 文件。默认 100000
   hive.exec.max.created.files=100000 	
6. 当有空分区生成时，是否抛出异常。一般不需要设置。默认 false
   hive.error.on.empty.partition=false 	
   
### 分桶表
1. 设置参数强制分桶 set hive.enforce.bucketing = true
2. 注意reduce数量要大于或等于分桶数（buckets），否则 分桶数会跟reduce数量一样
3. 分通表是在文件中划分不同bucket，分区表是在文件夹中划分不同分区

### function
1. 函数类型分为 UDF(一进一出),UDAF(多进一出),UDTF(一进多出)
2. 语法 lateral view （UDTF）函数 临时表（XX） as XX(别名)

### 窗口函数
1. Lag（colums，num,colums（可选）） over() 向前num取colums值
2. Lead（colums，num,colums（可选）） over() 向后num取colums值
3. ntile(num) over()  把有序窗口的行分发到指定数据的组中
4. RANK()  排序相同时会重复，总数不会变
5. DENSE_RANK()  排序相同时会重复，总数会减少
6. ROW_NUMBER()  会根据顺序计算
![group_set函数](https://github.com/qq840093270/study/blob/master/bigData/doc/Hive/images/group_set函数.png)


### 压缩参数
![hive压缩参数](https://github.com/qq840093270/study/blob/master/bigData/doc/Hive/images/hive压缩参数.png)

### 文件存储格式
1. Hive 支持的存储数据的格式主要有：TEXTFILE 、SEQUENCEFILE、ORC、PARQUET
  1. TEXTFILE ：默认格式，数据不做压缩，磁盘开销大，数据解析开销大。
  2. SEQUENCEFILE：SequenceFile是Hadoop API 提供的一种二进制文件，它将数据以的形式序列化到文件中。这种二进制文件内部使用Hadoop 的标准的Writable 接口实现序列化和反序列化。
  3. ORC ：数据按行分块 每块按照列存储 ，压缩快 快速列存取，效率比rcfile高,是rcfile的改良版本，相比RC能够更好的压缩，能够更快的查询，但还是不支持模式演进。
  4. PARQUET ： Parquet能够很好的压缩，有很好的查询性能，支持有限的模式演进。但是写速度通常比较慢。这中文件格式主要是用在Cloudera Impala上面的  
  >**未启用压缩，存储效率的对比总结： ORC >	Parquet >textFile**  
  >**hive 表的数据存储格式一般选择：orc 或 parquet。压缩方式一 般选择 snappy，lzo。**  

### hive优化
1. 使用explain（explain extended）查看sql执行计划
2. set hive.fetch.task.conversion = more（有些函数走MR任务）/none（都会走MR任务）设置是否走MR任务
3. set hive.exec.mode.local.auto = true 数据量较小时，可用这个进行优化查询
4. set hive.auto.convert.join = true（默认），新版的 hive 已经对小表 JOIN 大表和大表 JOIN 小表进行了优化。小表放 在左边和右边已经没有区别。
5. Map 阶段同一 Key 数据分发给一个 reduce，当一个 key 数据过大时就倾斜了。
   1. 尽量在Map端做聚合操作 set hive.map.aggr = true 
   2. 对数据量大的key做随机数处理

  



















































## hive内嵌安装缺点
不同路径启动hive，每一个hive拥有一套自己的元数据，无法共享  

