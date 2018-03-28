param(
    [parameter(mandatory)]$IyakuhinZipfile,
    [parameter(mandatory)]$ShinryouZipfile,
    [parameter(mandatory)]$KizaiZipfile,
    [parameter(mandatory)]$ValidFrom,
    [switch]$SkipDump = $false  # skip backing up master tables
)

$prevDate = (get-date $ValidFrom).AddDays(-1).toString('yyyy-MM-dd')

echo "IyakuhinZipfile: $IyakuhinZipfile"
echo "ShinryouZipfile: $ShinryouZipfile"
echo "KizaiZipfile: $KizaiZipfile"
echo "ValidFrom: $ValidFrom"
echo "previous day of ValidFrom: $prevDate"

. ./master-lib.ps1

# ensure working directory
mkdir work -Force | out-null

# backup master tables
# to restore backup
# get-content work/masters-save.sql | mysql -u hangil -p myclinic
if( -Not $SkipDump ){
    backupMaster
}

# set valid_upto of current masters
restrictCurrentMaster($prevDate)

# prepare updater sql
prepIyakuhinMaster $IyakuhinZipfile $ValidFrom $iyakuhinUpdaterFile
prepShinryouMaster $ShinryouZipfile $ValidFrom $shinryouUpdaterFile
prepKizaiMaster $KizaiZipfile $ValidFrom $kizaiUpdaterFile

execMysqlQuery($iyakuhinUpdaterFile)
execMysqlQuery($shinryouUpdaterFile)
execMysqlQuery($kizaiUpdaterFile)

