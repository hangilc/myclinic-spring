function Get-RepoBase(){
    "C:\pgdata"
}

Export-ModuleMember -Function Get-RepoBase

function Get-Repo(){
    $base = Get-RepoBase
    "$base\main"
}

Export-ModuleMember -Function Get-Repo

function Ask-YesOrNo($prompt){
    (Read-Host "$prompt [y/n]") -match "[yY]"
}

Export-ModuleMember -Function Ask-YesOrNo

function Get-TimeStamp(){
    Get-Date -Format "yyyy-MM-dd-HHmmss"
}

Export-ModuleMember -Function Get-TimeStamp

function BackupAndCreate-Directory($dir){
    if( -not (Test-Path $dir) ){
        $timestamp = Get-TimeStamp
        $save = "$dir-$timestamp"
        Rename-Item -Path $dir -NewName $save
    }
    New-Item -ItemType directory $dir
}

Export-ModuleMember -Function BackupAndCreate-Directory

function Get-PostgreSQLServiceName(){
    "PostgreSQL"
}

Export-ModuleMember -Function Get-PostgreSQLServiceName

function Stop-PostgreSQLService($dbHost = "localhost"){
    $name = Get-PostgreSQLServiceName
    if( $dbHost -eq "localhost" ){
        Stop-Service $name
    } else {
        Invoke-Command -ComputerName $dbHost -ScriptBlock { `
            Param($name) `
            Stop-Service $name `
        } -ArgumentList $name
    }
}

Export-ModuleMember -Function Stop-PostgreSQLService
