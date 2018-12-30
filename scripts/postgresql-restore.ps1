Param (
    [string][parameter(mandatory, position=0)]$BackupFile,
    [string][alias('Host')]$DbHost = "localhost",
    [boolean]$skipCreateRepository = $false
)

$ErrorActionPreference = "Stop"
Invoke-Expression "$PSScriptRoot\use-local-psmodules"

if( -not $skipCreateRepository ){
    New-PostgreSQLRepository $DbHost
}

$isRunning = Test-PostgreSQLServiceIsRunning $DbHost
if( -not $isRunning ){
    Start-PostgreSQLService $DbHost
}
New-PostgreSQLMyClinicDatabase $DbHost
$admin = $env:MYCLINIC_DB_ADMIN_USER
pg_restore -h $DbHost -d myclinic -U $admin $BackupFile
New-PostgreSQLPublication $DbHost
if( -not $isRunning ){
    Stop-PostgreSQLService $DbHost
}
