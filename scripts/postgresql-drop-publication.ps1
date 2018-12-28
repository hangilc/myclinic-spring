Param (
    [alias('host')][string]$dbHost = 'localhost'
)

psql -h $dbHost -c "drop publication myclinic_pub" myclinic postgres
