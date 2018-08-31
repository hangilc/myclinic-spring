::
:: Usage: install-release [RELEASE-REPOSITORY-FOLDER]
:: Default RELEASE-REPOSITORY-FOLDER is %MYCLINIC_REPOSITORY%
::
@echo off
setlocal enabledelayedexpansion

for /f "tokens=* usebackq" %%t in (`timestamp`) do (
    set ts=%%t
)

set repo=%1
if "%repo%" == "" (
    set repo=%MYCLINIC_REPOSITORY%
)

set folder=%repo%\myclinic-release-%ts%

java -cp management\target\management-1.0.0-SNAPSHOT-jar-with-dependencies.jar ^
    jp.chang.myclinic.management.DeployCurrent "%folder%"
echo %folder% > "%repo%\current.txt"
call shortcut-create.bat "%repo%"\current.lnk "%folder%"
endlocal
