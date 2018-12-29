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

Export-ModuleMember -Function *-PostgreSQL*
