# @echo off
# rem Usage: install-postgresql installer.exe
# rem %1 --datadir C:\pgdata\main\cluster --enable-components server,commandlinetools --locale C ^
# rem 	--mode unattended --servicename postgresql
# echo "PostgreSQL has been installed."
# set /P yesno="Open firewall port 5432? (Y/N): "
# if "%yesno%" == "Y" (
# 	powershell -Command "start-process netsh -Verb runas -Argument 'advfirewall firewall add rule action=allow dir=in protocol=tcp name=PostgreSQL profile=private localport=5432'"
# 	echo "    Port 5432 has been opend."
# )
# echo "Add bin path to PATH environment variable."

Param(
    $installer,
    [switch]$SkipRepositorySetup = $false,
    [switch]$SkipInstaller = $false
)

$repobase = "C:\pgdata"

if( -not $SkipRepositorySetup ){
    Write-Host "Setting up repository (C:\pgdata)"
    if( Test-Path -Path $repobase ){
        $timestamp = Get-Date -Format "yyyy-MM-dd-HHmmss"
        $repobase_save = "$repobase-$timestamp"
        Rename-Item -Path $repobase -NewName $repobase_save
    }
    $repomain = "$repobase\main"
    New-Item -ItemType directory -Path $repomain
    New-Item -ItemType directory -Path "$repomain\cluster"
    New-Item -ItemType directory -Path "$repomain\walarchive"
    New-Item -ItemType directory -Path "$repomain\backup"
}

if( -not $SkipInstaller ){
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

