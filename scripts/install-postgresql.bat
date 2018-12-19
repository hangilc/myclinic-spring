@echo off
rem Usage: install-postgresql installer.exe
rem %1 --datadir C:\pgdata\main\cluster --enable-components server,commandlinetools --locale C ^
rem 	--mode unattended --servicename postgresql
echo "PostgreSQL has been installed."
set /P yesno="Open firewall port 5432? (Y/N): "
if "%yesno%" == "Y" (
	powershell -Command "start-process netsh -Verb runas -Argument 'advfirewall firewall add rule action=allow dir=in protocol=tcp name=PostgreSQL profile=private localport=5432'"
	echo "    Port 5432 has been opend."
)
echo "Add bin path to PATH environment variable."




