# 应用于全自动化测试体系的实现实例(基于SVN跨平台敏捷项目) #

## 1．    期望实现效果 ##
### 【 用逻辑脚本和指令控制各个服务器、服务器上的程序和脚本7\*24小时自动、并行、有序地工作，无人值守，本地资源代码一“丢”，报告和邮件会及时自动来找你汇报，接受检查。应用如自动化测试、自动部署维护等。。】 ###

开发会将用于测试的源代码版本转存于SVN的一个路径下。

无人值守自动实施工作：每天固定几个时间段检查一次是否有新的需要测试的版本。

如果有： 【则下载源代码到各个Windows和Linux编译环境平台进行编译，编译成功后部署到相应的Windows和Linux测试服务器，然后启动自动化测试脚本或程序，测试完成后发送Summary邮件和测试报告网址。】

如果没有或者过程中有错：【中止测试，发送Summary邮件和错误日志。】

即，中控服务器自动定时任务：检查版本-->多平台多服务器同时编译-->多服务器同时部署-->调用启动测试-->测试结束展现测试结果（如自动发送邮件网页等）

手动工作：本地编写自动化测试代码+维护一套自动控制脚本-->接收邮件、检查测试结果

总之，自动化中控服务器平台执行控制脚本逻辑、调度操控各个可连接服务器有序工作（横向纵向可继续延伸至其他中控集群），本地工作机可一键生成同步资源到中控服务器（基于IP），一键增删查中控服务器定时任务列表（相对路径，控制脚本调度）。如图所示本地资源列表界面：

![http://img.my.csdn.net/uploads/201302/01/1359700078_6184.jpg](http://img.my.csdn.net/uploads/201302/01/1359700078_6184.jpg)
![http://img.my.csdn.net/uploads/201302/01/1359700079_8232.jpg](http://img.my.csdn.net/uploads/201302/01/1359700079_8232.jpg)
![http://img.my.csdn.net/uploads/201302/01/1359700004_6203.jpg](http://img.my.csdn.net/uploads/201302/01/1359700004_6203.jpg)
[ecs](http://www.itmal.com/images/img/a6.jpg)

## 2.   全局变量和公共函数 ##

```
wip="10.33.33.63"  --windows

lip="10. 33.33.44"  --linux

lhost="10. 33.33.62"  --中控

tlpath="/tmp/"

twpath="C:\\ECSTOOL\\Temp\\"

dates=os.date("%Y-%m-%d")

cfile="C:\\ECSTOOL\\Groups\\CWMCMS\\0CheckVersion\\current"

tfile="C:\\ECSTOOL\\Groups\\CWMCMS\\0CheckVersion\\test"

-----------------日志记录---------------------------

LogPath="C:\\ECSTOOL\\Log\\CMS."..dates..".log"

function WriteLog(str)

local ft = assert(io.open(LogPath, "a")) 

ft:write(str.."<br />\r\n") 

ft:close()

end

----------------sleep(n)和sleeping(n)------------------

--利用系统ping命令等待时间

function sleep(n) 

  if n > 0 then os.execute("ping -n " .. tonumber(n + 1) .. " localhost > NUL") end 

end

function sleeping(n)

  local i = 0

  local x = 100000*n

  while i < x do

     i = i + 1

  end

end

---------------------connect(IP)返回1成功0失败--------------

--建立连接

function connect(tip)

 local sip="127.0.0.1"

 --连接服务器

 ExecCommand(sip..":connect_one_server{@"..tip.."@}")

 local count=0

 while count<1000 do 

  sleep(1)

  ExecCommand(sip..":get_status_server{@"..tip.."@}")

  --寻找子字符串，可用正则

  local s, e = string.find(string.lower(MyForm.hReturn[MyForm.hReturn.Count-1]),"true",1)

  if (e ~= nil) and (s ~= nil) and (e > s) then 

     x = string.sub(MyForm.hReturn[MyForm.hReturn.Count-1], s, e) 

     return 1

  end 

  count = count+1

 end

 return 0

end

------------------------------------------------------------------------------------------------
```

## 3.   版本检查和编译实现函数 ##

开发会将用于测试的源代码版本转存于SVN的一个路径下。定时检查这个路径下是否有新增版本，有的话下载—>启动测试流程—>测试结束标记已测试（用于下次检查避免重复测试），如果需要回归测试只需编辑标记测试文件。0CheckVersion\current和0CheckVersion\test文件

编译打包脚本和开发一起协调完成，编译调度脚本为1Build\ packagebuild\_new.sh和1Build\ packagebuild\_new.sh，安装部署脚本为2Setup\setup.sh和2Setup\setup.bat，在检查版本需要测试后，均需要从中控服务器分发到Linux和Windows编译服务器和测试服务器。

![http://img.my.csdn.net/uploads/201302/01/1359700004_1949.jpg](http://img.my.csdn.net/uploads/201302/01/1359700004_1949.jpg)
[ecs](http://www.itmal.com/images/img/a7.jpg)

```
-------------------------------------------CheckVersion(style) -----------------------

-------------------------------------------- style:both;win;linux----------------------

function CheckVersion(style)

local url="http://svn.cubrid.org/cubridtools/cm-server/branches/"

local build="C:\\ECSTOOL\\Groups\\CWMCMS\\1Build\\"

local setpath="C:\\ECSTOOL\\Groups\\CWMCMS\\2Setup\\"

local lbuild="packagebuild_new.sh"

local wbuild="packagebuild_new.bat"

local version=nil

ExecCommand(lhost..":open_file{@svn list "..url..">"..cfile.."@}")

sleep(10)

--读已测版本列表

local ft = assert(io.open(tfile, "r")) 

local tt = ft:read("*all") 

ft:close()

tt=string.gsub(string.gsub(tt,"\n",""),"/","")

--读当前版本列表

local fc = assert(io.open(cfile, "r")) 

local tc = fc:read("*all") 

fc:close()

tc=string.gsub(string.gsub(tc,"\n",""),"/","")

--比较获取需要测试版本

if tc == tt then 

  return version,tc

else

  tt=string.gsub(string.gsub(tt,"%.","%%."),"%-","%%-")

  version=string.gsub(tc,tt,"")

  --windows和linux同时操作

  if style == "both" then

    --删除上次脚本

    ExecCommand(wip..":win_cmd{@cd "..twpath.." &&del /F /Q "..wbuild.." && del /F /Q setup.bat@}")

    ExecCommand(lip..":linux_shell{@cd "..tlpath.." &&rm -fr "..lbuild.." setup.sh@}")

    sleep(5)

    --分发脚本build

    ExecCommand(lhost..":send_windows_file{@"..build..wbuild..">"..wip.."?"..twpath.."?@}")

    sleep(1)

    ExecCommand(lhost..":send_linux_file{@"..build..lbuild..">"..lip..";"..tlpath.."?@}")

    sleep(10)

    --分发脚本setup

    ExecCommand(lhost..":send_windows_file{@"..setpath.."setup.bat>"..wip.."?"..twpath.."?@}")

    sleep(1)

    ExecCommand(lhost..":send_linux_file{@"..setpath.."setup.sh>"..lip..";"..tlpath.."?@}")

    sleep(10)

    --迁出编译

    ExecCommand(wip..":open_exe{@"..twpath..wbuild..";"..version.."@}")

    ExecCommand(lip..":linux_shell{@cd "..tlpath.." &&sh "..lbuild.." "..version.."@}")

    sleep(10)

  --只操作windows

  elseif style == "win" then

    --删除上次脚本

    ExecCommand(wip..":win_cmd{@cd "..twpath.." &&del /F /Q "..wbuild.."@}")

    sleep(5)

    --分发脚本

    ExecCommand(lhost..":send_windows_file{@"..build..wbuild..">"..wip.."?"..twpath.."?@}")

    sleep(10)

    --迁出编译

    ExecCommand(wip..":open_exe{@"..twpath..wbuild..";"..version.."@}")

    sleep(10)

  --只操作linux

  else

    --删除上次脚本

    ExecCommand(lip..":linux_shell{@cd "..tlpath.." &&rm -fr "..lbuild.."@}")

    sleep(5)

    --分发脚本

    sleep(1)

    ExecCommand(lhost..":send_linux_file{@"..build..lbuild..">"..lip..";"..tlpath.."?@}")

    sleep(10)

    --迁出编译

    ExecCommand(lip..":linux_shell{@cd "..tlpath.." &&sh "..lbuild.." "..version.."@}")

    sleep(10)

  end

  return version,tc

end

end

------------------------------------------------------------------------------------------------
```

## 4.   启动执行测试函数 ##

包括测试环境准备（打包资源文件Temp\TestReady.zip分发到Linux和Windows服务器后解压覆盖），自动化测试程序调用方式：ExecCommand(lhost..":open\_file{@cd C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\testexe-windows && CMSAutoTest.exe -p "..ver.." -h "..wip.."@}")

```
---------------------------------------ExecuteTest(ver,style) -------------------------

----------------------------------------style:both;win;linux----------------------------

function ExecuteTest(ver,style)
--变量
local lpath="/root/CUBRID/bin/"
local wpath="C:\\CUBRID\\bin\\"
local rpath="C:\\ECSTOOL\\Temp\\TestReady.zip"
local rar="%ProgramFiles%/WinRAR/WinRAR.exe"
local file="TestReady.zip"
ExecCommand(lhost..":kill_proc{@cmd@}{@CMSAutoTest@}")
sleep(5)
if style == "both" then
  ---------------------测试环境准备-----------------------
  WriteLog("["..os.date("%x %X").."] Set the testing ENV...")
  ExecCommand(wip..":win_cmd{@cd /d "..wpath.." && cubrid.exe service restart@}")
  ExecCommand(lip..":linux_shell{@cubrid service restart && cubrid server start demodb@}")
  sleep(10)
  ExecCommand(wip..":win_cmd{@cd /d "..wpath.." && cubrid.exe server start demodb@}")
  sleep(5)
  --清理压缩包
  ExecCommand(wip..":win_cmd{@cd /d "..wpath.." && del /f /q "..file.."@}")
  ExecCommand(lip..":linux_shell{@cd "..lpath.." && rm -fr "..file.."@}")
  sleep(5)
  --分发压缩包
  ExecCommand(lhost..":send_windows_file{@"..rpath..">"..wip.."?"..wpath.."?@}")
  sleep(10)
  ExecCommand(lhost..":send_linux_file{@"..rpath..">"..lip..";"..lpath.."?@}")
  sleep(10)
  --解压
  ExecCommand(wip..":win_cmd{@cd /d "..wpath.." && \""..rar.."\" x -r -o+ "..file.." .@}")
  ExecCommand(lip..":linux_shell{@cd "..lpath.." && unzip -o "..file.."@}")
  sleep(10)
  -----------------------执行测试-----------------------------
  WriteLog("["..os.date("%x %X").."] Excute testing...")
  --ExecCommand(lhost..":open_file{@cd /d C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\testexe-windows && CMSAutoTest.exe -p "..ver.." -h "..wip.."@}")
  ExecCommand(lhost..":open_exe{@C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\wincmsexec.bat;\""..ver.."\" "..wip.."@}")
  sleep(1)
  --ExecCommand(lhost..":open_file{@cd /d C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\testexe-linux && CMSAutoTest.exe -p "..ver.." -h "..lip.."@}")
  ExecCommand(lhost..":open_exe{@C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\linuxcmsexec.bat;\""..ver.."\" "..lip.."@}")
  sleep(1)
  ------------------------清理环境-----------------------------
  ExecCommand(wip..":win_cmd{@cd /d "..twpath.." && del /f /q "..ver.." && del /f /q CMS* && del /f /q setup.bat@}")
  ExecCommand(lip..":linux_shell{@cd "..tlpath.." && rm -fr "..ver.." CMS* setup.sh@}")
  WriteLog("["..os.date("%x %X").."] Clear testing ENV...")
elseif style == "win" then 
  ---------------------测试环境准备-----------------------
  WriteLog("["..os.date("%x %X").."] Set the testing ENV...")
  ExecCommand(wip..":win_cmd{@cd /d "..wpath.." && cubrid.exe service restart@}")
  sleep(10)
  ExecCommand(wip..":win_cmd{@cd /d "..wpath.." && cubrid.exe server start demodb@}")
  sleep(5)
  --清理压缩包
  ExecCommand(wip..":win_cmd{@cd /d "..wpath.." && del /f /q "..file.."@}")
  sleep(5)
  --分发压缩包
  ExecCommand(lhost..":send_windows_file{@"..rpath..">"..wip.."?"..wpath.."?@}")
  sleep(10)
  --解压
  ExecCommand(wip..":win_cmd{@cd /d "..wpath.." && \""..rar.."\" x -r -o+ "..file.." .@}")
  sleep(10)
  -----------------------执行测试-----------------------------
  WriteLog("["..os.date("%x %X").."] Excute testing...")
  --ExecCommand(lhost..":open_file{@cd /d C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\testexe-windows && CMSAutoTest.exe -p "..ver.." -h "..wip.."@}")
  ExecCommand(lhost..":open_exe{@C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\wincmsexec.bat;\""..ver.."\" "..wip.."@}")
  sleep(1)
  ------------------------清理环境-----------------------------
  ExecCommand(wip..":win_cmd{@cd /d "..twpath.." && del /f /q "..ver.." && del /f /q CMS* && del /f /q setup.bat@}")
  WriteLog("["..os.date("%x %X").."] Clear testing ENV...")
else
  ---------------------测试环境准备-----------------------
  WriteLog("["..os.date("%x %X").."] Set the testing ENV...")
  ExecCommand(lip..":linux_shell{@cubrid service restart && cubrid server start demodb@}")
  sleep(10)
  --清理压缩包
  ExecCommand(lip..":linux_shell{@cd "..lpath.." && rm -fr "..file.."@}")
  sleep(5)
  --分发压缩包
  ExecCommand(lhost..":send_linux_file{@"..rpath..">"..lip..";"..lpath.."?@}")
  sleep(10)
  --解压
  ExecCommand(lip..":linux_shell{@cd "..lpath.." && unzip -o "..file.."@}")
  sleep(10)
  -----------------------执行测试-----------------------------
  WriteLog("["..os.date("%x %X").."] Excute testing...")
  --ExecCommand(lhost..":open_file{@cd /d C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\testexe-linux && CMSAutoTest.exe -p "..ver.." -h "..lip.."@}")
  ExecCommand(lhost..":open_exe{@C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\linuxcmsexec.bat;\""..ver.."\" "..lip.."@}")
  sleep(1)
  ------------------------清理环境-----------------------------
  ExecCommand(lip..":linux_shell{@cd "..tlpath.." && rm -fr "..ver.." CMS* setup.sh@}")
  WriteLog("["..os.date("%x %X").."] Clear testing ENV...")
end
end
------------------------------------------------------------------------------------------------
```

## 5.  测试完成发送邮件和结果链接函数 ##

```
----------------------------------sendmail(ver,style) -------------------------------------

----------------------------------style:both;win;linux------------------------------------

function sendmail(ver,style)
--CMS-10.34.130.44/2013-01-10/index.html
local project="CMS-"..ver
local dates=os.date("%Y-%m-%d")
local hpath="http://"..lhost.."/"
local lpath=hpath..project.."-"..lip.."/"..dates.."/index.html"
local wpath=hpath..project.."-"..wip.."/"..dates.."/index.html" 
local receiver="wugang@nhn.com|li.long2@nhn.com;qiaohongliang@nhn.com;huxiangwei@nhn.com"
local filename,f,t
if style == "both" then
  filename="C:/ECSTOOL/nginx/html/"..project.."-"..lip.."/"..dates.."/index.html"
  f = assert(io.open(filename, "r")) 
  t = f:read("*all") 
  f:close()
  --发送邮件：收件人列表|抄送|主题|正文|是否html
  ExecCommand("127.0.0.1:send_mail{@"..receiver.."|CMS Test Reports In Linux on "..dates.."| URL link:<br /><a href=\""..lpath.."\">"..lpath.."</a><br />"..string.gsub(t,"|","").."|true@}")
  sleep(1)
  filename="C:/ECSTOOL/nginx/html/"..project.."-"..wip.."/"..dates.."/index.html"
  f = assert(io.open(filename, "r")) 
  t = f:read("*all") 
  f:close()
  --发送邮件：收件人列表|抄送|主题|正文|是否html
  ExecCommand("127.0.0.1:send_mail{@"..receiver.."|CMS Test Reports In Windows on "..dates.."| URL link:<br /><a href=\""..wpath.."\">"..wpath.."</a><br />"..string.gsub(t,"|","").."|true@}")
  sleep(1)
elseif style == "win" then
  filename="C:/ECSTOOL/nginx/html/"..project.."-"..wip.."/"..dates.."/index.html"
  f = assert(io.open(filename, "r")) 
  t = f:read("*all") 
  f:close()
  --发送邮件：收件人列表|抄送|主题|正文|是否html
  ExecCommand("127.0.0.1:send_mail{@"..receiver.."|CMS Test Reports In Windows on "..dates.."| URL link:<br /><a href=\""..wpath.."\">"..wpath.."</a><br />"..string.gsub(t,"|","").."|true@}")
  sleep(1)
elseif style == "linux" then
  filename="C:/ECSTOOL/nginx/html/"..project.."-"..lip.."/"..dates.."/index.html"
  f = assert(io.open(filename, "r")) 
  t = f:read("*all") 
  f:close()
  --发送邮件：收件人列表|抄送|主题|正文|是否html
  ExecCommand("127.0.0.1:send_mail{@"..receiver.."|CMS Test Reports In Linux on "..dates.."| URL link:<br /><a href=\""..lpath.."\">"..lpath.."</a><br />"..string.gsub(t,"|","").."|true@}")
  sleep(1)
else
  filename="C:\\ECSTOOL\\Log\\CMS."..dates..".log"
  f = assert(io.open(filename, "r")) 
  t = f:read("*all") 
  f:close()
  --发送邮件：收件人列表|抄送|主题|正文|是否html
  ExecCommand("127.0.0.1:send_mail{@"..receiver.."|CMS Daily Test Log on "..dates.."| URL link:<br /><a href=\""..lpath.."\">"..lpath.."</a><br /><a href=\""..wpath.."\">"..wpath.."</a><br />"..string.gsub(t,"|","").."|true@}")
end
end

-------------------------------------------------------------------------------------------------------
```

## 6.  逻辑流程串联 ##

检查是否有新的需要测试的版本。

如果有： 【则下载源代码到各个Windows和Linux编译环境平台进行编译，编译成功后部署到相应的Windows和Linux测试服务器，然后启动自动化测试脚本或程序，测试完成后发送Summary邮件和测试报告网址。】

如果没有或者过程中有错：【中止测试，发送Summary邮件和错误日志。】

即，中控服务器自动定时任务：检查版本à多平台多服务器同时编译à多服务器同时部署à调用启动测试à测试结束展现测试结果（如自动发送邮件网页等）

```
--------------------------------------Follow(including setup) --------------------------------- 

WriteLog("==============================================================================================")
WriteLog("["..os.date("%x %X").."] Start daily test follow...")
WriteLog("==============================================================================================")
WriteLog("["..os.date("%x %X").."] Connect to windows "..wip.." ...")
wbool=connect(wip)
WriteLog("["..os.date("%x %X").."] Connect to linux "..lip.." ...")
lbool=connect(lip)
WriteLog("==============================================================================================")
WriteLog("["..os.date("%x %X").."] Start to check version and build.")
if (wbool == 1) and (lbool == 1) then  
  v,t=CheckVersion("both")
elseif wbool == 1 then
  v,t=CheckVersion("win")
else
  v,t=CheckVersion("linux")
end
WriteLog("["..os.date("%x %X").."] End to check version and build.")
WriteLog("==============================================================================================")
if v ~= nil then
  --等待编译
  sleep(1500)
  if (wbool == 1) and (lbool == 1) then
    ExecCommand(wip..":win_cmd{@cubrid service stop@}")
    ExecCommand(lip..":linux_shell{@cubrid service stop@}")
    sleep(10)
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to set up CMS.")
    ExecCommand(wip..":open_exe{@"..twpath.."setup.bat;"..v.."@}")
    ExecCommand(lip..":linux_shell{@cd "..tlpath.." && sh setup.sh "..v.."@}")
    sleep(100)
    WriteLog("["..os.date("%x %X").."] End to set up CMS.")
    WriteLog("==============================================================================================")
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to execute testing.")
    ExecuteTest(v,"both")
    sleep(3600)
    WriteLog("["..os.date("%x %X").."] End to execute testing.")
    WriteLog("==============================================================================================")
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to send mail.")
    sendmail(v,"both")
    WriteLog("["..os.date("%x %X").."] End to send mail.")
    WriteLog("==============================================================================================")
  elseif wbool == 1 then
    ExecCommand(wip..":win_cmd{@cubrid service stop@}")
    sleep(10)
    ExecCommand(wip..":open_exe{@"..twpath.."setup.bat;"..v.."@}")
    sleep(100)
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to execute testing.")
    ExecuteTest(v,"win")
    sleep(3600)
    WriteLog("["..os.date("%x %X").."] End to execute testing.")
    WriteLog("==============================================================================================")
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to send mail.")
    sendmail(v,"win")
    WriteLog("["..os.date("%x %X").."] End to send mail.")
    WriteLog("==============================================================================================")
  elseif lbool == 1 then
    ExecCommand(lip..":linux_shell{@cubrid service stop@}")
    sleep(10)
    ExecCommand(lip..":linux_shell{@cd "..tlpath.." && sh setup.sh "..v.."@}")
    sleep(100)
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to execute testing.")
    ExecuteTest(v,"linux")
    sleep(3600)
    WriteLog("["..os.date("%x %X").."] End to execute testing.")
    WriteLog("==============================================================================================")
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to send mail.")
    sendmail(v,"linux")
    WriteLog("["..os.date("%x %X").."] End to send mail.")
    WriteLog("==============================================================================================")
  end
  local ff = assert(io.open(tfile, "w")) 
  ff:write(t) 
  ff:close()
else   
  WriteLog("["..os.date("%x %X").."] All versions have been tested. Don't need to test!["..t.."]")
  WriteLog("==============================================================================================")
end
WriteLog("==============================================================================================")
WriteLog("["..os.date("%x %X").."] Complete daily test follow!")
WriteLog("==============================================================================================")
sendmail(v,"log")
return "Complete Test Follow!"
```
## 7. 测试结果效果 ##
保存上述脚本，配置定时任务:<br />
`0|0,4,8|*|*|*|exec_lua{@C:\ECSTOOL\Groups\CWMCMS\3Test\ExecTestFollow.lua@}`

![http://img.my.csdn.net/uploads/201302/01/1359700105_5910.jpg](http://img.my.csdn.net/uploads/201302/01/1359700105_5910.jpg)
![http://img.my.csdn.net/uploads/201302/01/1359700005_6554.jpg](http://img.my.csdn.net/uploads/201302/01/1359700005_6554.jpg)
![http://img.my.csdn.net/uploads/201302/01/1359700035_3835.jpg](http://img.my.csdn.net/uploads/201302/01/1359700035_3835.jpg)
![http://img.my.csdn.net/uploads/201302/01/1359700035_8989.jpg](http://img.my.csdn.net/uploads/201302/01/1359700035_8989.jpg)
![http://img.my.csdn.net/uploads/201211/01/1351733791_6985.jpg](http://img.my.csdn.net/uploads/201211/01/1351733791_6985.jpg)
![http://img.my.csdn.net/uploads/201211/01/1351733807_9954.jpg](http://img.my.csdn.net/uploads/201211/01/1351733807_9954.jpg)
[ecs](http://www.itmal.com/images/img/a8.jpg)
[ecs](http://www.itmal.com/images/img/a9.jpg)