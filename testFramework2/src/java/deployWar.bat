rmdir /s /q ..\testFramework2.war
jar -cvf ..\testFramework2.war * 
rmdir /s /q C:\apache-tomcat-10\webapps\testFramework2.war
xcopy ..\testFramework2.war C:\apache-tomcat-10\webapps\
