Param (
    [int]$Code,
    $Field,
    $Kind
)

$key = $Field -replace '[ÅiÅjÅ|]',""
$sql = switch ($Kind) {
    's' { "select distinct(name) from shinryou_master where shinryoucode = $Code" }
    'k' { "select distinct(name) from kizai_master where kizaicode = $Code" }
}
$names = [array](Invoke-PostgreSQLQuery($sql) | % { $_.name})
if( $names.length -eq 0 ){
   Throw "Cannot find name for $Field"
} elseif( ($names.length -eq 1) -and ($Field -eq $names[0]) -and ($key -eq $Field) ){
"public int $key;"
} else {
   $qnames = $names | % { "`"$_`"" }
   "@MasterNameMap(candidates={$($qnames -join ",")})"
   "public int $key;"
}

