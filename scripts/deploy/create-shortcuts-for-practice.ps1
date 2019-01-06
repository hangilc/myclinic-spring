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

Link "�T�[�o�[" "server.jar" -cli
Link "�z�b�g���C���i��t�j" "hotline.jar" -ExtraArgs "practice", "reception"
