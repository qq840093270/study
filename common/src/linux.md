# linux网卡
[网卡故障排查](https://note.youdao.com/ynoteshare1/index.html?id=beae0c9a451ffa66127ffb5a0da7a063&type=note)
## ifcfg-ens33文件
1. cd /etc/sysconfig/network-scripts 网卡路径
2. 参数详情
> TYPE="Ethernet"		#网卡类型（通常是Ethemet以太网）  
> PROXY_METHOD="none"	#代理方式：为关闭状态  
> BROWSER_ONLY="no"	#只是浏览器：否  
> BOOTPROTO="static"	#网卡的引导协议【static：静态IP  dhcp：动态IP   none：不指定，不指定容易出现各种各样的网络受限】  
> DEFROUTE="yes"		#默认路由  
> IPV4_FAILURE_FATAL="no"		#是否开启IPV4致命错误检测  
> IPV6INIT="yes"		#IPV6是否自动初始化：是（现在还未用到IPV6，不会有任何影响）  
> IPV6_AUTOCONF="yes"	#IPV6是否自动配置：是（现在还未用到IPV6，不会有任何影响）  
> IPV6_DEFROUTE="yes"	#IPV6是否可以为默认路由：是（现在还未用到IPV6，不会有任何影响）  
> IPV6_FAILURE_FATAL="no"		#是否开启IPV6致命错误检测  
> IPV6_ADDR_GEN_MODE="stable-privacy"	#IPV6地址生成模型  
> NAME="ens33"		#网卡物理设备名称  
> UUID="ab60d501-535b-49f5-a76b-3336a4120f64"#通用唯一识别码，每一个网卡都会有，不能重复，否则两台linux机器只有一台可上网  
> DEVICE="ens33"		#网卡设备名称，必须和‘NAME’值一样
> ONBOOT="yes"		#是否开机启动，要想网卡开机就启动或通过 `systemctl restart network`控制网卡,必须设置为 `yes`  
> IPADDR=192.168.137.129		# 本机IP  
> NETMASK=255.255.255.0		#子网掩码  
> GATEWAY=192.168.137.2		#默认网关  
> DNS1=8.8.8.8#  
> DNS2=8.8.8.5#  
> ZONE=public#  

## linux重启网关指令
1. systemctl stop NetworkManager  
   systemctl disable NetworkManager  
   systemctl start NetworkManager  
2. systemctl start network.service
3. service network restart


