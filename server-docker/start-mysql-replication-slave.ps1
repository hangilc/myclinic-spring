Param(
    [parameter(mandatory)]
    [string]$DbMasterHost,
    [int]$DbMasterPort = 3306,
    [string]$DbMasterRootPass = "$env:MYCLINIC_DB_ROOT_PASS"
)

if( ! (Test-Path "data/replication-sql") ){
    mkdir "data/replication-sql"
}
mysqldump -h "$DbMasterHost" --port="$DbMasterPort" `
    -u root -p"$DbMasterRootPass" `
    --result-file "data/replication/master-backup.sql" `
    --single-transaction --master-data myclinic 

$tmpl = (Get-Content "./slave-template.sql")
$slaveSql = $ExecutionContext.InvokeCommand.ExpandString($tmpl)
Set-Content "./data/replication-sql/01-slave.sql" $slaveSql


