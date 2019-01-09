Param(
    [string][alias('Host')]$DbHost = "localhost"
)

& "$PSScriptRoot\use-local-psmodules.ps1"
Get-PostgreSQLReplicationStatus $DbHost
