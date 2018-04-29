Param (
    [parameter(mandatory)]$name,
    [parameter(mandatory)]$groupId,
    $version="1.0.0-SNAPSHOT"
)

$package = "${groupId}.${name}" -replace "-", ""

mvn archetype:generate -DarchetypeGroupId="jp.chang.myclinic" -DarchetypeArtifactId="archetype-lib" `
    -DgroupId="$groupId" -DartifactId="$name" -Dversion="$version" `
    -Dpackage="$package" -DinteractiveMode=false -DarchetypeCatalog=local
