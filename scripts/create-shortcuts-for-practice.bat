rem @echo off
set RELEASE_FOLDER=%1
set NAME_EXTRA=%2

echo %RELEASE_FOLDER%
echo %NAME_EXTRA%

java -cp C:\Users\hangil\myclinic-spring\winutil\target\winutil-1.0.0-SNAPSHOT-jar-with-dependencies.jar ^
  jp.chang.myclinic.winutil.main.CreateShortcut ^
  "%JAVA_HOME%"\bin\javaw ^
  "-jar ""%RELEASE_FOLDER%""\practice.jar http://localhost:18080/json" ^
  "%RELEASE_FOLDER%" ^
  "%cd%"\êfé@.lnk

