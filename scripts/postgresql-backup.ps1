Param (
    [string]$SaveDir = "$env:userprofile\db-backup",
    [string][alias('Host')]$DbHost = "localhost"
)

$block = {
    Param($saveDir, $backup)
    if( -not (Test-Path -Path $SaveDir) ){
        New-Item -ItemType directory -Path $saveDir
    }
    cmd.exe /c "pg_dump -U postgres -f `"$backup`" -E UTF8 -Fc --verbose myclinic 2>&1"
}

$timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
$backup = "$saveDir\myclinic-postgresql-backup-$timestamp.dump"

if( $DbHost -in ".", "localhost", "127.0.0.1" ){
    Invoke-Command -ScriptBlock $block -ArgumentList $SaveDir, $backup
} else {
    Invoke-Command -ScriptBlock $block -ArgumentList $SaveDir, $backup -ComputerName $DbHost
}
Write-Host "Backup has been created at: $backup (Host $DbHost)"