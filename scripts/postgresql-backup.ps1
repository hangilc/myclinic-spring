Param (
    [string]$saveDir = "$env:userprofile\db-backup"
)

if( -not (Test-Path -Path $saveDir) ){
    New-Item -ItemType directory -Path $saveDir
}
$timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
$backup = "$saveDir\myclinic-postgresql-backup-$timestamp.dump"
pg_dump -U postgres -f $backup -E UTF8 -Fc --verbose myclinic
Write-Host "Backup has been created at: $backup"