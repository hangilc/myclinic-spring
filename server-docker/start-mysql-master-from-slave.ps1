Param (
    [parameter(mandatory)]
    [string]$DbSlaveHost,
    [int]$DbSlavePort = 3306,
    [string]$DbSlaveRootPass = "$env:MYCLINIC_DB_ROOT_PASS",
    [int]$Port = 3306,
    [string]$ConfFile = "master.cnf",
    [string]$DbRootPass = "$env:MYCLINIC_DB_ROOT_PASS",
    [string]$DbUser = "$env:MYCLINIC_DB_USER",
    [string]$DbPass = "$env:MYCLINIC_DB_PASS",
    [string]$Name = "myclinic-mysql-server"
)

New-Item -Path "data/production-sql" -ItemType directory -Force
$stamp = get-date -Format "yyyyMMddHHmmss"
$dumpfile = "data/production-sql/slave-dump-$stamp.sql"
mysqldump -h "$DbSlaveHost" --port="$DbSlavePort" `
    -u "root" -p"$DbSlaveRootPass" `
    --result-file "$dumpfile" `
    --single-transaction myclinic 
.\start-mysql-master -SqlFile "$dumpfile" -Port $Port -DbRootPass "$DbRootPass" `
    -DbUser "$DbUser" -DbPass "$DbPass" -Name "$Name"




