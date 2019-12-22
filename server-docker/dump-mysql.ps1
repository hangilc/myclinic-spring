Param(
    [string]$DbHost = "localhost",
    [string]$DbUser = "$env:MYCLINIC_DB_ADMIN_USER",
    [string]$DbPass = "$env:MYCLINIC_DB_ADMIN_PASS",
    [string]$OutFile = "./data/dump.sql",
    [switch]$AddMasterData = $False,
    [string]$DefaultCharacterSet = ""
)

$OptMasterData = ""
if( $AddMasterData ){
    $OptMasterData = "--master-data"
}

$OptDefaultCharacterSet = ""
if( $DefaultCharacterSet -ne "" ){
    $OptDefaultCharacterSet = "--default-character-set=$DefaultCharacterSet"
}

if( $DbUser -eq "" ){
    throw "DbUser not specified."
}
if( $DbPass -eq "" ){
    throw "DbPass not specified."
}


mysqldump -h "$DbHost" -u "$DbUser" -p"$DbPass" `
    --result-file="$OutFile" `
    --single-transaction "$OptMasterData" "$OptDefaultCharacterSet" myclinic 