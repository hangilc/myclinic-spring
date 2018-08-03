rem @echo off

set RELEASE_FOLDER=%~1
set NAME_EXTRA=%~2

if "%RELEASE_FOLDER%" == "" (
	set RELEASE_FOLDER=%cd%
)

echo "%RELEASE_FOLDER%"
echo "%NAME_EXTRA%"

java -cp .\winutil.jar jp.chang.myclinic.winutil.main.CreateShortcut ^
  "%JAVA_HOME%"\bin\javaw ^
  "-jar ""%RELEASE_FOLDER%""\practice.jar http://localhost:18080/json" ^
  "%RELEASE_FOLDER%" ^
  "%cd%"\êfé@"%NAME_EXTRA%".lnk

