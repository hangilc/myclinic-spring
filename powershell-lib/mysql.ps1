$dbHost = $env:MYCLINIC_DB_HOST
$dbUser = $env:MYCLINIC_DB_USER
$dbPass = $env:MYCLINIC_DB_PASS
$dbDatabase = "myclinic"

$env:MYSQL_PWD = $dbPass

function mysqlQuery($sql){
    $output = mysql -h "$dbHost" -u "$dbUser" $dbDatabase --batch -e "$sql"
    $output | convertfrom-csv -Delimiter "`t"
}