Param(
    [string][alias('Host')]$DbHost = "localhost",
    [string]$SaveDir = ""
)

$block = {
    Param($SaveDir)
    dir "$SaveDir/*.dump"
    dir "$SaveDir/*.sql"
}

if( $SaveDir -eq "" ){
    $SaveDir = Get-PostgreSQLDefaultBackupDir -Host $DbHost
}

if( $DbHost -in ".", "localhost", "127.0.0.1" ){
    Invoke-Command -ScriptBlock $block -ArgumentList $SaveDir |
        Select-Object -property LastWriteTime, Length, FullName
} else {
    Invoke-Command -ComputerName $DbHost -ScriptBlock $block -ArgumentList $SaveDir |
        Select-Object -property LastWriteTime, Length, FullName
}
