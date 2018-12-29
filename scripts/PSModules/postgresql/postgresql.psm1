function Get-PostgreSQLRepository(){
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
        Invoke-Command -ComputerName $dbHost -ScriptBlock {
                Param($name) 
                "Running" -eq (Get-Service -Name $name | `
                    Select-Object -ExpandProperty Status -first 1)
            } -ArgumentList $name
    }
}

function Enable-PostgreSQLServiceCommand(){
    $serviceName = Get-PostgreSQLServiceName
    $userObj = New-Object System.Security.Principal.NTAccount($env:UserName)
    $userSID = $userObj.Translate([System.Security.Principal.SecurityIdentifier]).Value
    Write-Host "userSID", $userSID
    $acl = sc.exe sdshow $serviceName | Select-Object -Index 1
    $dacl = ""
    $sacl = ""
    $daclIndex = $acl.indexOf("D:")
    $saclIndex = $acl.indexOf("S:")
    if( $daclIndex -ge 0 ){
        if( $saclIndex -lt 0 ){
            $dacl = $acl
        } elseif ( $saclIndex -gt $daclIndex ){
            $dacl = $acl.substring($daclIndex, $saclIndex - $daclIndex)
            $sacl = $acl.substring($saclIndex)
        } else {
            $dacl = $acl.substring($daclIndex)
            $sacl = $acl.substring($saclIndex, $daclIndex - $saclIndex)
        }
        Write-Host "dacl", $dacl
        Write-Host "sacl", $scal
        if( !$dacl.Contains($userSID) ){
            $ace = "(A;;RPWP;;;$userSID)"
            $newAcl = "$dacl$ace$sacl"
            Write-Host $newAcl
            Write-Host "Allowing current user to start/stop PostgreSQL Service"
            Start-Process "cmd.exe" -ArgumentList "/c", "sc.exe", "sdset", $serviceName, `
                $newAcl -Verb runAs -Wait
        }
    } else {
        Write-Host "Could not find DACL for $serviceName Service."
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
        Invoke-Command -ComputerName $dbHost -ScriptBlock { 
            Param($name) 
            Stop-Service $name 
        } -ArgumentList $name
    }
}

function New-PostgreSQLMyClinicDatabase(){
    Param (
        [alias('host')][string] $DbHost = 'localhost',
        [string]$Admin = $env:MYCLINIC_DB_ADMIN_USER,
        [string]$Staff = $env:MYCLINIC_DB_USER
    )
    psql -h $dbHost -c "create database myclinic owner $admin" -U postgres
    psql -h $dbHost -c "grant all on database myclinic to $admin" -U postgres
    psql -h $dbHost -c "grant all on database myclinic to $staff" -U postgres
}

function Initialize-PostgreSQLMyClinicSchema(){
    Param (
        [alias('host')][string] $DbHost = 'localhost',
        [string]$admin = $env:MYCLINIC_DB_ADMIN_USER
    )

    Push-Location -Path 'postgresql-dev\sql'
    psql -h $dbHost -f 'create-master-tables.sql' myclinic $admin
    psql -h $dbHost -f 'create-data-tables.sql' myclinic $admin
    Pop-Location
}

function New-PostgreSQLRepository(){
    Param(
        [string][alias('Host')][parameter(position=0)]$DbHost = "localhost",
        [string]$ConfigTemplate = ".\config\postgresql"
    )
    $repo = Get-PostgreSQLRepository
    $isRunning = Test-PostgreSQLServiceIsRunning $DbHost
    if( $isRunning ){
        Stop-PostgreSQLService $DbHost
    }
    Invoke-Command -ComputerName $DbHost -ScriptBlock { 
        Param($repo)
        if( Test-Path -Path $repo ){
            $timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
            $save = "$repo-$timestamp"
            Rename-Item -Path $repo -NewName $save
        }
        New-Item -ItemType directory -Path $repo
        New-Item -ItemType directory -Path "$repo\cluster"
        New-Item -ItemType directory -Path "$repo\walarchive"

        initdb -D "$repo\cluster" -E UTF8 --no-locale -U postgres --auth=ident
    } -ArgumentList $repo
    $session = New-PSSession -ComputerName $DbHost
    Copy-Item -ToSession $session -Path "$ConfigTemplate\postgresql.conf" -Destination "$repo\cluster"
    Copy-Item -ToSession $session -Path "$ConfigTemplate\pg_hba.conf" -Destination "$repo\cluster"
    Remove-PSSession $session
    Start-PostgreSQLService $DbHost
    psql -f "$ConfigTemplate\initial-setup.sql" -U postgres
    if( -not $isRunning ){
        Stop-PostgreSQLService $DbHost
    }
}

function Get-PostgreSQLPublication(){
    Param(
        [string][alias('Host')]$DbHost = "localhost"
    )
    psql -c "select pubname from pg_publication" -t myclinic postgres
}

Export-ModuleMember -Function *-PostgreSQL*
