Param(
    [string][alias("Host")]$DbHost = "localhost",
    [switch]$SkipNewRepository = $false
)
$ErrorActionPreference = "Stop"
Invoke-Expression "$PSScriptRoot\use-local-psmodules"

if( $DbHost -ne "localhost" ){
    Write-Error "Currently, only localhost is supported."
    exit 1
}

if( -not $SkipNewRepository ){
    New-PostgreSQLRepository $DbHost
}
$src = "mysql"
$dst = "postgresql"
Initialize-PostgreSQLMyClinicSchema -DbHost $dbHost
my-pyenv\Scripts\activate.ps1
$pythonPathSave = $env:PYTHONPATH
$env:PYTHONPATH="db\scripts"
python db\scripts\xfer-db.py $src $dst
$env:PYTHONPATH = $pythonPathSave
deactivate
New-PostgreSQLPublication $DbHost