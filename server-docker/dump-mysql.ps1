$SavePath = "./data/dump-sql/dump.sql"

mysqldump -u "$env:MYCLINIC_DB_ADMIN_USER" -p"$env:MYCLINIC_DB_ADMIN_PASS" `
    --result-file "$SavePath" `
    --single-transaction --master-data myclinic 