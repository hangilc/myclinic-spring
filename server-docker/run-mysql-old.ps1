Param(
    [int]$Port = 3306,
    [String]$Conf = "",
    [string]$Sql = "",
    [string]$User = "user",
    [string]$Pass = "pass",
    [string]$Database = "myclinic"
)

function AddVolume($src, $dst){
    $abs = Resolve-Path $src
    $file = [System.IO.Path]::GetFileName($src)
    return " -v ${abs}:$dst/$file"
}

$prog = "docker run"
$prog += " -e MYSQL_ALLOW_EMPTY_PASSWORD=yes"
$prog += " -e MYSQL_USER=$User"
$prog += " -e MYSQL_PASSWORD=$Pass"
$prog += " -e MYSQL_DATABASE=$Database"
$prog += " -p ${Port}:3306"
$prog += " -d"
if( $Conf -ne "" ){
    if( Test-Path $Conf ){
        $ContainerConfPath = "/etc/mysql/conf.d"
        if( (Get-Item $Conf) -is [System.IO.DirectoryInfo] ){
            $items = (Get-ChildItem $Conf -Filter "*.cnf")
            foreach($item in $items){
                $prog += (AddVolume $item.FullName $ContainerConfPath)
            }
        } else {
            $prog += (AddVolume $Conf $ContainerConfPath)
        }
    } else {
        throw "No such file/direcotry $Conf"
    }
}
if( $Sql -ne "" ){
    if( Test-Path $Sql ){
        $ContainerDataPath = "/docker-entrypoint-initdb.d"
        if( (Get-Item $Sql) -is [System.IO.DirectoryInfo] ){
            $abs = Resolve-Path $Sql
            $prog += " -v ${abs}:$ContainerDataPath"
        } else {
            $abs = Resolve-Path $Sql
            $file = [System.IO.Path]::GetFileName($Sql)
            $prog += " -v ${abs}:$ContainerDataPath/$file"
        }
    } else {
        throw "No such file/direcotry $Sql"
    }
}
$prog += " mysql:5.7"

echo $prog
Invoke-Expression $prog
