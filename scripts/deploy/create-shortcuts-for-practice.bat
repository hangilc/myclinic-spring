::
:: 
::
@echo off

java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�z�b�g���C���i��t�j".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar hotline.jar practice reception" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�z�b�g���C���i��ǁj".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar hotline.jar practice pharmacy" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�@���~�[�e�B���O".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar intraclinic.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�T�[�o�[".lnk "%JAVA_HOME%\bin\java.exe" "-jar server.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "���".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar pharma.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�f�@".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar practice.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "���Z�v�g���".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar rcpt-drawer.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "��t".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar reception.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�f�Ø^".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar record-browser.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�X�L���i�[".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar scanner.jar" "."
