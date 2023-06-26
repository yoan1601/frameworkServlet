echo 'construction du .jar'
#compilation du framework
# Définir le répertoire racine à partir duquel on va compiler les fichiers Java
repertoire_racine="."

# Trouver tous les fichiers .java dans le répertoire racine et ses sous-répertoires
# fichiers_java=$(find "$repertoire_racine" -type f -name "*.java")

# # Compiler chaque fichier .java
# for fichier_java in $fichiers_java; do
#     javac -cp lib/servlet-api.jar -parameters -d classes "$fichier_java"
#     echo 'compilation de '$fichier_java
# done
javac -cp lib/servlet-api.jar:lib/gson-2.8.2.jar -parameters -d classes src/*.java
#construit le .jar
cd classes
bash constrJar.sh
#envoie du jar au debut du repertoire frameworkSerlvet 
cd ..
bash ready.sh
