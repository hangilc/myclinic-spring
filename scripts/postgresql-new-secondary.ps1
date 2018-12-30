Param(
    [parameter(mandatory)][string]$SecondaryHost,
    [ValidateScript({$_ -notin @("localhost", "127.0.0.1")})]
    [parameter(mandatory)][string]$PrimaryHost
)

$ErrorActionPreference = "Stop"
Invoke-Expression "$PSScriptRoot\use-local-psmodules"

$isRunning

$user = $env:MYCLINIC_DB_ADMIN_USER
$pass = $env:MYCLINIC_DB_ADMIN_PASS
$conn = "'host=$pubHost dbname=myclinic user=$user password=$pass'"
psql -h $subHost `
    -c "create subscription myclinic_sub connection $conn publication myclinic_pub" `
    myclinic postgres
