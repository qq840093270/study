# Hadoop是什么
Hadoop是apache 下面的一套开源软件平台  
Hadoop的功能：利用服务器的集群，根据用户的业务逻辑对海里信息进行处理（存储与计算）  
Hadoop的核心组件:  
  HDFS(分布式文件系统)  
  MAPREDUCE(分布式运行系统)  
  YARN(运算资源调度系统)  
   
# Hadoop特点
1.hdfs里的文件是分块（block）存储的，默认大小是128M  
2.hdfs使用统一的抽象目录树管理文件，客户端不需要关心具体的文件分块  
 例如：hdfs://hadoop01:port/path1/path2/file  
3.抽象目录树以及分块的信息由namenode节点管理  
4.具体的block存储在每一个节点上，并且每一个block可以有多个副本（dfs.replication）  
5.Hdfs适合设计成一次写入多次读取的情况，不支持修改  

# Hadoop写流程
![HDFS的写数据流程](https://github.com/qq840093270/study/blob/master/bigData/doc/Hadoop/images/HDFS%E7%9A%84%E5%86%99%E6%95%B0%E6%8D%AE%E6%B5%81%E7%A8%8B.png)  
1. 客户端使用 FIleSystem 上传 
2. FIleSystem 与 namenode 进行通信，nn 会检查自己维护得目录树，判断当前目录是否存 在 
3. 当 namenode 正确返回后，客户端再向 namenode 请求上传第一个 block,namenode 确认 datanode 的状态，
   把健康的 datanode 集合返回给客户端，客户端会根据返回的 datanode 集 合挑选一个进行连接 
4. 客户端对每一个用于传输的节点都建立 piple 管道，并对传输第一个 block 块的数据（每 次传输的并不是一整个 block 块，
   而是一个 packet，默认大小为 64K，64K 的 packet 中每次 传输 512b 的数据时（一个 chunk）会进行一次校验）
   所以你可以把 packet 又理解成 chunk 的集合 
5. 每个 datenode 写完个 block 后再返回确认信息 
6. 所有写完了，关闭输出流 
7. 整个完成后最后通知 namdenode 完成数据上传  
总结：1个block（块，默认128M） 会分成多个packet（包，默认64KB）,会分成多个chunk（默认512b）  

# Hadoop读流程
![HDFS的读数据流程](https://github.com/qq840093270/study/blob/master/bigData/doc/Hadoop/images/HDFS%E7%9A%84%E8%AF%BB%E6%95%B0%E6%8D%AE%E6%B5%81%E7%A8%8B.png)  
1. client 访问 NameNode，查询元数据信息，获得这个文件的数据块位置列表，返回输入流 对象。 
2. 就近挑选一台 datanode 服务器，请求建立输入流 
3. DataNode 向输入流中中写数据，以 packet 为单位 
4. 关闭输入

# Hadoop 元数据管理
hdfs 的读写流程都离不开 namenode，在 namenode 维护了文件、文件块的信息，这些信息 统统称之为元数据

元数据在 hdfs 中有 3 种存在形式 
1. 存在内存中，这个最全的元数据 
2. fsimage 磁盘元数据镜像文件 
3. 最新的操作日志文件 

内存的数据=fsimage+edits 文件  

![元数据](https://github.com/qq840093270/study/blob/master/bigData/doc/Hadoop/images/%E5%85%83%E6%95%B0%E6%8D%AE.jpg)  
cd/soft/data/tmp/dfs/name/current hdfsoev-iedits_0000000000000001913-0000000000000001959-oedits.xml hdfsoiv-ifsimage_0000000000000001972-pXML-ofsimage.xml

当达到某个条件后，secondary namenode 会把 namenode 上保存的 edits 和最新的 fsimage 下载到本地， 
并把这些 edits 和 fsimage 进行合并，产生新的 fsimage，这整个过程把他称作checkpoint。  



dfs.namenode.checkpoint.check.period=60#检查触发条件是否满足的频率，60 秒   
dfs.namenode.checkpoint.dir=file://${hadoop.tmp.dir}/dfs/namesecondary   
dfs.namenode.checkpoint.edits.dir=${dfs.namenode.checkpoint.dir}  # 以 上 两 个 参 数 做 checkpoint 操作时，secondarynamenode 的本地工作目录  
dfs.namenode.checkpoint.max-retries=3   #最大重试次数   
dfs.namenode.checkpoint.period=3600     #两次 checkpoint 之间的时间间隔 3600 秒   
dfs.namenode.checkpoint.txns=1000000    #两次 checkpoint 之间最大的操作记录  


1. SecondaryNameNode 会定时的和 NameNode 通信，请求其停止使用 edits 文件， 
   暂时将 新的写操作写到一个新的文件 edits.new 上，这个操作是瞬时完成的，上层的写日志函数完 全感觉不到差别
2. econdaryNameNode 通过 HTTP 的 get 方法从 NameNode 上获取到 fsimage 和 edits 文件，
   SecondaryNameNode 将 fsimage 文件载入内存中，逐一执行 edits 文件中的事务，创建新的 合并后的 fsimage 文件，
   使得内存中的 fsimage 保存最新。
3. SecondaryNameNode 执行完 2 之后，会通过 post 方法将新的 fsimage 文件发送到 NameNode 节点上
4. NameNode 将从 SecondaryNameNode 接收到的新的 fsimage 文件保存为.ckpt 文件
5. NameNode 重新命名 fsimage.ckpt 为 fsimage 替换旧的 fsimage 文件，同时将 edits.new 替 换 edits 文件，
   通过这个过程 edits 文件就变小了

# Hadoop mapreduce工作流程
一个完整的 mapreduce 程序在分布式运行时有三类实例进程： 
  
**MRAppMaster：负责整个程序的过程调度及状态协调**    
 
**mapTask：负责 map 阶段的整个数据处理流程**  
 
**ReduceTask：负责 reduce 阶段的整个数据处理流程**    
![job工作机制](https://github.com/qq840093270/study/blob/master/bigData/doc/Hadoop/images/%E5%B7%A5%E4%BD%9C%E6%9C%BA%E5%88%B6.jpg)  
1. 一个 mr 程序启动的时候，最先启动的是 MRAppMaster，MRAppMaster 启动后根据本次 job 的描述信息，计算出需要的 maptask 实例数量，然后向集群申请机器启动相应数量 的 maptask 进程 （这里先理解成一个文件一个 maptask）
2. maptask 进程启动之后，根据给定的数据切片范围进行数据处理，主体流程为：  
     -  利用客户指定的 inputformat 来获取数据，形成输入 K，V 对 
     -  将输入 KV 对传递给客户定义的 map()方法，做逻辑运算，并将 map()方法输出的 KV 对收集到缓存 
     -  将缓存中的 KV 对按照 K 分区排序后不断溢写到磁盘文件 
 3. MRAppMaster 监控到所有 maptask 进程任务完成之后，会根据客户指定的参数启动相应 数量的 reducetask 进程，并告知 reducetask 进程要处理的数据范围（数据分区） 
 4. Reducetask 进程启动之后，根据 MRAppMaster 告知的待处理数据所在位置，从若干台 maptask 运行所在机器上获取到若干个 maptask 输出结果文件，并在本地进行重新归并 排序，然后按照相同 key 的 KV 为一个组，调用客户定义的 reduce()方法进行逻辑运算， 并收集运算输出的结果 KV，然后调用客户指定的 outputformat 将结果数据输出到外部存储

# 提交任务流程与 Shuffle 流程
1. maptask 收集我们的 map()方法输出的 kv 对，放到内存缓冲区中 
2. 从内存缓冲区不断溢出本地磁盘文件，可能会溢出多个文件 
3. 多个溢出文件会被合并成大的溢出文件 
4. 在溢出过程中，及合并 Combine 的过程中，都要调用 partitoner 进行分组和针对 key 进行排序(compare) 
5. reducetask 根据自己的分区号，去各个 maptask 机器上取相应的结果分区数据 
6. reducetask 会取到同一个分区的来自不同 maptask 的结果文件，reducetask 会将这些 文件再进行合并（归并排序） 
7. 合并成大文件后，shuffle 的过程也就结束了，后面进入 reducetask 的逻辑运算过程 （从文件中取出一个一个的键值对 group，调用用户自定义的 reduce()方法） 
8. 缓冲区的大小可以通过参数调整, 参数：[mapreduce.task.io.sort.mb 默认100M](http://hadoop.apache.org/docs/stable/hadoop-mapreduce-client/hadoop-mapreduce-client-core/mapred-default.xml) 

# Hadoop mapreduce切片机制
![切片机制](https://github.com/qq840093270/study/blob/master/bigData/doc/Hadoop/images/%E5%88%87%E7%89%87%E6%9C%BA%E5%88%B6.png)   
切片机制（将待处理数据执行逻辑切片（即按照一个特定切片大小，将待处理数据划分成逻辑上的多个split,然后每一个split分配一个map(mapTask)并行实例处理） 
map个数：由任务切片spilt决定的，默认情况下一个split的大小就是block参与任务的文件个数决定的  
正常情况下，你不设置切片大小的时候，默认切片与 块 的大小是相同的。  
