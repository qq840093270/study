# linux yum安装mysql
[yum安装mysql](https://www.cnblogs.com/xuewenlong/p/12881837.html)


# mysql 取消密码复杂度
[mysqld]  
plugin-load=validate_password.so    
validate-password=OFF    

# 查看mysql用户信息
use mysql;
select User,Host,authentication_string from user;

