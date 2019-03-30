Write-Output $args
invoke-expression (-join("java -cp backend-sqlite\target\backend-sqlite-1.0.0-SNAPSHOT-jar-with-dependencies.jar",
    " jp.chang.myclinic.backendsqlite.Test $args"))
