Param (
    [parameter(mandatory)]
    [string]$backupFile,
    [boolean]$skipCreateRepository = $false
)

$ErrorActionPreference = "Stop"

$isRunning = "Running" -eq (Get-Service -Name 'PostgreSQL' `
    | Select-Object -ExpandProperty Status)
if( $isRunning ){
    Stop-Service 'PostgreSQL'
}

if( -not $skipCreateRepository ){
    invoke-expression "$PSScriptRoot\postgresql-create-new-repository.ps1"
}

Start-Service 'PostgreSQL'

#psql -U postgres -v ON_ERROR_STOP=1 -f $backupFile
psql -U postgres -f $backupFile

