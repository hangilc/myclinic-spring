::
:: Usage: deploy-pharma [pharma-prog-dir]
:: copies program jar files to %MYCLINIC_PHARMA_PROG%
:: jar files are copied from %MYCLINIC_REPOSITORY%\current
::

@echo off
setlocal enabledelayedexpansion
set dst=%1
if "%dst%" == "" (
    set dst=%MYCLINIC_PHARMA_PROG%
)
if "%dst%" == "" (
    echo MYCLINIC_PHARMA_PROG is not set
    goto :progend
)
if not exist "%dst%" (
    mkdir "%dst%"
)
set current=%MYCLINIC_REPOSITORY%\current
echo %dst%
if not exist "%current%" (
    echo Cannot find current specified by env var MYCLINIC_REPOSITORY
    goto :progend
)
copy /Y "%current%\hotline.jar" "%dst%"
copy /Y "%current%\intraclinic.jar" "%dst%"
copy /Y "%current%\management.jar" "%dst%"
copy /Y "%current%\pharma.jar" "%dst%"
:: copy /Y "%current%\practice.jar" "%dst%"
:: copy /Y "%current%\rcpt-drawer.jar" "%dst%"
:: copy /Y "%current%\reception.jar" "%dst%"
copy /Y "%current%\record-browser.jar" "%dst%"
;; copy /Y "%current%\scanner.jar" "%dst%"
:: copy /Y "%current%\server.jar" "%dst%"
:: xcopy "%current%\config" "%dst%\config" /I /Y /S /E /Q
copy /Y scripts\deploy\create-shortcuts-for-pharma.bat "%dst%"
:progend
endlocal


