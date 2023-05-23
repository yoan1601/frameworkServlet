webapp=testFramework2

mkdir $webapp
mkdir $webapp/WEB-INF
mkdir $webapp/WEB-INF/classes
mkdir $webapp/WEB-INF/lib
cp -r ../../web/WEB-INF/web.xml $webapp/WEB-INF/web.xml
cp -r ../../web/* $webapp/

# Définir le répertoire racine à partir duquel on va compiler les fichiers Java
repertoire_racine="."

# Trouver tous les fichiers .java dans le répertoire racine et ses sous-répertoires
fichiers_java=$(find "$repertoire_racine" -type f -name "*.java")

# Compiler chaque fichier .java
for fichier_java in $fichiers_java; do
    javac -cp ../../../framework.jar -parameters -d $webapp/WEB-INF/classes "$fichier_java"
    echo 'compilation de '$fichier_java
done

#javac -cp ../../../framework.jar -parameters -d $webapp/WEB-INF/classes objets/*.java
cp -r deployWar.sh $webapp/deployWar.sh
cp -r ../../../framework.jar $webapp/WEB-INF/lib/framework.jar
cd $webapp
bash deployWar.sh
cd ..
rm -r $webapp
