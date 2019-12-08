Param (
    [string]$DbHost = "localhost",
    [string]$DbUser = "$env:MYCLINIC_DB_USER",
    [string]$DbPass = "$env:MYCLINIC_DB_PASS",
    [int]$Port = 18080
)

$DOCKER_HOST = [System.Uri]$env:DOCKER_HOST
$HostAddr = $DOCKER_HOST.Host
$Cert = "$env:DOCKER_CERT_PATH\id_rsa"
ssh -i "$Cert" "docker@$HostAddr" "rm -rf ~/myclinic-server-config"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-config"
scp -i "$Cert" "./data/config/*" "docker@${HostAddr}:~/myclinic-server-config"
ssh -i "$Cert" "docker@$HostAddr" "rm -rf ~/myclinic-server-jar"
ssh -i "$Cert" "docker@$HostAddr" "mkdir ~/myclinic-server-jar"
scp -i "$Cert" "./data/server.jar" "docker@${HostAddr}:~/myclinic-server-jar/server.jar"

docker run -it `
    -v "/home/docker/myclinic-server-config:/data/config" `
    -v "/home/docker/myclinic-server-jar:/data/jar" `
    --workdir="/usr/src/myapp" `
    -e MYCLINIC_DB_HOST="$DbHost" `
    -e MYCLINIC_DB_USER="$DbUser" `
    -e MYCLINIC_DB_PASS="$DbPass" `
    -p "${Port}:18080" `
    myclinic-server "/bin/bash"

# $prog = "docker run"
# $prog += " -v ${ConfigDir}:/usr/src/myapp/config"
# $prog += " -v ${JarPath}:/usr/src/myapp/server.jar"
# $prog += " --workdir=/usr/src/myapp"
# $prog += " -e MYCLINIC_DB_HOST=$DbHost"
# $prog += " -e MYCLINIC_DB_USER=$DbUser"
# $prog += " -e MYCLINIC_DB_PASS=$DbPass"
# $prog += " -p 18080:18080"
# $prog += " openjdk:11-jre"
# $prog += " java -jar server.jar"

# Write-Host $prog
# Invoke-Expression $prog