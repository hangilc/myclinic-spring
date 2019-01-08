Param (
    [string]$Destination = $env:MYCLINIC_PHARMA_PROG,
    [string]$Repository = $env:MYCLINIC_REPOSITORY
)

Set-StrictMode -Version Latest

if( -not (Test-Path $Destination) ){
    New-Item -ItemType Directory $Destination
}
$current = "$Repository\current"
if( -not (Test-Path $current) ){
    throw "Cannot find current repository."
}
Copy-Item "$current\hotline.jar" -Destination $Destination
Copy-Item "$current\intraclinic.jar" -Destination $Destination
Copy-Item "$current\management.jar" -Destination $Destination
Copy-Item "$current\pharma.jar" -Destination $Destination
Copy-Item "$current\record-browser.jar" -Destination $Destination
Copy-Item "$current\scanner.jar" -Destination $Destination
Copy-Item "scripts\deploy\create-shortcuts-for-pharma.ps1" -Destination $Destination
