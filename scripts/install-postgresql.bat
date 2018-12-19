@echo off
rem Usage: install-postgresql installer.exe
%1 --datadir C:\pgdata\main\cluster --enable-components server,commandlinetools --locale C ^
	--mode unattended --servicename postgresql

