#ifcfg-ens33文件
1. cd /etc/sysconfig/network-scripts 网卡路径
2. 参数详情
![Image text](images/ifcfg-ens33.png)


#linux重启网关
1. systemctl stop NetworkManager
   systemctl disable NetworkManager
   systemctl start NetworkManager
2. systemctl start network.service
3. service network restart


