Param(
    [parameter(mandatory)]
    [string]$DbMasterHost,
    [int]$DbMasterPort = 3306,
    [string]$DbMasterRootPass = "$env:MYCLINIC_DB_ROOT_PASS",
    [string]$DbSlaveRootPass = "$env:MYCLINIC_DB_ROOT_PASS",
    [string]$DbSlaveUser = "$env:MYCLINIC_DB_USER",
    [string]$DbSlavePass = "$env:MYCLINIC_DB_PASS",
    [int]$DbSlaveServerId = 0,
    [int]$DbSlavePort = 3306
)

function SelectServerId(){
    $result = (mysql -h $DbMasterHost -u root -p"$DbMasterRootPass" -e "show slave hosts" --xml)
#     $result = @'
# <?xml version="1.0"?>
# <resultset statement="show slave hosts
# " xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
#   <row>
#         <field name="Server_id">2</field>
#         <field name="Host"></field>
#         <field name="Port">3306</field>
#         <field name="Master_id">1</field>
#         <field name="Slave_UUID">826a8587-1654-11ea-a900-0242ac110002</field>
#   </row>
#   <row>
#         <field name="Server_id">13</field>
#         <field name="Host"></field>
#         <field name="Port">3306</field>
#         <field name="Master_id">1</field>
#         <field name="Slave_UUID">826a8587-1654-11ea-a900-0242ac110002</field>
#   </row>
#   <row>
#         <field name="Server_id">3</field>
#         <field name="Host"></field>
#         <field name="Port">3306</field>
#         <field name="Master_id">1</field>
#         <field name="Slave_UUID">826a8587-1654-11ea-a900-0242ac110002</field>
#   </row>
# </resultset>
# '@
    $xml = [XML]$result
    $ids = $xml.SelectNodes("//resultset/row/field[@name='Server_id']").InnerXML
    $mes = ($ids | Measure-Object -Maximum)
    if( $mes.Count -eq 0 ){
        return 2
    } else {
        return ($mes.Maximum + 1)
    }
}

if( $DbSlaveServerId -eq 0 ){
    $DbSlaveServerId = SelectServerId
}

if( $DbSlaveServerId -le 1 ){
    throw "Invalid slave server-id ${DbSlaveServerId}"
}

if( ! (Test-Path "data") ){
    mkdir "data"
}

if( Test-Path "data/replication-sql" ){
    Remove-Item -Recurse "data/replication-sql"
}
mkdir "data/replication-sql"
mysqldump -h "$DbMasterHost" --port="$DbMasterPort" `
    -u "root" -p"$DbMasterRootPass" `
    --result-file "data/replication-sql/master-backup.sql" `
    --single-transaction --master-data myclinic 

$tmpl = (Get-Content "./slave-template.sql") -join "`n"
$slaveSql = $ExecutionContext.InvokeCommand.ExpandString($tmpl)
Set-Content "./data/replication-sql/01-slave.sql" $slaveSql

if( Test-Path "data/replication-cnf" ){
    Remove-Item -Recurse "data/replication-cnf"
}
mkdir "data/replication-cnf"
$tmpl = (Get-Content "./slave-template.cnf") -join "`n"
$slaveConf = $ExecutionContext.InvokeCommand.ExpandString($tmpl)
Set-Content "./data/replication-cnf/slave.cnf" $slaveConf

$SqlDir = "./data/replication-sql"
$ConfDir = "./data/replication-cnf"
$DOCKER_HOST = [System.Uri]$env:DOCKER_HOST
$HostAddr = $DOCKER_HOST.Host
$Cert = "$env:DOCKER_CERT_PATH\id_rsa"
ssh -i "$Cert" "docker@$HostAddr" "rm -rf ~/myclinic-server-data"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-data"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-data/sql"
scp -i "$Cert" "${SqlDir}/*.sql" "docker@${HostAddr}:~/myclinic-server-data/sql"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-data/cnf"
scp -i "$Cert" "${ConfDir}/*.cnf" "docker@${HostAddr}:~/myclinic-server-data/cnf"
docker run -e MYSQL_ROOT_PASSWORD="$DbSlaveRootPass" `
    -d `
    -e MYSQL_USER="$DbSlaveUser" `
    -e MYSQL_PASSWORD="$DbSlavePass" `
    -e MYSQL_DATABASE="myclinic" `
    -p "${DbSlavePort}:3306" `
    -v "/home/docker/myclinic-server-data/sql:/data/sql" `
    -v "/home/docker/myclinic-server-data/cnf:/data/cnf" `
    myclinic-mysql 

