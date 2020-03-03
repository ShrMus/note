# 问题出现的背景

电脑重装了系统，Oracle的目录和文件都在，有没有方法在已有的Oracle文件基础上重新安装Oracle系统服务呢？



# 1. 重新安装Oracle

安装在原来的目录即可，在==安装选项==这个步骤中选择==仅安装数据库软件==，**不要**选择创建和配置数据库！！！



# 2. 配置环境变量

1. 右击【我的电脑】选择【属性】
2. 选择【高级系统设置】
3. 在系统属性对话框中的【高级】选项卡中点击【环境变量...】
4. 在环境变量对话框中的【系统变量】选择【Path】项，点击【编辑】按钮
5. 将Oracle的安装目录```D:\Oracle\app\Shr\product\11.2.0\dbhome_1\bin```复制到里面，然后保存。注意这个目录复制你自己电脑上的，要复制到bin目录。



# 3. 创建实例

用管理员身份打开DOS窗口输入```oradim -new -sid ORCL```命令，其中```ORCL```是你原来的数据库实例名。

```shell
C:\Windows\system32>oradim -new -sid ORCL
Instance created.
```



# 4. 启动监听

```shel
lsnrctl start
```



# 5. 连接数据库

使用sqlplus工具连接数据库

```shell
C:\Windows\system32>sqlplus /nolog

SQL*Plus: Release 11.2.0.1.0 Production on Wed Mar 4 01:24:57 2020

Copyright (c) 1982, 2010, Oracle.  All rights reserved.

SQL> conn /as sysdba
ERROR:
ORA-01031: insufficient privileges
```

出现了**ORA-01031: insufficient privileges**的错误。

解决方法查看[ERROR:ORA-01031:insufficient privileges完美解决方法](https://blog.csdn.net/qq_41464283/article/details/89684302)。

接着再连接数据库

```
SQL> conn /as sysdba
Connected to an idle instance.
```

这下连接成功了。

接着查看一下数据库表。

```shell
SQL> select * from dba_tables;
select * from dba_tables
*
ERROR at line 1:
ORA-01034: ORACLE not available
Process ID: 0
Session ID: 0 Serial number: 0
```

出现了**ORA-01034: ORACLE not available**的错误。

解决方法：

```
SQL> startup
ORACLE instance started.

Total System Global Area 6814535680 bytes
Fixed Size                  2188688 bytes
Variable Size            3539995248 bytes
Database Buffers         3254779904 bytes
Redo Buffers               17571840 bytes
Database mounted.
Database opened.
```

再来查查scott用户中的表信息。

```
SQL> set linesize 200;
SQL> select table_name, tablespace_name, owner
  2    from dba_tables
  3   where owner = 'SCOTT';

TABLE_NAME                     TABLESPACE_NAME                OWNER
------------------------------ ------------------------------ -------------------
DEPT                           USERS                          SCOTT
EMP                            USERS                          SCOTT
SALGRADE                       USERS                          SCOTT
BONUS                          USERS                          SCOTT
```



# 6. 启动监听服务

启动监听服务，用PL/SQL Developer工具连接数据库：

```shell
C:\Windows\system32>lsnrctl start
```



# 7. 修改注册表

在注册表的**HKEY_LOCAL_MACHINE\SOFTWARE\ORACLE\KEY_OraDb11g_home1**里新建字符串，名称为**ORACLE_SID**，值为**ORCL**，值是你本机上的**数据库实例名**，这里不要照葫芦画瓢。



# 8. 总结

恢复Oracle数据库首先要清晰得知道Oracle的各种服务的作用以及计算机的系统知识，在恢复的过程中，看到Oracle安装目录中还有很多没用过的工具，例如**LSNRCTL.EXE**，**oradim.exe**，**TNSLSNR.EXE**等。还有备份和恢复演练，电脑重装过好几次了，这一次之前，电脑一直有备份的，可惜没有尝试恢复，到了需要恢复的时候以前的备份却没有用处，可能是我备份的方式不对，如果平常有演练的话也不会写下这篇文章。不只是数据库要备份，个人电脑也要常备份以及恢复演练，防止意外发生的时候又浪费大把时间。



# 参考文献

[Using ORADIM to Administer an Oracle Database Instance](https://docs.oracle.com/cd/E11882_01/win.112/e10845/create.htm#i1006533)

[谈谈WINDOWS下重装系统后oracle的恢复](http://blog.itpub.net/231499/viewspace-63722)