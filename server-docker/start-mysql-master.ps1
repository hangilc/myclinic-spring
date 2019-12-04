Param(
    [parameter(mandatory)]
    [string]$SqlFile,
    [int]$Port = 3306,
    [string]$ConfFile = "master.cnf",
    [string]$DbRootPass = "$env:MYCLINIC_DB_ROOT_PASS",
    [string]$DbUser = "$env:MYCLINIC_DB_USER",
    [string]$DbPass = "$env:MYCLINIC_DB_PASS"
)

if( $DbRootPass -eq "" ){
    throw "MYCLINIC_DB_ROOT_PASS is not specified."
}
$DOCKER_HOST = [System.Uri]$env:DOCKER_HOST
$HostAddr = $DOCKER_HOST.Host
$Cert = "$env:DOCKER_CERT_PATH\id_rsa"
ssh -i "$Cert" "docker@$HostAddr" "rm -rf ~/myclinic-server-data"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-data"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-data/sql"
scp -i "$Cert" "$SqlFile" "docker@${HostAddr}:~/myclinic-server-data/sql/master-data.sql"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-data/cnf"
scp -i "$Cert" "$ConfFile" "docker@${HostAddr}:~/myclinic-server-data/cnf/myclinic-master.cnf"
docker run -e MYSQL_ROOT_PASSWORD="$DbRootPass" `
    -d `
    -e MYSQL_USER="$DbUser" `
    -e MYSQL_PASSWORD="$DbPass" `
    -e MYSQL_DATABASE="myclinic" `
    -p "${Port}:3306" `
    -v "/home/docker/myclinic-server-data/cnf:/data/cnf" `
    -v "/home/docker/myclinic-server-data/sql:/data/sql" `
    myclinic-mysql

