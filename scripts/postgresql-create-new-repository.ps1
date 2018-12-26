$ErrorActionPreference = "Stop"

$isRunning = "Running" -eq (Get-Service -Name 'PostgreSQL' `
    | Select-Object -ExpandProperty Status)
if( $isRunning ){
    Stop-Service 'PostgreSQL'
}

$repo = 'C:\pgdata\main'
if( Test-Path -Path $repo ){
    $timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
    Rename-Item -Path $repo -NewName "$repo-$timestamp"
}

New-Item -ItemType directory -Path $repo
New-Item -ItemType directory -Path "$repo\cluster"
New-Item -ItemType directory -Path "$repo\walarchive"

initdb -D "$repo\cluster" -E UTF8 --no-locale -U postgres

Copy-Item -Path 'config\postgresql\postgresql.conf' -Destination "$repo\cluster"
Copy-Item -Path 'config\postgresql\pg_hba.conf' -Destination "$repo\cluster"

if( $isRunning ){
    Start-Service 'PostgreSQL'
}