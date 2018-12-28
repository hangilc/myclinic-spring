Param (
    [alias('host')][string]$dbHost = 'localhost'
)

psql -h $dbHost -c "create publication myclinic_pub for all tables" myclinic postgres
