mkdir testFramework2
mkdir testFramework2/WEB-INF
mkdir testFramework2/WEB-INF/classes
mkdir testFramework2/WEB-INF/lib
cp -r ../../web/WEB-INF/web.xml testFramework2/WEB-INF/web.xml
cp -r ../../web/* testFramework2/
javac -cp ../../../framework.jar -parameters -d testFramework2/WEB-INF/classes objets/*.java
cp -r deployWar.sh testFramework2/deployWar.sh
cp -r ../../../framework.jar testFramework2/WEB-INF/lib/framework.jar
cd testFramework2
bash deployWar.sh
cd ..
rm -r testFramework2
