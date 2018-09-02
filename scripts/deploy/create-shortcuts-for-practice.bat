::
:: 
::
@echo off

java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "ホットライン（受付）".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar hotline.jar practice reception" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "ホットライン（薬局）".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar hotline.jar practice pharmacy" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "院内ミーティング".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar intraclinic.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "サーバー".lnk "%JAVA_HOME%\bin\java.exe" "-jar server.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "薬局".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar pharma.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "診察".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar practice.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "レセプト印刷".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar rcpt-drawer.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "受付".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar reception.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "診療録".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar record-browser.jar" "."
java -cp management.jar jp.chang.myclinic.management.CreateShortcut ^
  "スキャナー".lnk "%JAVA_HOME%\bin\javaw.exe" "-jar scanner.jar" "."
