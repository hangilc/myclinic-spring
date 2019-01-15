$modulepath = resolve-path $PSScriptRoot\PSModules
$current = $env:PSModulePath.split(";")
if( $modulepath -notin $current ){
    if( $env:PSModulePath ){
        $env:PSModulePath += ";$modulepath"
        } else {
            $env:PSModulePath = "$modulepath"
        }
}
