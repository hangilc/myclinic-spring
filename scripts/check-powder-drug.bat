@echo off
setlocal
set checkdate=%~1
if "%checkdate%" == "" (
    echo Usage: check-powder-drug DATE
    exit /b
)
java -cp management\target\management-1.0.0-SNAPSHOT-jar-with-dependencies.jar ^
    jp.chang.myclinic.management.CheckPowderDrug ""%checkdate%"
endlocal
