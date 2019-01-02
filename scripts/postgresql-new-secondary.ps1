Param(
    [parameter(mandatory)][string]$SecondaryHost,
    [ValidateScript({$_ -notin @("localhost", "127.0.0.1")})]
    [parameter(mandatory)][string]$PrimaryHost
)

$ErrorActionPreference = "Stop"
Invoke-Expression "$PSScriptRoot\use-local-psmodules"

$isRunning = Test-PostgreSQLServiceIsRunning $SecondaryHost
if( $isRunning ){
    Get-PostgreSQLSubscription -Host $SecondaryHost |
        ForEach { 
            Write-Host "Removing current subscription:", $_.subname
            Remove-PostgreSQLSubscription -Host $SecondaryHost -Subscription $_.subname 
        }
}
New-PostgreSQLRepository $SecondaryHost
Start-PostgreSQLService $SecondaryHost
New-PostgreSQLMyClinicDatabase $SecondaryHost
Initialize-PostgreSQLMyClinicSchema $SecondaryHost
Grant-PostgreSQLUserPrivilege -Host $SecondaryHost -User $env:MYCLINIC_DB_USER
New-PostgreSQLSecondary -SecondaryHost $SecondaryHost -PrimaryHost $PrimaryHost
if( -not $isRunning ){
    Stop-PostgreSQLService $SecondaryHost
}
