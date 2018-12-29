Param(
    [string][alias("Host")]$DbHost = "localhost",
    [string]$FromBackup = $null,
    [switch]$FromMysql = $false,
)
$ErrorActionPreference = "Stop"
Invoke-Expression "$PSScriptRoot\use-local-psmodules"

if( $FromMySql ){
    $src = "mysql"
    $dst = "postgresql"
    Initialize-PostgreSQLMyClinicSchema -DbHost $dbHost
    my-pyenv\Scripts\activate.ps1
    $env:PYTHONPATH="db\scripts"
    python db\scripts\xfer-db.py $src $dst
    deactivate
} elseif( $FromBackup ){
    Write-Error "Not implemented."
} else {
    Write-Error "Either -FromBackup or -FromMysql should be specified."
}