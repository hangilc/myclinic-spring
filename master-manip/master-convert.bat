@echo off
set progdir=%~dp0
set jarfile=%progdir%target\master-manip-1.0.0-SNAPSHOT.jar
set mainclass=jp.chang.myclinic.mastermanip.Convert
java -cp %jarfile% %mainclass% %*


