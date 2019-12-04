Param (
    [parameter(mandatory)]
    [string]$MasterHost,
     
    [parameter(mandatory)]
    [int]$SlaveServerId = 2
)

$tmpl = (Get-Content "./slave-template.cnf")
$slaveConf = $ExecutionContext.InvokeCommand.ExpandString($tmpl)
Set-Content "./slave.cnf" $slaveConf
