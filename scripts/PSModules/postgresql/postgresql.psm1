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

function Test-PostgreSQLServiceIsRunning(){
    Param (
        [string]$DbHost = "localhost"
    )
    $name = Get-PostgreSQLServiceName
    "Running" -eq (Get-Service -ComputerName $DbHost -Name $name | 
        Select-Object -ExpandProperty Status -first 1)
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
        } -ArgumentList $name -EnableNetworkAccess
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
        } -ArgumentList $name -EnableNetworkAccess
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
        [string]$ConfigTemplate = ".\config\postgresql",
        [string]$SuperPass = $env:MYCLINIC_POSTGRES_SUPER_PASS
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
        Start-Process initdb -WindowStyle hidden -Wait -PassThru `
            -ArgumentList "-D", "$repo\cluster", "-E", "UTF8", "--no-locale", `
            "-U", "postgres", "--auth=ident"
    } -ArgumentList $repo -EnableNetworkAccess
    $session = New-PSSession -ComputerName $DbHost -EnableNetworkAccess
    Copy-Item -ToSession $session -Path "$ConfigTemplate\postgresql.conf" -Destination "$repo\cluster"
    Copy-Item -ToSession $session -Path "$ConfigTemplate\pg_hba.conf" -Destination "$repo\cluster"
    Remove-PSSession $session
    Start-PostgreSQLService $DbHost
    Invoke-Command -ComputerName $DbHost -ScriptBlock {
        Param($SuperPass)
        psql -c "alter role postgres password '$SuperPass'" -U postgres
        } -ArgumentList $SuperPass -EnableNetworkAccess
    psql -h $DbHost -f "$ConfigTemplate\initial-setup.sql" -U postgres
    if( -not $isRunning ){
        Stop-PostgreSQLService $DbHost
    }
}

function Query(){
    Param(
        [string][parameter(mandatory)]$Sql,
        [string][alias('Host')]$DbHost = "localhost",
        [string]$User = $env:MYCLINIC_DB_ADMIN_USER
    )
    $env:PGCLIENTENCODING="SJIS"
    @(psql -h $DbHost -c "select row_to_json(t) from ($sql) t" -t myclinic $user | 
        Where-Object { -not [string]::IsNullOrWhitespace($_) } |
        ConvertFrom-Json)
}

function Get-PostgreSQLPublication(){
    Param(
        [string][alias('Host')]$DbHost = "localhost"
    )
    Query "select * from pg_publication" $DbHost postgres
}

function New-PostgreSQLPublication(){
    Param(
        [string][alias('Host')]$DbHost = "localhost"
    )
    psql -h $dbHost -c "create publication myclinic_pub for all tables" myclinic postgres
}

function Get-PostgreSQLReplicationState(){
    Param(
        [string][alias('Host')]$DbHost = "localhost"
    )
    Query "select * from pg_stat_replication" -Host $DbHost -User postgres
}

function New-PostgreSQLSubscription(){
    [CmdletBinding(PositionalBinding=$false)]
    Param(
        [string][parameter(mandatory)] $SecondaryHost,
        [ValidateScript({$_ -notin @("localhost", "127.0.0.1")})]
        [string][parameter(mandatory)]$PrimaryHost,
        [string]$User = $env:MYCLINIC_DB_ADMIN_USER,
        [string]$Pass = $env:MYCLINIC_DB_ADMIN_PASS
    )
    $conn = "'host=$PrimaryHost dbname=myclinic user=$user password=$pass'"
    psql -h $SecondaryHost `
        -c "create subscription myclinic_sub connection $conn publication myclinic_pub" `
        myclinic postgres
}

function Get-PostgreSQLConnectingSecondary(){
    Param(
        [string][alias('Host')]$DbHost = "localhost"
    )
    $res = Query "select client_addr, client_hostname from pg_stat_replication" -Host $DbHost `
        -User postgres
    $d = @{}
    $res | foreach {
        if( $_.client_hostname ){
            $d[$_.client_hostname] = $true
        } elseif( $_.client_addr ){
            $host = [Net.DNS]::GetHostEntry($_.client_addr).HostName
            if( $host ){
                $d[$host] = $true
            }
        } else {
            $null
        }
    }
    @($d.PSBase.keys)
}

Export-ModuleMember -Function *-PostgreSQL*
