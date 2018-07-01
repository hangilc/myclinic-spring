@set PATH=.\scripts;%PATH%
@doskey server=java -jar server\target\server-1.0.0-SNAPSHOT.jar $*
@doskey practice=java -jar practice\target\practice-1.0.0-SNAPSHOT.jar $*
@cmd /K my-pyenv\scripts\activate.bat

