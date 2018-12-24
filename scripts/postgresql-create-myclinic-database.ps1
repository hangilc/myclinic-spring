psql -c 'create database myclinic owner hangil' -U postgres
psql -c 'grant all on database myclinic to hangil' -U postgres
psql -c 'grant all on database myclinic to staff' -U postgres
Push-Location -Path 'postgresql-dev\sql'
psql -f 'create-master-tables.sql' myclinic $env:MYCLINIC_DB_ADMIN_USER
psql -f 'create-data-tables.sql' myclinic $env:MYCLINIC_DB_ADMIN_USER
Pop-Location
