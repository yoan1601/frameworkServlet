rm -r ../testFramework.war
jar -cvf ../testFramework.war * 
rm -r /home/yoan/apache-tomcat-10.0.27/webapps/testFramework.war
cp -r ../testFramework.war /home/yoan/apache-tomcat-10.0.27/webapps/testFramework.war
