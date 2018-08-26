::
:: Usage: deploy-current [TARGET-FOLDER]
:: Default target folder is %MYCLINIC_CURRENT%
::
@echo off
setlocal

set folder=%1
if "%folder%" == "" (
    set folder=%MYCLINIC_CURRENT%
)

java -cp management\target\management-1.0.0-SNAPSHOT-jar-with-dependencies.jar ^
    jp.chang.myclinic.management.DeployCurrent ^
    practice "%folder%"
java -cp management\target\management-1.0.0-SNAPSHOT-jar-with-dependencies.jar ^
    jp.chang.myclinic.management.CreateShortcuts ^
    practice "%folder%" "%folder%"

endlocal