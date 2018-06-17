$dstDir = "C:\Users\hangil\myclinic-current"

if( test-path "$dstDir" ){
    $dateTag = get-date -format "yyyyMMddHHmm"
    ren $dstDir "$dstDir-$dateTag"
}
mkdir $dstDir

function copyModule($module){
    $src = "./$module/target/${module}*.jar"
    $dst = "$dstDir/$module/target"
    mkdir "$dst"
    copy $src $dst
}

$modules = "server", "hotline", "reception", "practice", "intraclinic", "pharma", "record-browser", "scanner"
$modules | %{ copyModule $_ }
copy-item "./config" -destination "$dstDir/config" -recurse -exclude "*.git"
