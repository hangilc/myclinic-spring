Param(
    [string]$DbHost = "localhost",
    [string]$DbUser = "$env:MYCLINIC_DB_ADMIN_USER",
    [string]$DbPass = "$env:MYCLINIC_DB_ADMIN_PASS",
    [string]$SavePath = "./data/production-sql/dump.sql"
)

mysqldump -h "$DbHost" -u "$env:MYCLINIC_DB_ADMIN_USER" -p"$env:MYCLINIC_DB_ADMIN_PASS" `
    --result-file "$SavePath" `
    --single-transaction --master-data myclinic 