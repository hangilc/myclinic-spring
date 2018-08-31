::
:: Usage: deploy-practice
:: copies program jar files to %MYCLINIC_PRACTICE_PROG%
:: jar files are copied from %MYCLINIC_REPOSITORY%\current
::

@echo off
setlocal enabledelayedexpansion
set dst=%MYCLINIC_PRACTICE_PROG%
if not exist "%dst%" (
    mkdir "%dst%"
)
set current=%MYCLINIC_REPOSITORY%\current
copy /Y "%current%\hotline.jar" "%dst%"
copy /Y "%current%\intraclinic.jar" "%dst%"
copy /Y "%current%\pharma.jar" "%dst%"
copy /Y "%current%\practice.jar" "%dst%"
copy /Y "%current%\rcpt-drawer.jar" "%dst%"
copy /Y "%current%\reception.jar" "%dst%"
copy /Y "%current%\record-browser.jar" "%dst%"
copy /Y "%current%\scanner.jar" "%dst%"
copy /Y "%current%\server.jar" "%dst%"
xcopy "%current%\config" "%dst%\config" /I /Y /S /E /Q
endlocal

