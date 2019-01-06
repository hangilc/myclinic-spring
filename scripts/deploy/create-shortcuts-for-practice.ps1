Param(
    [string]$Destination = (Get-Location).Path
)

$ScriptShell = New-Object -ComObject WScript.Shell

function CreateShortcut($path, $target, [string]$arguments, $workingDirectory){
    $shortcut = $ScriptShell.CreateShortcut($path)
    $shortcut.TargetPath = $target
    $shortcut.Arguments = $arguments
    $shortcut.WorkingDirectory = $workingDirectory
    $shortcut.Save()
}

function Link(){
    Param(
        [string]$Name,
        [string]$Jar,
        [string[]]$ExtraArgs = @(),
        [switch]$Cli
    )
    $JarPath = Join-Path (Resolve-Path .) $Jar
    $Target = if( $Cli ){
        '"%JAVA_HOME%\bin\java.exe"'
    } else {
        '"%JAVA_HOME%\bin\javaw.exe"'
    }
    $Args = @("-jar", "`"$JarPath`"") + $ExtraArgs
    $WorkDir = (Get-Location).Path
    CreateShortcut (Join-Path $Destination "$Name.lnk") $Target `
        ([string]$Args) "`"$WorkDir`""
}

Link "サーバー" "server.jar" -cli
Link "ホットライン（受付）" "hotline.jar" -ExtraArgs "practice", "reception"
Link "ホットライン（薬局）" "hotline.jar" -ExtraArgs "practice", "pharmacy"
Link "受付" "reception.jar"
Link "院内ミーティング" "intraclinic.jar"
Link "診療録" "record-browser.jar"
Link "診察" "practice.jar"
Link "薬局" "pharma.jar"
Link "スキャナー" "scanner.jar"
Link "レセプト印刷" "rcpt-drawer.jar"
