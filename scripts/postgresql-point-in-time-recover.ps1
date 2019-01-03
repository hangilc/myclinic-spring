[CmdletBinding(PositionalBinding=$false)]
Param (
    [string][alias('Host')]$DbHost = "localhost",
    [ValidatePattern('\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}')]
    [string]$At = "",
    [ValidateSet("pause", "promote", "shutdown")]
    [string]$TargetAction = "promote"
)

$ErrorActionPreference = "Stop"
& "$PSScriptRoot\use-local-psmodules"

$repo = Get-PostgreSQLRepository
$targetTime = ''
if( $at ){
    Write-Host "recovering to $at"
    $targetTime = "recovery_target_time = '$at'"
}

$isRunning = Test-PostgreSQLServiceIsRunning $DbHost
if( $isRunning ){
    Stop-PostgreSQLService $DbHost
}

$block = {
    Param($repo, $targetAction, $targetTime)
    $backup = "$repo\backup"
    $cluster = "$repo\cluster"
    $timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
    $clusterSave = "$cluster-$timestamp"
    Rename-Item -Path $cluster -NewName $clusterSave
    Copy-Item $backup $cluster -recurse
    Remove-Item "$cluster\pg_wal" -recurse -force
    Copy-Item "$clusterSave\pg_wal" "$cluster\pg_wal" -recurse
    $walarch = "$repo\walarchive" -replace "\\", "\\"
    $recovery = @"
restore_command = 'copy "$walarch\\%f" "%p"'
recovery_target_action = '$targetAction'
$targetTime
"@
    Write-Host $recovery
    $recovery | Out-File -FilePath "$cluster\recovery.conf" -Encoding ASCII
}

if( $DbHost in ".", "localhost", "127.0.0.1" ){
    Invoke-Command -ScriptBlock $block -ArgumentList $repo, $targetAction, $targetTime
} else {
    Invoke-Command -ComputerName $DbHost `
                   -ScriptBlock $block -ArgumentList $repo, $targetAction, $targetTime 
}

Start-PostgreSQLService $DbHost
