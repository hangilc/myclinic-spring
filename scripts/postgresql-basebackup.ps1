Param (
    [string][alias('Host')]$DbHost = "localhost",
    [string]$Label = ''
)

$ErrorActionPreference = "Stop"
& "$PSScriptRoot\use-local-psmodules"

$repo = Get-PostgreSQLRepository

$block = {
    Param($repo, $label)
    $backup = "$repo\backup"
    if( Test-Path -Path $backup ){
        $timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
        Rename-Item -Path $backup -NewName "$backup-$timestamp"
    }
    New-Item -ItemType directory -Path $backup
    pg_basebackup -h localhost --checkpoint=fast -D $backup -U postgres --label "'$label'"
}

if( $DbHost -in ".", "localhost", "127.0.0.1"){
    Invoke-Command -ScriptBlock $block -ArgumentList $repo, $Label
} else {
    Invoke-Command -ScriptBlock $block -ArgumentList $repo, $Label -ComputerName $DbHost
}
