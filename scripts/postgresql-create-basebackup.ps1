Param (
    [string]$label = ''
)

$repo = 'C:\pgdata\main'
$backup = "$repo\backup"
if( Test-Path -Path $backup ){
    $timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
    $backupSave = "$backup-$timestamp"
    Rename-Item -Path $backup -NewName $backupSave
}
New-Item -ItemType directory -Path $backup
pg_basebackup -D $backup -U postgres --verbose --label $label