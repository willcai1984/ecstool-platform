# 命令执行引擎帮助手册ExecCommand(string) #


## （一）命令格式 ##

```
IP1,IP2,…:command_type{@command1@}{@command2@}…

IP1:IP2:…:command_type{@command1@}{@command2@}…

(:传递执行)
```



## （二）分类举例 ##

```

1. 执行linux shell命令（保持连接执行）：

IP1,IP2,…:linux_shell{@shell1@}{@shell2@}…

例：42.121.98.7:linux_shell{@cd ..&&ll@}{@ps -aux@}

 

2. 执行windows命令（每条初始化连接执行）：

IP1,IP2,…:win_cmd{@dos1@}{@dos2@}…

例：42.121.98.70:win_cmd{@cd ..&&dir@}

 

3. 打开文件（windows）：

IP1,IP2,…:open_file{@path1@}{@path2@}…

例：127.0.0.1:open_file{@D:\[CN]MessengerGuide.ppt@}{@D:\Office 配置指南\outlookurl.txt@}

127.0.0.1:open_file{@D:\[CN]MessengerGuide.bat param1@}

 

4. 打开网站（windows）：

IP1,IP2,…:open_web{@url1@}{@url2@}…

例：127.0.0.1:open_web{@www.baidu.com@}{@www.163.com@}

 

5. 打开可执行程序（windows）：

IP1,IP2,…:open_exe{@path1|param2@}{@path1|param2@}…

例：127.0.0.1:open_exe{@C:\Windows\System32\notepad.exe|D:\Office 配置指南\outlookurl.txt@}

127.0.0.1:open_exe{@D:\[CN]MessengerGuide.bat;param1@}

 

6. 杀掉进程（windows）：

IP1,IP2,…:kill_proc{@proc1@}{@proc2@}…

例：127.0.0.1:kill_proc{@notepad@}

 

7. 执行Windows API：

IP1,IP2,…:win_api{@proc1|send_text_xy|x|y|string@}{@proc2|send_text|id|string@}{@proc3|click|id@}{@proc4|click_xy|x|y@}{@proc5|send_key|key@}{@proc6|get_byid|id@}{@proc7|set_max@}{@proc8|set_min@}{@proc9|set_normal@}{@proc9|set_front@}{@proc10|get_text|id@}…

例：127.0.0.1:win_api{@notepad|send_text|15|wugang,hello world!@}{@notepad|get_text|15@}

 

8. 打开Lua脚本：

IP1,IP2,…:open_lua{@path1@}{@path2@}…

例：127.0.0.1:open_lua{@D:\Sample.lua@}

 

9. 执行Lua脚本：

IP1,IP2,…:exec_lua{@path1@}{@path2@}…

例：127.0.0.1:exec_lua{@D:\Sample.lua@}

 

10. 停止执行Lua脚本：

IP1,IP2,…:stop_lua{@@}

例：127.0.0.1:stop_lua{@@}

 

11. 得到连接状态的服务器（在线）：

IP1,IP2,…:get_connect_servers{@all@}{@server@}{@client@}{@linux@}…

例：127.0.0.1:get_connect_servers{@all@}

 

12：得到未连接状态的服务器（离线）：

IP1,IP2,…:get_noconnect_servers{@@}

例：127.0.0.1: get_noconnect_servers{@@}

 

13：得到所有服务器IP：

IP1,IP2,…:get_all_servers{@@}

例：127.0.0.1: get_all_servers{@@}

 

14. 连接所有服务器：

IP1,IP2,…:connect_all_servers{@@}

例：127.0.0.1: connect_all_servers{@@}

 

15. 连接单个服务器：

IP1,IP2,…:connect_one_server{@IP_1@}{@IP_2@}…

例：127.0.0.1:connect_one_server{@42.121.98.7@}{@42.121.98.70@}

 

16. 断开所有服务器：

IP1,IP2,…:close_all_servers{@@}

例：127.0.0.1: close_all_servers{@@}

 

17. 断开单个服务器：

IP1,IP2,…:close_one_server{@IP_1@}{@IP_2@}…

例：127.0.0.1:close_one_server{@42.121.98.7@}{@42.121.98.70@}

 

18. 得到单个服务器连接状态：

IP1,IP2,…:get_status_server{@IP_1@}{@IP_2@}…

例：127.0.0.1: get_status_server{@42.121.98.7@}{@42.121.98.70@}

 

19. 删除一个服务器（彻底删除）：

IP1,IP2,…:delete_one_server{@IP_1@}{@IP_2@}…

例：127.0.0.1: delete_one_server {@10.34.130.45@}

 

20. 检查windows文件是否存在（返回大小）：

IP1,IP2,…:check_windows_file{@path1|minSize1|timeout1@}{@path2|minSize2|timeout2@}…

例：127.0.0.1: check_windows_file{@ D:\Office 配置指南\outlookurl.txt|1|100@}

 

21. 检查windows目录是否存在：

IP1,IP2,…:check_windows_directory{@path1@}{@path2@}…

例：127.0.0.1: check_windows_directory {@ D:\Office 配置指南@}

 

22. 检查linux文件是否存在（返回大小）：

IP1,IP2,…:check_linux_file{@path1|minSize1|timeout1@}{@path2|minSize2|timeout2@}…

例：42.121.98.7: check_linux_file{@/root/wugang/wug.txt|10|100@}

 

23. 检查linux目录是否存在：

IP1,IP2,…:check_linux_directory{@path1@}{@path2@}…

例：42.121.98.7: check_linux_ directory {@~/home@}

 

24. 得到windows子目录（深度n）：

IP1,IP2,…:get_windows_directories{@n|path1 @}{@n|path2 @}…

例：127.0.0.1: get_windows_directories{@2|D:\ @}

 

25. 得到windows子文件（深度n）：

IP1,IP2,…:get_windows_files{@n|path1 @}{n|@path2 @}…

例：127.0.0.1: get_windows_filies{@2|D:\ @}

 

26. 发送windows文件（私网）：

IP1,IP2,…:send_windows_file{@filePath1>IP1?IP1_dir1?IP1_dir2?…|IP2?IP2_dir1?IP2_dir2?…|…@}{@filePath2>IP1?IP1_dir1?IP1_dir2?…|IP2?IP2_dir1?IP2_dir2?…|…@}…

例：127.0.0.1: send_windows_file{@ F:\python核心编程.pdf>127.0.0.1?C:\CWM\firefox fire?C:\CWM@}

 

26. 发送net文件（公网）：

IP1,IP2,…:send_net_file{@filePath1>IP1?IP1_dir1?IP1_dir2?…|IP2?IP2_dir1?IP2_dir2?…|…@}{@filePath2>IP1?IP1_dir1?IP1_dir2?…|IP2?IP2_dir1?IP2_dir2?…|…@}…

例：42.121.98.70: send_net_file{@ F:\python核心编程.pdf>C:\Debug?C:\Debug\Docs@}

 

27. 下载windows文件：

IP1,IP2,…:download_windows_file{@IP_1>filePath1>IP1_dir1?IP2_dir2? … @}{@ IP_2>filePath2|IP1_dir1?IP1_dir2?…@}…

例:127.0.0.1: download_windows_file{@42.121.98.70>C:\Debug\python核心编程.pdf>C:\CWM\firefox fire?C:\CWM@}

 

28. 发送Linux文件：

IP1,IP2,…:send_linux_file{@filePath1>IP1;IP1_dir1?IP1_dir2?…|IP2;IP2_dir1?IP2_dir2?…|…@}{@filePath2>IP1;IP1_dir1?IP1_dir2?…|IP2;IP2_dir1?IP2_dir2?…|…@}…

例：127.0.0.1: send_linux_file{@C:\CWM\wug.txt>42.121.98.7;/home?/root/wugang@}

 

29. 下载Linux文件：

IP1,IP2,…:download_linux_file{@IP_1|file1|IP1_dir1?IP1_dir2?…@}{@IP_2|file2|IP1_dir1?IP1_dir2?…@}…

例:127.0.0.1: download_linux_file{@42.121.98.7|/root/wug.txt|C:\CWM?C:\CWM\firefox fire@}

 

30. 关闭所有文件发送线程：

IP1,IP2,…: close_send_file{@@}

例：127.0.0.1: close_send_file{@@}

 

31. 关闭所有文件下载线程：

IP1,IP2,…: close_download_file{@@}

例：127.0.0.1: close_download_file{@@}

 

32. 替换文本字符串右边的值（逐行）：

IP1,IP2,…:replace_right{@file1|left1|right1@}{@file1|left1|right1@}…

例：127.0.0.1: replace_right{@ C:\CWM\wug.txt|xxx=|www.baidu.com@}

 

33. 替换文本字符串：

IP1,IP2,…:replace_text{@file1|old1|new1@}{@file2|old2|new2@}…

例：127.0.0.1: replace_text{@ C:\CWM\wug.txt|yyy=|zzz=@}

 

34. 添加定时任务：

IP1,IP2:con_add[[min|hour|day|month|week|command]]…

例：127.0.0.1:cron_add[[*|*|*|*|*|10.34.130.44:exec_lua{@Groups/Project/email.lua@}]]

 

35. 删除定时任务：

IP1,IP2:con_del[[min|hour|day|month|week|command]]…

例：127.0.0.1:cron_del[[*|*|*|*|*|10.34.130.44:get_all_servers{@@}]]

 

36. 发送邮件：

IP1,IP2:send_mail{@to1;to2;to3|cc1;cc2|subject|body|true@}…

例：127.0.0.1:send_mail{@workhard_smile@163.com;wugang@nhn.com|wugang@nhn.com|测试|<html>测试test</html>|true@}

 

37. 查看文本文件：

IP1,IP2: view_file{@file1@}{@file2@}…

例：127.0.0.1:view_file{@crontab.lst@}{@C:\root.txt@}

 

38. 强制等待执行CMD：

IP1,IP2:run_cmd{@command1@}{@command2@}…

例：127.0.0.1: run_cmd{@ping –n 10 127.0.0.1@}…

 

39. 输出窗口：

IP1,IP2: out_put{@content1@}{@ content1@}…

例：127.0.0.1: out_put{@Hello world!@}
```