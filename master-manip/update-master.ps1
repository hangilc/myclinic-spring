param(
    [parameter(mandatory)]$IyakuhinZipfile,
    [parameter(mandatory)]$ShinryouZipfile,
    [parameter(mandatory)]$KizaiZipfile,
    [parameter(mandatory)]$ValidFrom
)

echo "IyakuhinZipfile: $IyakuhinZipfile"
echo "ShinryouZipfile: $ShinryouZipfile"
echo "KizaiZipfile: $KizaiZipfile"
echo "ValidFrom: $ValidFrom"

# ensure working directory
mkdir work -Force | out-null

# read db user and password
$dbUser = read-host -prompt "mysql user: "
$dbPass = ./read-pass "mysql pass: "

echo $dbPass.Length

# backup master tables
mysqldump -u "$dbUser" -p"$dbPass" --default-character-set=cp932 myclinic `
    iyakuhin_master_arch shinryoukoui_master_arch `
    tokuteikizai_master_arch > work/masters-save.sql


