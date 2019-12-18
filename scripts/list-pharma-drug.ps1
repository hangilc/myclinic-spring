$User = $env:MYCLINIC_DB_USER
$Pass = $env:MYCLINIC_DB_PASS

$result = mysql -u $User -p"$Pass" -e "select distinct (p.iyakuhincode) as iyakuhincode , `
    (select m.name from iyakuhin_master_arch m where m.iyakuhincode = p.iyakuhincode `
     order by m.valid_from desc limit 1) as name from pharma_drug p `
     order by name" --xml myclinic
$xml = [XML]$result
$xml.resultset.row | ForEach-Object {
    $iyakuhincode = $_.SelectNodes("field[@name='iyakuhincode']").InnerText
    $name = $_.SelectNodes("field[@name='name']").InnerText
    echo ("{0},{1}" -f $iyakuhincode, $name)
}
