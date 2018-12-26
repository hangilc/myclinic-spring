Param (
    [string]$saveDir = "$($env:userprofile)\db-backup"
)

if( -not (Test-Path -Path $saveDir) ){
    New-Item -ItemType directory -Path $saveDir
}
$timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
$backup = "$saveDir\myclinic-backup-$timestamp.sql"
pg_dumpall -U postgres -f $backup
Write-Host "Backup has been created at: $backup"