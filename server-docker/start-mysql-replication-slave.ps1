Param(
    [parameter(mandatory)]
    [string]$DbMasterHost,
    [int]$DbMasterPort = 3306,
    [string]$DbMasterRootPass = "$env:MYCLINIC_DB_ROOT_PASS",
    [string]$DbSlaveRootPass = "$env:MYCLINIC_DB_ROOT_PASS",
    [string]$DbSlaveUser = "$env:MYCLINIC_DB_USER",
    [string]$DbSlavePass = "$env:MYCLINIC_DB_PASS"
)

if( ! (Test-Path "data") ){
    throw "No data directory"
}

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

$DOCKER_HOST = [System.Uri]$env:DOCKER_HOST
$HostAddr = $DOCKER_HOST.Host
$Cert = "$env:DOCKER_CERT_PATH\id_rsa"
ssh -i "$Cert" "docker@$HostAddr" "rm -rf ~/myclinic-server-data"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-data"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-data/sql"
scp -i "$Cert" "./$SqlDir/*.sql" "docker@${HostAddr}:~/myclinic-server-data/sql"
scp -i "$Cert" "./grant-repl.sql" "docker@${HostAddr}:~/myclinic-server-data/sql/grant-repl.sql"
scp -i "$Cert" "$ConfFile" "docker@${HostAddr}:~/myclinic-server-data/myclinic-mysql.cnf"
docker run -e MYSQL_ROOT_PASSWORD="$MYSQLROOTPASS" `
    -d `
    -e MYSQL_USER="$env:MYCLINIC_DB_USER" `
    -e MYSQL_PASSWORD="$env:MYCLINIC_DB_PASS" `
    -e MYSQL_DATABASE="myclinic" `
    -p "${Port}:3306" `
    -v "/home/docker/myclinic-server-data/myclinic-mysql.cnf:/etc/mysql/conf.d/myclinic-mysql.cnf" `
    -v "/home/docker/myclinic-server-data/sql:/docker-entrypoint-initdb.d" `
    mysql:5.7

