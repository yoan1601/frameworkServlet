# frameworkServlet

NB: importez framework.jar dans votre librairie

# Votre projet
### ajouter dans votre web.xml dans la balise "web-app" ceci pour definir le mapping vers le frontServlet :
    <servlet>
        <servlet-name>FrontServlet</servlet-name>
        <servlet-class>etu1793.framework.servlet.FrontServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>FrontServlet</servlet-name>
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>

### pour utiliser le framework, votre méthode doit ressembler à ceci :
    @UrlAnnotation(urlPattern = "emp_find_by_id.do")
    public ModelView findById(@ParamAnnotation(description = "id") Integer id,@ParamAnnotation(description = "salut") String salut) {
        ModelView mv = new ModelView();
        mv.setView("emp_fiche.jsp");
        ArrayList <Employe> l = new ArrayList<>();
        Employe e = new Employe("jean", 18);
        l.add(e);
        e = new Employe("jacque", 32);
        l.add(e);
        e = new Employe("patrik", 25);
        l.add(e);
        mv.addItem("employe", l.get(id));
        mv.addItem("id", id);
        mv.addItem("salut", salut);
        return mv;
    }


>notez que :
<p>1. dans @UrlAnnotation l'urlPattern doit <strong>toujours se terminer par l'extension ".do"</strong>
</p>
<p>2. l'objet retourné doit être <strong>toujours de type ModelView</strong>
</p>
<p>3. l'attribut "view" de votre ModelView doit toujours avoir une valeur, pour ce faire
 utilisez la méthode ModelView.setView(String). Ceci sert de redirection après que le traitement dans votre méthode est terminé</p>
</p>
</p>
&nbsp;
 # donnée model vers vue :
    1. Ajoutez vos données en les référençant par des clés avec la méthode ModelView.addItem(String clé, Object Valeur)
    2.Dans votre vue, vous obtiendrez votre donnée en utilisant la méthode request.getAttribute(String clé), n'oubliez pas de caster l'objet retourné par le type adéquat

&nbsp;
# donnée vue vers model :
## 1. Utilisant les attributs de votre model
>NB: <strong>les "name" de vos données</strong> (exemple : formulaire) doivent <strong>TOUJOURS CORRESPONDRE AUX NOMS DES ATTRIBUTS de votre model</strong>
### --------------------------------- Upload de fichier-------------------------------------
Si vous voulez uploader un fichier depuis un formulaire
- L'attribut correspondant à votre fichier doit etre de type **FileUpload**  que vous importerez vie le paquet **etu1793.framework.utilitaire.FileUpload**
- dans fileUpload il y a l'attribut :
> String **name** : stock le nom du fichier (ex : badge.png)

> byte [ ] **file** : stock les bytes du fichier

## 2. Utilisant les arguments de votre model
>NB: <strong>les "name" de vos données</strong> (exemple : formulaire) doivent <strong>TOUJOURS CORRESPONDRE AUX NOMS DES ARGUMENTS de votre model</strong>

>Annotez les arguments de votre model par **@ParamAnnotation(description = String valeur)** tel que *valeur* sera le name que partagera votre vue et votre model
## 3. Les types de données supportés
<ul>
<li>String</li>
<li>Integer</li>
<li>Double</li>
<li>boolean</li>
<li>java.util.Date</li>
<li>int</li>
<li>double</li>
</ul>

>Veuillez noter qu'il est conseillé d'utiliser des Integer au lieu de int et Double au lieu de double si vous voulez conserver les valeurs destinées à être null (pas de valeur par défaut)

&nbsp;
 # Version tomcat compatible : 10
 >ceci en raison des importations de librairie, dans les servlets vous devez importer jakarta au lieu d'utiliser d'autre librairie
