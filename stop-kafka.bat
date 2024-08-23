@echo off
cd /d C:\kafka
bin\windows\kafka-server-stop.bat
timeout /t 5 /nobreak
bin\windows\zookeeper-server-stop.bat
