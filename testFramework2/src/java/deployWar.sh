rm -r ../testFramework2.war
jar -cvf ../testFramework2.war * 
rm -r /home/yoan/apache-tomcat-10.0.27/webapps/testFramework2.war
cp -r ../testFramework2.war /home/yoan/apache-tomcat-10.0.27/webapps/testFramework2.war
