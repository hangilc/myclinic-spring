::
:: Usage: release-delete
:: interactively deletes releases
::
@echo off
java -cp management\target\management-1.0.0-SNAPSHOT-jar-with-dependencies.jar ^
    jp.chang.myclinic.management.ReleaseDelete %*
