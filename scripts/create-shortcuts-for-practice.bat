:: 
:: Usage: create-shortcuts-for-practice [RELEASE_FOLDER] [NAME_EXTRA]
:: 
@echo off

set RELEASE_FOLDER=%~1
set NAME_EXTRA=%~2

if "%RELEASE_FOLDER%" == "" (
	set RELEASE_FOLDER=%cd%
)

echo "%RELEASE_FOLDER%"
echo "%NAME_EXTRA%"

java -cp "%RELEASE_FOLDER%"\winutil.jar jp.chang.myclinic.winutil.main.CreateShortcut ^
  "%JAVA_HOME%"\bin\java ^
  "-jar ""%RELEASE_FOLDER%""\server.jar" ^
  "%RELEASE_FOLDER%" ^
  "%cd%"\�T�[�o�["%NAME_EXTRA%".lnk

java -cp "%RELEASE_FOLDER%"\winutil.jar jp.chang.myclinic.winutil.main.CreateShortcut ^
  "%JAVA_HOME%"\bin\javaw ^
  "-jar ""%RELEASE_FOLDER%""\hotline.jar http://localhost:18080/json practice reception" ^
  "%RELEASE_FOLDER%" ^
  "%cd%"\�z�b�g���C���i��t�j"%NAME_EXTRA%".lnk

java -cp "%RELEASE_FOLDER%"\winutil.jar jp.chang.myclinic.winutil.main.CreateShortcut ^
  "%JAVA_HOME%"\bin\javaw ^
  "-jar ""%RELEASE_FOLDER%""\hotline.jar http://localhost:18080/json practice pharmacy" ^
  "%RELEASE_FOLDER%" ^
  "%cd%"\�z�b�g���C���i��ǁj"%NAME_EXTRA%".lnk

java -cp "%RELEASE_FOLDER%"\winutil.jar jp.chang.myclinic.winutil.main.CreateShortcut ^
  "%JAVA_HOME%"\bin\javaw ^
  "-jar ""%RELEASE_FOLDER%""\reception.jar http://localhost:18080/json" ^
  "%RELEASE_FOLDER%" ^
  "%cd%"\��t"%NAME_EXTRA%".lnk

java -cp "%RELEASE_FOLDER%"\winutil.jar jp.chang.myclinic.winutil.main.CreateShortcut ^
  "%JAVA_HOME%"\bin\javaw ^
  "-jar ""%RELEASE_FOLDER%""\practice.jar http://localhost:18080/json" ^
  "%RELEASE_FOLDER%" ^
  "%cd%"\�f�@"%NAME_EXTRA%".lnk

java -cp "%RELEASE_FOLDER%"\winutil.jar jp.chang.myclinic.winutil.main.CreateShortcut ^
  "%JAVA_HOME%"\bin\javaw ^
  "-jar ""%RELEASE_FOLDER%""\intraclinic.jar http://localhost:18080/json" ^
  "%RELEASE_FOLDER%" ^
  "%cd%"\�@���~�[�e�B���O"%NAME_EXTRA%".lnk

java -cp "%RELEASE_FOLDER%"\winutil.jar jp.chang.myclinic.winutil.main.CreateShortcut ^
  "%JAVA_HOME%"\bin\javaw ^
  "-jar ""%RELEASE_FOLDER%""\pharma.jar http://localhost:18080/json" ^
  "%RELEASE_FOLDER%" ^
  "%cd%"\���"%NAME_EXTRA%".lnk

java -cp "%RELEASE_FOLDER%"\winutil.jar jp.chang.myclinic.winutil.main.CreateShortcut ^
  "%JAVA_HOME%"\bin\javaw ^
  "-jar ""%RELEASE_FOLDER%""\record-browser.jar http://localhost:18080/json" ^
  "%RELEASE_FOLDER%" ^
  "%cd%"\�f�Ø^"%NAME_EXTRA%".lnk

