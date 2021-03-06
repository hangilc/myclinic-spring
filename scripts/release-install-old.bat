::
:: Usage: install-release [LABEL]
:: installs binaries to the repository identified by %MYCLINIC_REPOSITORY%
::
@echo off
setlocal enabledelayedexpansion

for /f "tokens=* usebackq" %%t in (`timestamp`) do (
    set ts=%%t
)

set repo=%MYCLINIC_REPOSITORY%
if "%repo%" == "" (
    echo MYCLINIC_REPOSITORY is not set.
    goto :progend
)
set label=%1


set folder=%repo%\myclinic-release-%ts%
mkdir %folder%
copy hotline\target\hotline-1.0.0-SNAPSHOT.jar "%folder%\hotline.jar"
copy intraclinic\target\intraclinic-1.0.0-SNAPSHOT.jar "%folder%\intraclinic.jar"
copy management\target\management-1.0.0-SNAPSHOT-jar-with-dependencies.jar "%folder%\management.jar"
copy pharma\target\pharma-1.0.0-SNAPSHOT.jar "%folder%\pharma.jar"
copy practice\target\practice-1.0.0-SNAPSHOT.jar "%folder%\practice.jar"
copy rcpt\target\rcpt-1.0.0-SNAPSHOT.jar "%folder%\rcpt.jar"
copy rcpt-drawer\target\rcpt-drawer-1.0.0-SNAPSHOT.jar "%folder%\rcpt-drawer.jar"
copy reception\target\reception-1.0.0-SNAPSHOT.jar "%folder%\reception.jar"
copy record-browser\target\record-browser-1.0.0-SNAPSHOT.jar "%folder%\record-browser.jar"
copy scanner\target\scanner-1.0.0-SNAPSHOT.jar "%folder%\scanner.jar"
copy server\target\server-1.0.0-SNAPSHOT.jar "%folder%\server.jar"
xcopy config "%folder%\config" /I /Y /S /E /Q
type nul >"%folder%\myclinic-release-%ts%.txt"
if not %label% == "" (
	java -cp management\target\management-1.0.0-SNAPSHOT-jar-with-dependencies.jar jp.chang.myclinic.management.ReleaseLabel ^
          create "%folder%" "%label%"
)
set current=%repo%\current
if exist "%current%" (
    rmdir "%current%"
)
mklink /J "%current%" "%folder%"
:progend
endlocal
