# Spark是什么

Spark 是一种基于内存的快速、通用、可扩展的大数据分析计算引擎。
----------------------------------------------------------------
# Spark 核心模块
![Spark 核心模块](https://github.com/qq840093270/study/blob/master/bigData/doc/Spark/images/spark%E6%A0%B8%E5%BF%83%E6%A8%A1%E5%9D%97.jpg)
1. **Spark Core** 中提供了 Spark 最基础与最核心的功能，Spark 其他的功能如：Spark SQL，Spark Streaming，GraphX, MLlib 都是在 Spark Core 的基础上进行扩展的
2. **Spark SQL** 是 Spark 用来操作结构化数据的组件。通过 Spark SQL，用户可以使用 SQL或者 Apache Hive 版本的 SQL 方言（HQL）来查询数据。
3. **Spark Streaming** 是 Spark 平台上针对实时数据进行流式计算的组件，提供了丰富的处理数据流的 API。
4. **Spark MLlib** 是 Spark 提供的一个机器学习算法库。MLlib 不仅提供了模型评估、数据导入等额外的功能，还提供了一些更底层的机器学习原语。
5. **Spark GraphX** 是 Spark 面向图计算提供的框架与算法库

#  Spark的三种部署模式
1. Local模式：单机调试
  - local：只启动一个线程
  - local[k]：启动k个线程
  - local[*]：启动cpu数目的线程
2. 分布式  

（1）standalone模式：

       在架构上和MapReduce1具有一致性，资源抽象为粗粒度的slot，slot决定task。

（2）Spark on yarn模式（☆）

       Spark客户端直接连接Yarn，不需要额外构建Spark集群。有yarn-client和yarn-cluster两种模式，主要区别在于：Driver程序的运行节点。

yarn-client：Driver程序运行在客户端，适用于交互、调试，希望立即看到app的输出  
yarn-cluster：Driver程序运行在由RM（ResourceManager）启动的AP（APPMaster）适用于生产环境。 

（3）Spark On Mesos模式。

       Mesos也是统一资源管理与调度系统。支持两个模式，即粗粒度模式和细粒度模式，粗粒度节省资源调度时间，细粒度节省资源。

# RDD
##  什么是 RDD

    RDD（Resilient Distributed Dataset）叫做弹性分布式数据集，是 Spark 中最基本的数据处理模型。代码中是一个抽象类，它代表一个弹性的、不可变、可分区、里面的元素可并行计算的集合。
 
1. 弹性
   - 存储的弹性：内存与磁盘的自动切换；
   - 容错的弹性：数据丢失可以自动恢复；
   - 计算的弹性：计算出错重试机制；
   - 分片的弹性：可根据需要重新分片。
2. 分布式：数据存储在大数据集群不同节点上
3. 数据集：RDD 封装了计算逻辑，并不保存数据
4. 数据抽象：RDD 是一个抽象类，需要子类具体实现
5. 不可变：RDD 封装了计算逻辑，是不可以改变的，想要改变，只能产生新的 RDD，在新的 RDD 里面封装计算逻辑
6. 可分区、并行计算      
 
## RDD 与 IO 区别
![Spark 核心模块](https://github.com/qq840093270/study/blob/master/bigData/doc/Spark/images/spark%E6%A0%B8%E5%BF%83%E6%A8%A1%E5%9D%97.jpg)
![Spark 核心模块](https://github.com/qq840093270/study/blob/master/bigData/doc/Spark/images/spark%E6%A0%B8%E5%BF%83%E6%A8%A1%E5%9D%97.jpg)
- RDD的数据处理方式类似IO流,也有装饰者模式
- RDD的数据只有在调用collect方法时,才会真正执行业务逻辑操作。之前的封装都是功能的扩展
- RDD是不保存数据的,但是IO可以临时保存一部分数据

