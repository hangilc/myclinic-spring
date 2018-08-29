@echo off
@mysqldump -h %MYCLINIC_DB_HOST% -u %MYCLINIC_DB_USER% -p%MYCLINIC_DB_PASS% --no-data myclinic ^
    | sed "s/AUTO_INCREMENT=[0-9]*\b//g" ^
    >server\migrations\schema.sql
