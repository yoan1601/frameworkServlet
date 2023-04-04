#compilation du framework
javac -cp lib/servlet-api.jar  -d classes src/*.java
#construit le .jar
cd classes
bash constrJar.sh
#envoie du jar au debut du repertoire frameworkSerlvet 
cd ..
bash ready.sh
