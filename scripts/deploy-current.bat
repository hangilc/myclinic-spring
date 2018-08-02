@echo off
set RELEASE_FOLDER=C:/Users/hangil/current-test
if exist %RELEASE_FOLDER% (
	set time0=%time::=%
	set date0=%date:/=%
	set stamp=%date0:~,8%-%time0:~,4%
	for %%a in ("%RELEASE_FOLDER%") do (
		set rename_path=%%~na-%stamp%
	)
	rename "%RELEASE_FOLDER%" "%rename_path%"
) 
echo creating release folder^: %RELEASE_FOLDER%
mkdir "%RELEASE_FOLDER%"

xcopy /I config "%RELEASE_FOLDER%/config"
copy hotline\target\hotline-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%/hotline.jar"
copy intraclinic\target\intraclinic-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%/intraclinic.jar"
copy pharma\target\pharma-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%/pharma.jar"
copy practice\target\practice-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%/practice.jar"
copy reception\target\reception-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%/reception.jar"
copy record-browser\target\record-browser-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%/record-browser.jar"
copy scanner\target\scanner-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%/scanner.jar"
copy server\target\server-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%/server.jar"
copy rcpt-drawer\target\rcpt-drawer-1.0.0-SNAPSHOT.jar "%RELEASE_FOLDER%/rcpt-drawer.jar"
mkdir "%RELEASE_FOLDER%/work"

