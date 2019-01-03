$modulepath = resolve-path $PSScriptRoot\PSModules
$current = $env:PSModulePath.split(";")
if( $modulepath -notin $current ){
    $env:PSModulePath += ";$modulepath"
}
