::
:: Usage: create-shortcuts-for-pharma
:: create shortcuts in the current directory
::

@echo off
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�z�b�g���C��".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar hotline.jar pharmacy practice" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�@���~�[�e�B���O".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar intraclinic.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "���".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar pharma.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "�f�Ø^".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar record-browser.jar" "."
