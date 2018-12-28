
Param(
    $installer,
    [string]$configTemplate = ".\config\postgresql"
)

$ErrorActionPreference = "Stop"

$repobase = "C:\pgdata"
$repomain = "$repobase\main"
$reply = Read-Host "Is config ($configTemplate) uptodate? [y/n]"
if( -not ($reply -match "[yY]") ){
    exit 1
}

Write-Host "Setting up repository (C:\pgdata)"
if( Test-Path -Path $repobase ){
    $timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
    $repobase_save = "$repobase-$timestamp"
    Rename-Item -Path $repobase -NewName $repobase_save
}
New-Item -ItemType directory -Path $repomain
New-Item -ItemType directory -Path "$repomain\cluster"
New-Item -ItemType directory -Path "$repomain\walarchive"

Write-Host "Invoking postgresql installer. $installer"
$proc = Start-Process -FilePath $installer -ArgumentList "--datadir", "$repomain\cluster", `
    "--enable-components", "server,commandlinetools", `
    "--locale", "C", "--mode", "unattended", "--servicename", "postgresql", `
    "--debugtrace", "$repomain\installer.log" `
    -Wait -PassThru

if( $proc.ExitCode -ne 0 ){
    Write-Host "Installer failed."
    exit 1
}

$out = Get-NetFirewallRule | Where-Object {$_.DisplayName -eq 'PostgreSQL'}
if( $out -eq $null ){
    Write-Host "Opening firewall port 5432."
    Start-Process -FilePath "powershell" -Verb runAs -Wait -PassThru -WindowStyle Hidden `
        -ArgumentList "New-NetFirewallRule", `
        "-Name", "PostgreSQL", "-DisplayName", "PostgreSQL", `
        "-Enabled", "True", "-Profile", "private", "-Direction", "Inbound", `
        "-Action", "Allow", "-LocalAddress", "Any", "-LocalPort", "5432", "-Protocol", "TCP" 
}

$installedDir = (Get-ChildItem -Path "C:\Program Files\PostgreSQL" `
    | Sort-Object LastWriteTime -Descending | Select-Object -first 1 `
    | Resolve-Path | Convert-Path)
if( $installedDir -ne $null ){
    $postgresqlPath = "$installedDir\bin"
    $found = $env:PATH.split(";") | where {$_ -eq $postgresqlPath}
    if( $found -eq $null ){
        Write-Host "adding $postgresqlPath to PATH"
        $env:PATH += ";$postgresqlPath"
        [Environment]::SetEnvironmentVariable("PATH", $env:PATH, "USER")
    }
}

$userObj = New-Object System.Security.Principal.NTAccount($env:UserName)
$userSID = $userObj.Translate([System.Security.Principal.SecurityIdentifier]).Value
Write-Host "userSID", $userSID
$acl = sc.exe sdshow PostgreSQL | Select-Object -Index 1
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
        Start-Process "cmd.exe" -ArgumentList "/c", "sc.exe", "sdset", "PostgreSQL", $newAcl -Verb runAs -Wait
    }
} else {
    Write-Host "Could not find DACL for PostgreSQL Service."
}

$isRunning = (Get-Service -Name "PostgreSQL" | Select-Object -ExpandProperty Status -first 1) -eq "Running"
if( $isRunning ){
    Stop-Service -Name 'PostgreSQL'
}

Copy-Item -Path "$configTemplate\postgresql.conf" -Destination "$repomain\cluster"
Copy-Item -Path "$configTemplate\pg_hba.conf" -Destination "$repomain\cluster"

Restart-Service -Name 'PostgreSQL'

psql -f "$configTemplate\initial-setup.sql" -U postgres

if( -not (Test-Path -Path "$env:AppData\postgresql") ){
	New-Item -ItemType directory "$env:AppData\postgresql"
}
$pgpassPath = [io.path]::Combine($env:AppData, 'postgresql', 'pgpass.conf')
if( ! (Test-Path -Path $pgpassPath) ){
    Write-Host "Copy pgpass.conf to $pgpassPath"
    Copy-Item -Path "$configTemplate\pgpass.conf" -Destination $pgpassPath
}