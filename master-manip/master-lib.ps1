$dbHost = $env:MYCLINIC_DB_HOST
$dbUser = $env:MYCLINIC_DB_USER
$dbPass = $env:MYCLINIC_DB_PASS

$jarFile = "$PSScriptRoot/target/master-manip-1.0.0-SNAPSHOT.jar"
$masterBackupFile = "work/master-backup.sql"
$iyakuhinUpdaterFile = "work/iyakuhin-updater.sql"
$shinryouUpdaterFile = "work/shinryou-updater.sql"
$kizaiUpdaterFile = "work/kizai-updater.sql"

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

function prepIyakuhinMaster($masterZipFile, $validFrom, $outfile){
    if( !($outfile) ){
        $outfile = "work/iyakuhin-updater.sql"
    }
    $outputencoding = [Console]::OutputEncoding
    java -cp $jarFile jp.chang.myclinic.mastermanip.Cat iyakuhin "$masterZipFile" `
    | java -cp $jarFile jp.chang.myclinic.mastermanip.Convert iyakuhin `
    | java -cp $jarFile jp.chang.myclinic.mastermanip.Updater iyakuhin "$validFrom" `
    | set-content -encoding $outputencoding $outfile
}

function prepShinryouMaster($masterZipFile, $validFrom, $outfile){
    if( !($outfile) ){
        $outfile = "work/shinryou-updater.sql"
    }
    $outputencoding = [Console]::OutputEncoding
    java -cp $jarFile jp.chang.myclinic.mastermanip.Cat shinryou "$masterZipFile" `
    | java -cp $jarFile jp.chang.myclinic.mastermanip.Convert shinryou `
    | java -cp $jarFile jp.chang.myclinic.mastermanip.Updater shinryou "$validFrom" `
    | set-content -encoding $outputencoding $outfile
}

function prepKizaiMaster($masterZipFile, $validFrom, $outfile){
    if( !($outfile) ){
        $outfile = "work/kizai-updater.sql"
    }
    $outputencoding = [Console]::OutputEncoding
    java -cp $jarFile jp.chang.myclinic.mastermanip.Cat kizai "$masterZipFile" `
    | java -cp $jarFile jp.chang.myclinic.mastermanip.Convert kizai `
    | java -cp $jarFile jp.chang.myclinic.mastermanip.Updater kizai "$validFrom" `
    | set-content -encoding $outputencoding $outfile
}

function execMysqlQuery($sourceFile){
    $outputencoding = [Console]::OutputEncoding
    get-content -encoding $outputencoding "$sourceFile" `
    | mysql --default-character-set=cp932 -h "$dbHost" -u "$dbUser" -p"$dbPass" myclinic
}


