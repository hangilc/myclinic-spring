@echo off
set RELEASE_FOLDER=C:\Users\hangil\current-test
if exist %RELEASE_FOLDER% (
	set time0=%time::=%
	set date0=%date:/=%
	set stamp=%date0:~,8%-%time0:~,4%
	for %%a in ("%RELEASE_FOLDER%") do (
		set rename_path=%%~na-%stamp%
	)
	rename "%RELEASE_FOLDER%" "%rename_path%"
	if not "%errorlevel%" == "0" (goto end)
) 
echo creating release folder^: %RELEASE_FOLDER%
mkdir "%RELEASE_FOLDER%"
if not "%errorlevel%" == "0" (goto end)

xcopy /Y /I config "%RELEASE_FOLDER%/config"
copy /Y hotline\target\hotline-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\hotline.jar"
copy /Y intraclinic\target\intraclinic-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\intraclinic.jar"
copy /Y pharma\target\pharma-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\pharma.jar"
copy /Y practice\target\practice-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\practice.jar"
copy /Y reception\target\reception-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\reception.jar"
copy /Y record-browser\target\record-browser-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\record-browser.jar"
copy /Y scanner\target\scanner-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\scanner.jar"
copy /Y server\target\server-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\server.jar"
copy /Y rcpt-drawer\target\rcpt-drawer-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%\rcpt-drawer.jar"
copy /Y winutil\target\winutil-1.0.0-SNAPSHOT-jar-with-dependencies.jar "%RELEASE_FOLDER%\winutil.jar"
copy /Y scripts\create-shortcuts-for-practice.bat "%RELEASE_FOLDER%\create-shortcuts-for-practice.bat"
mkdir "%RELEASE_FOLDER%\work"

echo @cmd /K >"%RELEASE_FOLDER%"/console.bat

:END
