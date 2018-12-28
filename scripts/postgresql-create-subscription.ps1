Param(
    [parameter(mandatory)]
    [string]$subHost,
    [parameter(mandatory)]
    [string]$pubHost
)

$ErrorActionPreference = "Stop"

$user = $env:MYCLINIC_DB_ADMIN_USER
$pass = $env:MYCLINIC_DB_ADMIN_PASS
$conn = "'host=$pubHost dbname=myclinic user=$user password=$pass'"
psql -h $subHost `
    -c "create subscription myclinic_sub connection $conn publication myclinic_pub" `
    myclinic postgres
