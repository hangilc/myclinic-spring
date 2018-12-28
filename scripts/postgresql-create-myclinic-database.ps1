Param (
    [alias('host')][string] $dbHost = 'localhost',
    [string]$admin = $env:MYCLINIC_DB_ADMIN_USER,
    [string]$staff = $env:MYCLINIC_DB_USER
)

$ErrorActionPreference = "Stop"

psql -h $dbHost -c "create database myclinic owner $admin" -U postgres
psql -h $dbHost -c "grant all on database myclinic to $admin" -U postgres
psql -h $dbHost -c "grant all on database myclinic to $staff" -U postgres
Push-Location -Path 'postgresql-dev\sql'
psql -h $dbHost -f 'create-master-tables.sql' myclinic $admin
psql -h $dbHost -f 'create-data-tables.sql' myclinic $admin
Pop-Location
