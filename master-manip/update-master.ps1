param(
    [parameter(mandatory)]$IyakuhinZipfile,
    [parameter(mandatory)]$ShinryouZipfile,
    [parameter(mandatory)]$KizaiZipfile,
    [parameter(mandatory)]$ValidFrom
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
mysqldump --default-character-set=cp932 -u "$dbUser" -p"$dbPass" myclinic `
    iyakuhin_master_arch shinryoukoui_master_arch `
    tokuteikizai_master_arch | set-content work/masters-save.sql



