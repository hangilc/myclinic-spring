Param(
    [string]$DbHost = "localhost",
    [string]$DbUser = "$env:MYCLINIC_DB_ADMIN_USER",
    [string]$DbPass = "$env:MYCLINIC_DB_ADMIN_PASS",
    [string]$OutFile = "./data/production-sql/dump.sql",
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

mysqldump -h "$DbHost" -u "$env:MYCLINIC_DB_ADMIN_USER" -p"$env:MYCLINIC_DB_ADMIN_PASS" `
    --result-file="$OutFile" `
    --single-transaction "$OptMasterData" "$OptDefaultCharacterSet" myclinic 