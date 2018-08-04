:: 
:: Usage: deploy-reception [TARGET-FOLDER]
:: Default target folder is %RECEPTION_RELEASE_FOLDER%
:: 
rem @echo off

set RELEASE_FOLDER=%~1

if "%RELEASE_FOLDER%" == "" (
	set RELEASE_FOLDER=%RECEPTION_RELEASE_FOLDER%
)


if "%RELEASE_FOLDER%" == "" (
	set RELEASE_FOLDER=%HOMEDRIVE%%HOMEPATH%\myclinic-reception-releases\current
)

echo %RELEASE_FOLDER%

if exist %RELEASE_FOLDER% (
	set time0=%time::=%
	set date0=%date:/=%
	set stamp=%date0:~,8%-%time0:~,4%
	for %%a in ("%RELEASE_FOLDER%") do (
		set rename_path=%%~na-%stamp%
	)
	rename "%RELEASE_FOLDER%" "%rename_path%" || exit /B 1
) 
echo creating release folder^: %RELEASE_FOLDER%
mkdir "%RELEASE_FOLDER%" || exit /B 2

copy /Y hotline\target\hotline-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\hotline.jar"
copy /Y intraclinic\target\intraclinic-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\intraclinic.jar"
copy /Y pharma\target\pharma-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\pharma.jar"
copy /Y reception\target\reception-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\reception.jar"
copy /Y record-browser\target\record-browser-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\record-browser.jar"
copy /Y scanner\target\scanner-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\scanner.jar"
copy /Y winutil\target\winutil-1.0.0-SNAPSHOT-jar-with-dependencies.jar "%RELEASE_FOLDER%\winutil.jar"
copy /Y scripts\create-shortcuts-for-reception.bat "%RELEASE_FOLDER%\create-shortcuts-for-practice.bat"
mkdir "%RELEASE_FOLDER%\work"

echo @cmd /K >"%RELEASE_FOLDER%"/console.bat

