Param(
    [parameter(mandatory)][string]$SecondaryHost,
    [ValidateScript({$_ -notin @("localhost", "127.0.0.1")})]
    [parameter(mandatory)][string]$PrimaryHost
)

$ErrorActionPreference = "Stop"
Invoke-Expression "$PSScriptRoot\use-local-psmodules"

$isRunning = Test-PostgreSQLServiceIsRunning $SecondaryHost
New-PostgreSQLRepository $SecondaryHost
Start-PostgreSQLService $SecondaryHost
New-PostgreSQLMyClinicDatabase $SecondaryHost
Initialize-PostgreSQLMyClinicSchema $SecondaryHost
New-PostgreSQLSubscription -SecondaryHost $SecondaryHost -PrimaryHost $PrimaryHost
if( -not $isRunning ){
    Stop-PostgreSQLService $SecondaryHost
}
