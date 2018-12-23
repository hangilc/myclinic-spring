# my-pyenv\Scripts\Activate.ps1
# $env:PYTHONPATH="db\scripts"
# .\scripts\setup-new-postgresql-repository
# $loc = get-location
# set-location "postgresql-dev\sql"
# psql -f create-master-tables.sql myclinic "$env:MYCLINIC_DB_ADMIN_USER"
# psql -f create-data-tables.sql myclinic "$env:MYCLINIC_DB_ADMIN_USER"
# set-location $loc
# python db\scripts\xfer-db.py mysql postgresql
Write-Host "making initial backup as user postgres"
pg_basebackup -D C:\pgdata\main\backup -U postgres

