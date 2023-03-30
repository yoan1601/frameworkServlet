mkdir testFramework
mkdir testFramework/WEB-INF
mkdir testFramework/WEB-INF/classes
mkdir testFramework/WEB-INF/lib
cp ../../web/WEB-INF/web.xml testFramework/WEB-INF/web.xml
cp ../../web/* testFramework/
javac -cp ../../../framework.jar -d testFramework/WEB-INF/classes objets/*.java
cp -r deployWar.sh testFramework/deployWar.sh
cp -r ../../../framework.jar testFramework/WEB-INF/lib/framework.jar
cd testFramework
bash deployWar.sh
cd ..
rm -r testFramework
