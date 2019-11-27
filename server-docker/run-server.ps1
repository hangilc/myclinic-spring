Param (
    [string]$DbHost = "localhost",
    [string]$DbUser = "",
    [string]$DbPass = ""
)

$ConfigDir = Resolve-Path "./data/config"
$JarPath = Resolve-Path "./data/server.jar"

$prog = "docker run"
$prog += " -v ${ConfigDir}:/usr/src/myapp/config"
$prog += " -v ${JarPath}:/usr/src/myapp/server.jar"
$prog += " --workdir=/usr/src/myapp"
$prog += " -e MYCLINIC_DB_HOST=$DbHost"
$prog += " -e MYCLINIC_DB_USER=$DbUser"
$prog += " -e MYCLINIC_DB_PASS=$DbPass"
$prog += " -p 18080:18080"
$prog += " openjdk:11-jre"
$prog += " java -jar server.jar"

Write-Host $prog
Invoke-Expression $prog