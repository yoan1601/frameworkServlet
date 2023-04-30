REM compilation du framework
javac -cp lib\servlet-api.jar -parameters -d classes src\*.java
REM construit le .jar
cd classes\
constrJar.bat
REM envoie du jar au debut du repertoire frameworkSerlvet 
cd ..\
ready.bat
