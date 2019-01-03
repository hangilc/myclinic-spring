Param(
    [string][alias('Host')][parameter(mandatory)]$DbHost,
    [string]$User = $env:MYCLINIC_DB_USER,
    [switch]$Revoke = $false
)

$ErrorActionPreference = "Stop"
Invoke-Expression "$PSScriptRoot\use-local-psmodules"

if( $Revoke ){
    Revoke-PostgreSQLUserAccess -Host $DbHost -User $User
} else {
    $isRunning = Test-PostgreSQLServiceIsRunning $DbHost
    if( $isRunning ){
        Get-PostgreSQLSubscription -Host $DbHost |
            ForEach { 
                Write-Host "Removing current subscription:", $_.subname
                Remove-PostgreSQLSubscription -Host $DbHost -Subscription $_.subname 
            }
    }
    Grant-PostgresqlUserAccess -Host $DbHost -User $User
}


