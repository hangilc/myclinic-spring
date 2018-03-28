param(
    [parameter(mandatory)][string]$sourceFile
)

. ./master-lib.ps1

restoreMaster $sourceFile

