::
:: Usage: create-shortcuts-for-pharma
:: create shortcuts in the current directory
::

@echo off
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "ホットライン".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar hotline.jar pharmacy practice" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "院内ミーティング".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar intraclinic.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "薬局".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar pharma.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "診療録".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar record-browser.jar" "."
