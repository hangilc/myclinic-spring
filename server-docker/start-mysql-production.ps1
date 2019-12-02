Param(
    [int]$Port = 3306,
    [string]$SqlDir = "data/production-sql",
    [string]$ConfFile = "myclinic-mysql-production.cnf"
)

$MYSQLROOTPASS="$env:MYCLINIC_DB_ROOT_PASS"
if( $MYSQLROOTPASS -eq "" ){
    throw "MYCLINIC_DB_ROOT_PASS is not specified."
}
$DOCKER_HOST = [System.Uri]$env:DOCKER_HOST
$HostAddr = $DOCKER_HOST.Host
$Cert = "$env:DOCKER_CERT_PATH\id_rsa"
ssh -i "$Cert" "docker@$HostAddr" "rm -rf ~/myclinic-server-data"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-data"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-data/sql"
scp -i "$Cert" "./$SqlDir/*.sql" "docker@${HostAddr}:~/myclinic-server-data/sql"
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

