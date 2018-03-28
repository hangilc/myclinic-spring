$dbHost = $env:MYCLINIC_DB_HOST
$dbUser = $env:MYCLINIC_DB_USER
$dbPass = $env:MYCLINIC_DB_PASS

$masterBackupFile = "work/master-backup.sql"

function backupMaster($backupFile){
    if( !($backupFile) ){
        $backupFile = $masterBackupFile
    }
    $outputencoding = [Console]::OutputEncoding
    mysqldump --default-character-set=cp932 `
        -h "$dbHost" -u "$dbUser" -p"$dbPass" `
        myclinic `
        iyakuhin_master_arch shinryoukoui_master_arch `
        tokuteikizai_master_arch `
    | set-content -encoding $outputencoding "$backupFile"
}

function restoreMaster($backupFile){
    if( !($backupFile) ){
        $backupFile = $masterBackupFile
    }
    $outputencoding = [Console]::OutputEncoding
    get-content -encoding $outputencoding "$backupFile" `
    | mysql --default-character-set=cp932 `
        -h "$dbHost" -u "$dbUser" -p"$dbPass" `
        myclinic
}

function restrictCurrentIyakuhinMaster($validUpto){
    echo "update iyakuhin_master_arch set valid_upto = '$validUpto' where valid_upto = '0000-00-00';" `
    | mysql -h "$dbHost" -u "$dbUser" -p"$dbPass" myclinic
}

function restrictCurrentShinryouMaster($validUpto){
    echo "update shinryoukoui_master_arch set valid_upto = '$validUpto' where valid_upto = '0000-00-00';" `
    | mysql -h "$dbHost" -u "$dbUser" -p"$dbPass" myclinic
}

function restrictCurrentKizaiMaster($validUpto){
    echo "update tokuteikizai_master_arch set valid_upto = '$validUpto' where valid_upto = '0000-00-00';" `
    | mysql -h "$dbHost" -u "$dbUser" -p"$dbPass" myclinic
}

function restrictCurrentMaster($validUpto){
    restrictCurrentIyakuhinMaster $validUpto
    restrictCurrentShinryouMaster $validUpto
    restrictCurrentKizaiMaster $validUpto
}

