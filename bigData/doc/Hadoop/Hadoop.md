#Hadoop是什么
Hadoop是apache 下面的一套开源软件平台
Hadoop的功能：利用服务器的集群，根据用户的业务逻辑对海里信息进行处理（存储与计算）
Hadoop的核心组件:
  HDFS(分布式文件系统)
  MAPREDUCE(分布式运行系统)
  YARN(运算资源调度系统)
  
#Hadoop特点
1.hdfs里的文件是分块（block）存储的，默认大小是128M
2.hdfs使用统一的抽象目录树管理文件，客户端不需要关心具体的文件分块
 例如：hdfs://hadoop01:port/path1/path2/file
3.抽象目录树以及分块的信息由namenode节点管理
4.具体的block存储在每一个节点上，并且每一个block可以有多个副本（dfs.replication）
5.Hdfs适合设计成一次写入多次读取的情况，不支持修改

#Hadoop写流程
![Image text](/images/图片1.png)
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


#Hadoop读流程
![Image text](/images/图片2.png)
1.client 访问 NameNode，查询元数据信息，获得这个文件的数据块位置列表，返回输入流 对象。 
2.就近挑选一台 datanode 服务器，请求建立输入流 
3.DataNode 向输入流中中写数据，以 packet 为单位 
4.关闭输入

#Hadoop 元数据管理
hdfs 的读写流程都离不开 namenode，在 namenode 维护了文件、文件块的信息，这些信息 统统称之为元数据

元数据在 hdfs 中有 3 种存在形式 
1. 存在内存中，这个最全的元数据 
2. fsimage 磁盘元数据镜像文件 
3. 最新的操作日志文件

内存的数据=fsimage+edits 文件

![Image text](/images/图片3.png)
cd/soft/data/tmp/dfs/name/current hdfsoev-iedits_0000000000000001913-0000000000000001959-oedits.xml hdfsoiv-ifsimage_0000000000000001972-pXML-ofsimage.xml

当达到某个条件后，secondary namenode 会把 namenode 上保存的 edits 和最新的 fsimage 下载到本地，
并把这些 edits 和 fsimage 进行合并，产生新的 fsimage，这整个过程把他称作checkpoint。

http://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/ hdfs-default.xml

dfs.namenode.checkpoint.check.period=60#检查触发条件是否满足的频率，60 秒 
dfs.namenode.checkpoint.dir=file://${hadoop.tmp.dir}/dfs/namesecondary 
dfs.namenode.checkpoint.edits.dir=${dfs.namenode.checkpoint.dir} #以 上 两 个 参 数 做 checkpoint 操作时，secondarynamenode 的本地工作目录
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

#Hadoop mapreduce工作流程




#Hadoop mapreduce切片机制
![Image text](/images/图片5.png)
切片机制（将待处理数据执行逻辑切片（即按照一个特定切片大小，将待处理数据划分成逻辑上的多个split，
         然后每一个split分配一个map(mapTask)并行实例处理　
         map个数：由任务切片spilt决定的，默认情况下一个split的大小就是block参与任务的文件个数决定的）

正常情况下，你不设置切片大小的时候，默认切片与 块 的大小是相同的。
