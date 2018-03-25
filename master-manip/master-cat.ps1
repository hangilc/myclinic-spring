$jarfile = "$PSScriptRoot/target/master-manip-1.0.0-SNAPSHOT.jar"
$mainClass = "jp.chang.myclinic.mastermanip.Cat"
java -cp $jarfile $mainClass $args
