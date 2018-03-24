Param(
    [parameter(mandatory)]$name,
    $version="1.0.0-SNAPSHOT",
    $groupId=$env:PROJECT_GROUP_ID
)

if (!$groupId) {
    echo "Error: groupId is not specified"
    exit 1
}

mvn archetype:generate -DarchetypeArtifactId=maven-archetype-quickstart -DgroupId="$groupId" -DartifactId="$name" -Dversion="$version" -DinteractiveMode=false


