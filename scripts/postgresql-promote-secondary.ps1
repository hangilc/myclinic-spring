Param(
    [string][alias('Host')]$DbHost = "localhost",
    [string]$User = $env:MYCLINIC_DB_USER,
    [switch]$Revoke = $false
)

$ErrorActionPreference = "Stop"
Invoke-Expression "$PSScriptRoot\use-local-psmodules"

if( $Revoke ){
    Revoke-PostgreSQLUserAccess -Host $DbHost -User $User
} else {
    Grant-PostgresqlUserAccess -Host $DbHost -User $User
}


