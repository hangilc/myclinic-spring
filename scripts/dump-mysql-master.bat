@echo off
mysqldump -u root -p --databases myclinic intraclinic --single-transaction ^
    --flush-logs --master-data=2 --add-drop-table 