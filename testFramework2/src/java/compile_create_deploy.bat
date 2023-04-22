rmdir /s /q testFramework2
mkdir testFramework2
mkdir testFramework2\WEB-INF
mkdir testFramework2\WEB-INF\classes
mkdir testFramework2\WEB-INF\lib
xcopy ..\..\web\WEB-INF\web.xml testFramework2\WEB-INF\
xcopy ..\..\web\* testFramework2\
javac -cp ..\..\..\frameworkWin.jar -d testFramework2\WEB-INF\classes objets\*.java
xcopy deployWar.bat testFramework2\
xcopy ..\..\..\frameworkWin.jar testFramework2\WEB-INF\lib\
cd testFramework2
deployWar.bat
cd ..\
rmdir /s /q testFramework2
