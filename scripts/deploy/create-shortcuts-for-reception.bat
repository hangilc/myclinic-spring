::
:: Usage: create-shortcuts-for-reception
:: create shortcuts in the current directory
::

@echo off
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�z�b�g���C��".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar hotline.jar reception practice" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�@���~�[�e�B���O".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar intraclinic.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "��t".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar reception.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�f�Ø^".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar record-browser.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�X�L���i�[".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar scanner.jar" "."
