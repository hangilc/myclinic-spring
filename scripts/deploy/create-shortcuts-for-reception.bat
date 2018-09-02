::
:: Usage: create-shortcuts-for-reception
:: create shortcuts in the current directory
::

@echo off
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "ホットライン".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar hotline.jar reception practice" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "院内ミーティング".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar intraclinic.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "受付".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar reception.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "診療録".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar record-browser.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "スキャナー".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar scanner.jar" "."
