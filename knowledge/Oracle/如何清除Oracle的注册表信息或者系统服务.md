2、开始－＞程序－＞Oracle - OraHome10g－＞Oracle Installation Products－＞Universal Installer 卸装所有Oracle产品。
 3、运行regedit，选择HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\，删除该路径下的所有以oracle开始的服务名称，这个键是标识Oracle在windows下注册的各种服务。 
4、选择HKEY_LOCAL_MACHINE\SOFTWARE\ORACLE，删除该oracle目录，该目录下注册着Oracle数据库的软件安装信息。 
5、选择HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Services\Eventlog\Application，删除注册表的以oracle开头的所有项目。 
6、删除环境变量path中关于oracle的内容。 鼠标右键右单击“我的电脑-->属性-->高级-->环境变量-->PATH 变量。 删除Oracle在该值中的内容。注意:path中记录着一堆操作系统的目录，在windows中各个目录之间使用分号（; ）隔开的，删除时注意。 建议：删除PATH环境变量中关于Oracle的值时，将该值全部拷贝到文本编辑器中，找到对应的Oracle的值，删除后，再拷贝修改的串，粘贴到PATH环境变量中，这样相对而言比较安全。 
7、从桌面上、STARTUP（启动）组、程序菜单中，删除所有有关Oracle的组和图标 
8、重新启动计算机，重起后才能完全删除Oracle所在目录。 
9、重启操作系统后各种Oracle相关的进程都不会加载了。这时删除Oracle_Home下的所有数据。（Oracle_Home指Oracle程序的安装目录）