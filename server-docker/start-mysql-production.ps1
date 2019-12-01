Param(
    [int]$Port = 3306,
    [string]$SqlDir = "dump-sql"
    )

$MYSQLROOTPASS="$env:MYCLINIC_DB_ROOT_PASS"
if( $MYSQLROOTPASS -eq "" ){
    throw "MYCLINIC_DB_ROOT_PASS is not specified."
}
$DOCKER_HOST = [System.Uri]$env:DOCKER_HOST
$HostAddr = $DOCKER_HOST.Host
$Cert = "$env:DOCKER_CERT_PATH\id_rsa"
ssh -i "$Cert" "docker@$HostAddr" "mkdir -p ~/myclinic-server-data"
ssh -i "$Cert" "docker@$HostAddr" "rm -rf ~/myclinic-server-data/*-sql"
scp -i "$Cert" -r "./data/$SqlDir" "docker@${HostAddr}:~/myclinic-server-data/"
scp -i "$Cert" ./data/myclinic-mysql-production.cnf "docker@${HostAddr}:~/myclinic-server-data/myclinic-mysql-production.cnf"
docker run -e MYSQL_ROOT_PASSWORD="$MYSQLROOTPASS" `
    -d `
    -e MYSQL_USER="$env:MYCLINIC_DB_USER" `
    -e MYSQL_PASSWORD="$env:MYCLINIC_DB_PASS" `
    -e MYSQL_DATABASE="myclinic" `
    -p "${Port}:3306" `
    -v "/home/docker/myclinic-server-data/myclinic-mysql-production.cnf:/etc/mysql/conf.d/myclinic-mysql-production.cnf" `
    -v "/home/docker/myclinic-server-data/${SqlDir}:/docker-entrypoint-initdb.d" `
    mysql:5.7

