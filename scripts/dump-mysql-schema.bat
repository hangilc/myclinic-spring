@mysqldump -h %MYCLINIC_DB_HOST% -u %MYCLINIC_DB_USER% -p%MYCLINIC_DB_PASS% --no-data myclinic >server\migrations\schema.sql
