--[[
--CreateDate: 2013-01-18
--UpdateDate: 2013-01-21
--author: wugang
--project: CMS
--]]
----------------------------------------------------ȫ�ֱ�����������------------------------------------------------------
wip="10.34.130.63"
lip="10.34.130.44"
lhost="10.34.130.62"
tlpath="/tmp/"
twpath="C:\\ECSTOOL\\Temp\\"
dates=os.date("%Y-%m-%d")
cfile="C:\\ECSTOOL\\Groups\\CWMCMS\\0CheckVersion\\current"
tfile="C:\\ECSTOOL\\Groups\\CWMCMS\\0CheckVersion\\test"
----------------------------------------------------��־��¼---------------------------------------------------------------
LogPath="C:\\ECSTOOL\\Log\\CMS."..dates..".log"
function WriteLog(str)
local ft = assert(io.open(LogPath, "a")) 
ft:write(str.."<br />\r\n") 
ft:close()
end
---------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------sleep(n)��sleeping(n)-----------------------------------------------------
--����ϵͳping����ȴ�ʱ��
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
---------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------connect(IP)����1�ɹ�0ʧ��-------------------------------------------------
--��������
function connect(tip)
 local sip="127.0.0.1"
 --���ӷ�����
 ExecCommand(sip..":connect_one_server{@"..tip.."@}")
 local count=0
 while count<1000 do 
  sleep(1)
  ExecCommand(sip..":get_status_server{@"..tip.."@}")
  --Ѱ�����ַ�������������
  local s, e = string.find(string.lower(MyForm.hReturn[MyForm.hReturn.Count-1]),"true",1)
  if (e ~= nil) and (s ~= nil) and (e > s) then 
     x = string.sub(MyForm.hReturn[MyForm.hReturn.Count-1], s, e) 
     return 1
  end 
  count = count+1
 end
 return 0
end
-------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------CheckVersion(style)-----------------------------------------------
------------------------------------------------------ style:both;win;linux----------------------------------------------
function CheckVersion(style)
local url="http://svn.cubrid.org/cubridtools/cm-server/branches/"
local build="C:\\ECSTOOL\\Groups\\CWMCMS\\1Build\\"
local setpath="C:\\ECSTOOL\\Groups\\CWMCMS\\2Setup\\"
local lbuild="packagebuild_new.sh"
local wbuild="packagebuild_new.bat"
local version=nil
ExecCommand(lhost..":open_file{@svn list "..url..">"..cfile.."@}")
sleep(10)
--���Ѳ�汾�б�
local ft = assert(io.open(tfile, "r")) 
local tt = ft:read("*all") 
ft:close()
tt=string.gsub(string.gsub(tt,"\n",""),"/","")
--����ǰ�汾�б�
local fc = assert(io.open(cfile, "r")) 
local tc = fc:read("*all") 
fc:close()
tc=string.gsub(string.gsub(tc,"\n",""),"/","")
--�Ƚϻ�ȡ��Ҫ���԰汾
if tc == tt then 
  return version,tc
else
  tt=string.gsub(string.gsub(tt,"%.","%%."),"%-","%%-")
  version=string.gsub(tc,tt,"")
  --windows��linuxͬʱ����
  if style == "both" then
    --ɾ���ϴνű�
    ExecCommand(wip..":win_cmd{@cd "..twpath.." &&del /F /Q "..wbuild.." && del /F /Q setup.bat@}")
    ExecCommand(lip..":linux_shell{@cd "..tlpath.." &&rm -fr "..lbuild.." setup.sh@}")
    sleep(5)
    --�ַ��ű�build
    ExecCommand(lhost..":send_windows_file{@"..build..wbuild..">"..wip.."?"..twpath.."?@}")
    sleep(1)
    ExecCommand(lhost..":send_linux_file{@"..build..lbuild..">"..lip..";"..tlpath.."?@}")
    sleep(10)
    --�ַ��ű�setup
    ExecCommand(lhost..":send_windows_file{@"..setpath.."setup.bat>"..wip.."?"..twpath.."?@}")
    sleep(1)
    ExecCommand(lhost..":send_linux_file{@"..setpath.."setup.sh>"..lip..";"..tlpath.."?@}")
    sleep(10)
    --Ǩ������
    ExecCommand(wip..":open_exe{@"..twpath..wbuild..";"..version.."@}")
    ExecCommand(lip..":linux_shell{@cd "..tlpath.." &&sh "..lbuild.." "..version.."@}")
    sleep(10)
  --ֻ����windows
  elseif style == "win" then
    --ɾ���ϴνű�
    ExecCommand(wip..":win_cmd{@cd "..twpath.." &&del /F /Q "..wbuild.."@}")
    sleep(5)
    --�ַ��ű�
    ExecCommand(lhost..":send_windows_file{@"..build..wbuild..">"..wip.."?"..twpath.."?@}")
    sleep(10)
    --Ǩ������
    ExecCommand(wip..":open_exe{@"..twpath..wbuild..";"..version.."@}")
    sleep(10)
  --ֻ����linux
  else
    --ɾ���ϴνű�
    ExecCommand(lip..":linux_shell{@cd "..tlpath.." &&rm -fr "..lbuild.."@}")
    sleep(5)
    --�ַ��ű�
    sleep(1)
    ExecCommand(lhost..":send_linux_file{@"..build..lbuild..">"..lip..";"..tlpath.."?@}")
    sleep(10)
    --Ǩ������
    ExecCommand(lip..":linux_shell{@cd "..tlpath.." &&sh "..lbuild.." "..version.."@}")
    sleep(10)
  end
  return version,tc
end
end
--------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------ExecuteTest(ver,style)---------------------------------------------
--------------------------------------------------------style:both;win;linux---------------------------------------------
function ExecuteTest(ver,style)
--����
local lpath="/root/CUBRID/bin/"
local wpath="C:\\CUBRID\\bin\\"
local rpath="C:\\ECSTOOL\\Temp\\TestReady.zip"
local rar="%ProgramFiles%/WinRAR/WinRAR.exe"
local file="TestReady.zip"
ExecCommand(lhost..":kill_proc{@cmd@}{@CMSAutoTest@}")
sleep(5)
if style == "both" then
  ---------------------���Ի���׼��-----------------------
  WriteLog("["..os.date("%x %X").."] Set the testing ENV...")
  ExecCommand(wip..":win_cmd{@cd "..wpath.." && cubrid.exe service restart@}")
  ExecCommand(lip..":linux_shell{@cubrid service restart && cubrid server start demodb@}")
  sleep(10)
  ExecCommand(wip..":win_cmd{@cd "..wpath.." && cubrid.exe server start demodb@}")
  sleep(5)
  --����ѹ����
  ExecCommand(wip..":win_cmd{@cd "..wpath.." && del /f /q "..file.."@}")
  ExecCommand(lip..":linux_shell{@cd "..lpath.." && rm -fr "..file.."@}")
  sleep(5)
  --�ַ�ѹ����
  ExecCommand(lhost..":send_windows_file{@"..rpath..">"..wip.."?"..wpath.."?@}")
  sleep(10)
  ExecCommand(lhost..":send_linux_file{@"..rpath..">"..lip..";"..lpath.."?@}")
  sleep(10)
  --��ѹ
  ExecCommand(wip..":win_cmd{@cd "..wpath.." && \""..rar.."\" x -r -o+ "..file.." .@}")
  ExecCommand(lip..":linux_shell{@cd "..lpath.." && unzip -o "..file.."@}")
  sleep(10)
  -----------------------ִ�в���-----------------------------
  WriteLog("["..os.date("%x %X").."] Excute testing...")
  ExecCommand(lhost..":open_file{@cd C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\testexe-windows && CMSAutoTest.exe -p "..ver.." -h "..wip.."@}")
  sleep(1)
  ExecCommand(lhost..":open_file{@cd C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\testexe-linux && CMSAutoTest.exe -p "..ver.." -h "..lip.."@}")
  sleep(1)
  ------------------------������-----------------------------
  ExecCommand(wip..":win_cmd{@cd "..twpath.." && del /f /q "..ver.." && del /f /q CMS* && del /f /q setup.bat@}")
  ExecCommand(lip..":linux_shell{@cd "..tlpath.." && rm -fr "..ver.." CMS* setup.sh@}")
  WriteLog("["..os.date("%x %X").."] Clear testing ENV...")
elseif style == "win" then 
  ---------------------���Ի���׼��-----------------------
  WriteLog("["..os.date("%x %X").."] Set the testing ENV...")
  ExecCommand(wip..":win_cmd{@cd "..wpath.." && cubrid.exe service restart@}")
  sleep(10)
  ExecCommand(wip..":win_cmd{@cd "..wpath.." && cubrid.exe server start demodb@}")
  sleep(5)
  --����ѹ����
  ExecCommand(wip..":win_cmd{@cd "..wpath.." && del /f /q "..file.."@}")
  sleep(5)
  --�ַ�ѹ����
  ExecCommand(lhost..":send_windows_file{@"..rpath..">"..wip.."?"..wpath.."?@}")
  sleep(10)
  --��ѹ
  ExecCommand(wip..":win_cmd{@cd "..wpath.." && \""..rar.."\" x -r -o+ "..file.." .@}")
  sleep(10)
  -----------------------ִ�в���-----------------------------
  WriteLog("["..os.date("%x %X").."] Excute testing...")
  ExecCommand(lhost..":open_file{@cd C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\testexe-windows && CMSAutoTest.exe -p "..ver.." -h "..wip.."@}")
  sleep(1)
  ------------------------������-----------------------------
  ExecCommand(wip..":win_cmd{@cd "..twpath.." && del /f /q "..ver.." && del /f /q CMS* && del /f /q setup.bat@}")
  WriteLog("["..os.date("%x %X").."] Clear testing ENV...")
else
  ---------------------���Ի���׼��-----------------------
  WriteLog("["..os.date("%x %X").."] Set the testing ENV...")
  ExecCommand(lip..":linux_shell{@cubrid service restart && cubrid server start demodb@}")
  sleep(10)
  --����ѹ����
  ExecCommand(lip..":linux_shell{@cd "..lpath.." && rm -fr "..file.."@}")
  sleep(5)
  --�ַ�ѹ����
  ExecCommand(lhost..":send_linux_file{@"..rpath..">"..lip..";"..lpath.."?@}")
  sleep(10)
  --��ѹ
  ExecCommand(lip..":linux_shell{@cd "..lpath.." && unzip -o "..file.."@}")
  sleep(10)
  -----------------------ִ�в���-----------------------------
  WriteLog("["..os.date("%x %X").."] Excute testing...")
  ExecCommand(lhost..":open_file{@cd C:\\ECSTOOL\\Groups\\CWMCMS\\3Test\\testexe-linux && CMSAutoTest.exe -p "..ver.." -h "..lip.."@}")
  sleep(1)
  ------------------------������-----------------------------
  ExecCommand(lip..":linux_shell{@cd "..tlpath.." && rm -fr "..ver.." CMS* setup.sh@}")
  WriteLog("["..os.date("%x %X").."] Clear testing ENV...")
end
end
----------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------sendmail(ver,style)--------------------------------------------------------
-------------------------------------------------------style:both;win;linux-------------------------------------------------------
function sendmail(ver,style)
--CMS-10.34.130.44/2013-01-10/index.html
local project="CMS-"..ver
local dates=os.date("%Y-%m-%d")
local hpath="http://"..lhost.."/"
local lpath=hpath..project.."-"..lip.."/"..dates.."/index.html"
local wpath=hpath..project.."-"..wip.."/"..dates.."/index.html" 
local receiver="wugang@nhn.com;huxiangwei@nhn.com|qiaohongliang@nhn.com"
local filename,f,t
if style == "both" then
  filename="C:/ECSTOOL/nginx/html/"..project.."-"..lip.."/"..dates.."/index.html"
  f = assert(io.open(filename, "r")) 
  t = f:read("*all") 
  f:close()
  --�����ʼ����ռ����б�|����|����|����|�Ƿ�html
  ExecCommand("127.0.0.1:send_mail{@"..receiver.."|CMS Test Reports In Linux on "..dates.."| URL link:<br /><a href=\""..lpath.."\">"..lpath.."</a><br />"..string.gsub(t,"|","").."|true@}")
  sleep(1)
  filename="C:/ECSTOOL/nginx/html/"..project.."-"..wip.."/"..dates.."/index.html"
  f = assert(io.open(filename, "r")) 
  t = f:read("*all") 
  f:close()
  --�����ʼ����ռ����б�|����|����|����|�Ƿ�html
  ExecCommand("127.0.0.1:send_mail{@"..receiver.."|CMS Test Reports In Windows on "..dates.."| URL link:<br /><a href=\""..wpath.."\">"..wpath.."</a><br />"..string.gsub(t,"|","").."|true@}")
  sleep(1)
elseif style == "win" then
  filename="C:/ECSTOOL/nginx/html/"..project.."-"..wip.."/"..dates.."/index.html"
  f = assert(io.open(filename, "r")) 
  t = f:read("*all") 
  f:close()
  --�����ʼ����ռ����б�|����|����|����|�Ƿ�html
  ExecCommand("127.0.0.1:send_mail{@"..receiver.."|CMS Test Reports In Windows on "..dates.."| URL link:<br /><a href=\""..wpath.."\">"..wpath.."</a><br />"..string.gsub(t,"|","").."|true@}")
  sleep(1)
else
  filename="C:/ECSTOOL/nginx/html/"..project.."-"..lip.."/"..dates.."/index.html"
  f = assert(io.open(filename, "r")) 
  t = f:read("*all") 
  f:close()
  --�����ʼ����ռ����б�|����|����|����|�Ƿ�html
  ExecCommand("127.0.0.1:send_mail{@"..receiver.."|CMS Test Reports In Linux on "..dates.."| URL link:<br /><a href=\""..lpath.."\">"..lpath.."</a><br />"..string.gsub(t,"|","").."|true@}")
  sleep(1)
end
filename="C:\\ECSTOOL\\Log\\CMS."..dates..".log"
f = assert(io.open(filename, "r")) 
t = f:read("*all") 
f:close()
--�����ʼ����ռ����б�|����|����|����|�Ƿ�html
ExecCommand("127.0.0.1:send_mail{@"..receiver.."|CMS Daily Test Log on "..dates.."| URL link:<br /><a href=\""..lpath.."\">"..lpath.."</a><br /><a href=\""..wpath.."\">"..wpath.."</a><br />"..string.gsub(t,"|","").."|true@}")
end
-----------------------------------------------------------------------------------------------------------------------------------
-------------------------------------------------------Follow(including setup)----------------------------------------------------- 
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
  --�ȴ�����
  sleep(1800)
  if (wbool == 1) and (lbool == 1) then
    ExecCommand(wip..":win_cmd{@cubrid service stop@}")
    ExecCommand(lip..":linux_shell{@cubrid service stop@}")
    sleep(10)
    ExecCommand(wip..":open_exe{@"..twpath.."setup.bat;"..v.."@}")
    ExecCommand(lip..":linux_shell{@cd "..tlpath.." && sh setup.sh "..v.."@}")
    sleep(200)
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to execute testing.")
    ExecuteTest(v,"both")
    WriteLog("["..os.date("%x %X").."] End to execute testing.")
    WriteLog("==============================================================================================")
    sleep(4000)
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to send mail.")
    sendmail(v,"both")
    WriteLog("["..os.date("%x %X").."] End to send mail.")
    WriteLog("==============================================================================================")
  elseif wbool == 1 then
    ExecCommand(wip..":win_cmd{@cubrid service stop@}")
    sleep(10)
    ExecCommand(wip..":open_exe{@"..twpath.."setup.bat;"..v.."@}")
    sleep(200)
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to execute testing.")
    ExecuteTest(v,"win")
    WriteLog("["..os.date("%x %X").."] End to execute testing.")
    WriteLog("==============================================================================================")
    sleep(4000)
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to send mail.")
    sendmail(v,"win")
    WriteLog("["..os.date("%x %X").."] End to send mail.")
    WriteLog("==============================================================================================")
  elseif lbool == 1 then
    ExecCommand(lip..":linux_shell{@cubrid service stop@}")
    sleep(10)
    ExecCommand(lip..":linux_shell{@cd "..tlpath.." && sh setup.sh "..v.."@}")
    sleep(200)
    WriteLog("==============================================================================================")
    WriteLog("["..os.date("%x %X").."] Start to execute testing.")
    ExecuteTest(v,"linux")
    WriteLog("["..os.date("%x %X").."] End to execute testing.")
    WriteLog("==============================================================================================")
    sleep(4000)
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
WriteLog("["..os.date("%x %X").."] Complete daily test follow...")
WriteLog("==============================================================================================")
return "Complete Test Follow!"
