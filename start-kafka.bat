@echo off
cd /d C:\kafka
start bin\windows\zookeeper-server-start.bat config\zookeeper.properties
timeout /t 10 /nobreak
start bin\windows\kafka-server-start.bat config\server.properties