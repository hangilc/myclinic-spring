@echo off
setlocal enabledelayedexpansion
set groupId=%~1
set artifactId=%~2
set version=%~3

if "%groupId%" == "" (
    goto :usage
)

if "%artifactId%" == "" (
    goto :usage
)

if "%version%" == "" (
    set version=1.0.0-SNAPSHOT
)

set groupArtifact=%groupId%.%artifactId%
set package=%groupArtifact:-=%

@echo on
mvn archetype:generate -DarchetypeGroupId="jp.chang.myclinic" -DarchetypeArtifactId="archetype-cli" ^
    -DgroupId="%groupId%" -DartifactId="%artifactId%" -Dversion="%version%" ^
    -Dpackage="%package%" -DinteractiveMode=false -DarchetypeCatalog=local
@echo off
goto :endpoint

:usage
echo Usage create-cli-project GROUP-ID ARTIFACT-ID [VERSION]

:endpoint
exit /b
endlocal

