::
:: Usage: deploy-practice
:: copies program jar files to %MYCLINIC_PRACTICE_PROG%
:: jar files are copied from %MYCLINIC_REPOSITORY%\current
::

:: @echo off
setlocal enabledelayedexpansion
set dst=%MYCLINIC_PRACTICE_PROG%
if not exist "%dst%" (
    mkdir "%dst%"
)
set /P current=<"%MYCLINIC_REPOSITORY%\current.txt"
set current=%current%
copy "%current%\hotline.jar" "%dst%"
endlocal

