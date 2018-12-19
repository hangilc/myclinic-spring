$yesno = Read-Host "PostgreSQL サービスを停止しましたか？ (Y/N)"
if( $yesno -ne "Y" ){
    exit 1
}
$folder = "C:\pgdata\main"
if( Test-Path -Path $folder ){
    $stamp = Get-Date -UFormat "%Y-%m-%d-%H%M%S"
    $dest = "$folder-$stamp"
    Move-Item -Path $folder -Destination $dest
}
New-Item -ItemType directory -Path $folder
New-Item -ItemType directory -Path "$folder\cluster"
New-Item -ItemType directory -Path "$folder\walarchive"
New-Item -ItemType directory -Path "$folder\backup"
$cluster = "$folder\cluster"
initdb -D $cluster -E UTF8 --no-locale -U postgres
Copy-Item -Path config\postgresql\postgresql.conf -Destination "$cluster"
Copy-Item -Path config\postgresql\pg_hba.conf -Destination "$cluster"
$yesno = Read-Host "PostgreSQL サービスを開始してください。 (Y/N)"
if( $yesno -ne "Y" ){
    exit 1
}
psql -U postgres -f "config\postgresql\initial-setup.sql"

