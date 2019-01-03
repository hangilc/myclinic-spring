Param (
    [string][parameter(mandatory, position=0)]$BackupFile,
    [string][alias('Host')]$DbHost = "localhost",
    [switch]$skipCreateRepository = $false,
    [string]$Owner = $env:MYCLINIC_DB_ADMIN_USER,
    [string]$User = $env:MYCLINIC_DB_USER
)

$ErrorActionPreference = "Stop"
Invoke-Expression "$PSScriptRoot\use-local-psmodules"

$isRunning = Test-PostgreSQLServiceIsRunning $DbHost
if( -not $skipCreateRepository ){
    New-PostgreSQLRepository $DbHost
}
Start-PostgreSQLService $DbHost
New-PostgreSQLMyClinicDatabase $DbHost
pg_restore -h $DbHost -d myclinic -U $Owner $BackupFile
Grant-PostgreSQLUserPrivilege -Host $DbHost -User $User
New-PostgreSQLPublication $DbHost
if( $isRunning ){
    Start-PostgreSQLService $DbHost
} else {
    Stop-PostgreSQLService $DbHost
}
