::
:: Usage: deploy-pharma [TARGET-FOLDER]
:: Default target folder is %MYCLINIC_PHARMA_CURRENT%
::
@echo off
setlocal

set folder=%1
if "%folder%" == "" (
    set folder=%MYCLINIC_PHARMA_CURRENT%
)

java -cp management\target\management-1.0.0-SNAPSHOT-jar-with-dependencies.jar ^
    jp.chang.myclinic.management.DeployCurrent ^
    pharma "%folder%"

endlocal