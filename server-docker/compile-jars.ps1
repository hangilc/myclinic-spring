function ToDockerPath($path)
{
    [regex]$regex="^(.):"
    $path = $regex.Replace("$path", {$args[0].Value.ToLower()})
    $dp = $path -replace ":?\\","/"
    return "/$dp"
}

$java = ToDockerPath $([System.IO.Path]::GetFullPath($(Join-Path (pwd) "..")))
$m2 = $(ToDockerPath $HOME) + "/.m2"
docker run -it --rm -v "${java}:/usr/src/mymaven" -v "${m2}:/root/.m2" -w "/usr/src/mymaven" `
    maven:3.6.2-jdk-11 mvn clean install
