# CDH安装教程
[CDH安装教程](https://blog.csdn.net/u010520724/article/details/110635922)

## CDH安装遇到的问题

###  未在已配置的存储库中找到任何 parcel。尝试在更多选项下添加一个自定义存储库。否则，您可能只能继续使用包。
注意： Parcle默认存储目录 /opt/cloudera/parcels  
本地存放目录： 默认/opt/cloudera/parcel-repo   
重启server和agent   

### CDH中JVM堆内存较低会报 no GCs detected
要配置SCM进程的最大堆大小，请执行以下操作：  
1）在/etc/default/cloudera-scm-server文件中编辑CMF_JAVA_OPTS属性。  
2）更改文件的最后一行：  
export CMF_JAVA_OPTS =“-Xmx2G -XX：MaxPermSize = 256m ...”  
3）要将更多堆内存配置到Cloudera Manager，请将-Xmx2G更改为更大的值。  
例如，-Xmx4G。  
注意：-Xmx2G选项指定2 GB的RAM。  
4）重启CM服务 systemctl restart cloudera-scm-server   
  
