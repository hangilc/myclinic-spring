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

# ensure working directory
mkdir work -Force | out-null

# read db user and password
$dbUser = read-host -prompt "mysql user: "
$dbPass = ./read-pass "mysql pass: "

# backup master tables
# to restore backup
# get-content work/masters-save.sql | mysql -u hangil -p myclinic
if( -Not $SkipDump ){
    mysqldump --default-character-set=cp932 -u "$dbUser" -p"$dbPass" myclinic `
        iyakuhin_master_arch shinryoukoui_master_arch `
        tokuteikizai_master_arch | set-content work/masters-save.sql
}

# set valid_upto of current masters
echo "update iyakuhin_master_arch set valid_upto = '$prevDate' where valid_upto = '0000-00-00';" |
        mysql -u "$dbUser" -p"$dbPass" myclinic
echo "update shinryoukoui_master_arch set valid_upto = '$prevDate' where valid_upto = '0000-00-00';" |
        mysql -u "$dbUser" -p"$dbPass" myclinic
echo "update tokuteikizai_master_arch set valid_upto = '$prevDate' where valid_upto = '0000-00-00';" |
        mysql -u "$dbUser" -p"$dbPass" myclinic


