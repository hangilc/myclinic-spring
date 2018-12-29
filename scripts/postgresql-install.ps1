Param(
    [parameter(mandatory)][string]$installer,
    [string]$ConfigTemplate = ".\config\postgresql",
    [switch]$SkipInstaller = $false,
    [switch]$SkipConfiguration = $false
)

$ErrorActionPreference = "Stop"
Invoke-Expression $PSScriptRoot\use-local-psmodules

if( (-not $skipConfiguration) -and (-not (Test-Path -Path $configTemplate)) ){
    Write-Host "Configuration template ($configTemplate) does not exits."
    exit 1
}

$repomain = Get-PostgreSQLRepo
$serviceName = Get-PostgreSQLServiceName

if( -not $skipInstaller ){
    Write-Host "Setting up repository (C:\pgdata)"
    New-PostgreSQLDirectoryWithBackup $repomain
    New-Item -ItemType directory -Path "$repomain\cluster"
    New-Item -ItemType directory -Path "$repomain\walarchive"

    Write-Host "Invoking postgresql installer. $installer"
    $proc = Start-Process -FilePath $installer -ArgumentList "--datadir", "$repomain\cluster", `
        "--enable-components", "server,commandlinetools", `
        "--locale", "C", "--mode", "unattended", "--servicename", $serviceName, `
        "--debugtrace", "$repomain\installer.log" `
        -Wait -PassThru

    if( $proc.ExitCode -ne 0 ){
        Write-Host "Installer failed."
        exit 1
    }
}

if( -not $skipConfiguration ){
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

    $isRunning = Test-PostgreSQLServiceIsRunning
    if( $isRunning ){
        Stop-PostgreSQLService
    }

    Copy-Item -Path "$configTemplate\postgresql.conf" -Destination "$repomain\cluster"
    Copy-Item -Path "$configTemplate\pg_hba.conf" -Destination "$repomain\cluster"

    Start-PostgreSQLService

    psql -f "$configTemplate\initial-setup.sql" -U postgres

    if( -not $isRunning ){
        Stop-PostgreSQLService
    }

    if( -not (Test-Path -Path "$env:AppData\postgresql") ){
    	New-Item -ItemType directory "$env:AppData\postgresql"
    }
    $pgpassPath = [io.path]::Combine($env:AppData, 'postgresql', 'pgpass.conf')
    if( ! (Test-Path -Path $pgpassPath) ){
        Write-Host "Copy pgpass.conf to $pgpassPath"
        Copy-Item -Path "$configTemplate\pgpass.conf" -Destination $pgpassPath
    }

}