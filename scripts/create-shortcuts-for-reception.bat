::
:: Usage: create-shortcuts-for-reception RELEASE_FOLDER TARGET_FOLDER [NAME_EXTRA]
::
@echo off

java -cp management\target\management-1.0.0-SNAPSHOT-jar-with-dependencies.jar ^
    jp.chang.myclinic.management.CreateShortcuts ^
    reception %*
