#linux网卡
[网卡故障排查](https://note.youdao.com/ynoteshare1/index.html?id=beae0c9a451ffa66127ffb5a0da7a063&type=note)
##ifcfg-ens33文件
1. cd /etc/sysconfig/network-scripts 网卡路径
2. 参数详情
![参数详情](https://github.com/qq840093270/study/blob/master/common/images/ifcfg-ens33.png)

##linux重启网关指令
1. systemctl stop NetworkManager  
   systemctl disable NetworkManager  
   systemctl start NetworkManager  
2. systemctl start network.service
3. service network restart


