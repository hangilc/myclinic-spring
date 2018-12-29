function Get-PostgreSQLRepo(){
    "C:\pgdata\main"
}

function Get-PostgreSQLServiceName(){
    "PostgreSQL"
}

function Get-TimeStamp(){
    Get-Date -Format "yyyy-MM-dd-HHmmss"
}

function New-PostgreSQLDirectoryWithBackup($dir){
    if( Test-Path $dir ){
        $timestamp = Get-TimeStamp
        $save = "$dir-$timestamp"
        Rename-Item -Path $dir -NewName $save
    }
    New-Item -ItemType directory $dir
}

function Test-PostgreSQLServiceIsRunning($dbHost = "localhost"){
    $name = Get-PostgreSQLServiceName
    if( $dbHost -eq "localhost") {
        "Running" -eq (Get-Service -Name $serviceName `
            | Select-Object -ExpandProperty Status -first 1)
    } else {
        "Running" -eq (Invoke-Command -ComputerName $dbHost -ScriptBlock {`
                Param($name) `
                Get-Service -Name $name | Select-Object -ExpandProperty Status -first 1 `
            } -ArgumentList $name)
    }
}

function Start-PostgreSQLService($dbHost = "localhost"){
    $name = Get-PostgreSQLServiceName
    if( $dbHost -eq "localhost" ){
        Start-Service $name
    } else {
        Invoke-Command -ComputerName $dbHost -ScriptBlock { `
            Param($name) `
            Start-Service $name `
        } -ArgumentList $name
    }
}

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

function Initialize-PostgreSQLMyClinicSchema(){
    Param (
        [alias('host')][string] $dbHost = 'localhost',
        [string]$admin = $env:MYCLINIC_DB_ADMIN_USER,
        [string]$staff = $env:MYCLINIC_DB_USER
    )

    psql -h $dbHost -c "create database myclinic owner $admin" -U postgres
    psql -h $dbHost -c "grant all on database myclinic to $admin" -U postgres
    psql -h $dbHost -c "grant all on database myclinic to $staff" -U postgres
    Push-Location -Path 'postgresql-dev\sql'
    psql -h $dbHost -f 'create-master-tables.sql' myclinic $admin
    psql -h $dbHost -f 'create-data-tables.sql' myclinic $admin
    Pop-Location
}

Export-ModuleMember -Function *-PostgreSQL*
