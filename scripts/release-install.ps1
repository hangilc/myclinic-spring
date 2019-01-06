Param (
    [string]$Label = "",
    [string]$Repository = $env:MYCLINIC_REPOSITORY
)

function saveAsUTF8($absPath, $content){
    $enc = New-Object System.Text.UTF8Encoding $false
    [System.IO.File]::WriteAllText($absPath, $content, $enc)
}

$ts = Get-Date -Format "yyyyMMdd-HHmmss"
if( -not $Repository ){
    throw "Myclinic Repository is not defined."
}
$folder = "$Repository\myclinic-release-$ts"
New-Item -ItemType directory $folder
Copy-Item hotline\target\hotline-1.0.0-SNAPSHOT.jar -Destination "$folder\hotline.jar"
Copy-Item intraclinic\target\intraclinic-1.0.0-SNAPSHOT.jar -Destination "$folder\intraclinic.jar"
Copy-Item management\target\management-1.0.0-SNAPSHOT-jar-with-dependencies.jar -Destination "$folder\management.jar"
Copy-Item pharma\target\pharma-1.0.0-SNAPSHOT.jar -Destination "$folder\pharma.jar"
Copy-Item practice\target\practice-1.0.0-SNAPSHOT.jar -Destination "$folder\practice.jar"
Copy-Item rcpt\target\rcpt-1.0.0-SNAPSHOT.jar -Destination "$folder\rcpt.jar"
Copy-Item rcpt-drawer\target\rcpt-drawer-1.0.0-SNAPSHOT.jar -Destination "$folder\rcpt-drawer.jar"
Copy-Item reception\target\reception-1.0.0-SNAPSHOT.jar -Destination "$folder\reception.jar"
Copy-Item record-browser\target\record-browser-1.0.0-SNAPSHOT.jar -Destination "$folder\record-browser.jar"
Copy-Item scanner\target\scanner-1.0.0-SNAPSHOT.jar -Destination "$folder\scanner.jar"
Copy-Item server\target\server-1.0.0-SNAPSHOT.jar -Destination "$folder\server.jar"
dir config -Exclude ".git" | foreach {
    $name = $_.Name
    Copy-Item "config\$name" -Destination "$folder\config\$name" -Recurse
}
New-Item -ItemType File "$folder\myclinic-release-$ts.txt"
if( $label ){
    saveAsUTF8 "$folder\label.txt" $label
}
$current = "$Repository\current"
if( Test-Path $current ){
    [io.directory]::Delete($current)
}
New-Item -ItemType Junction $current -Target $folder