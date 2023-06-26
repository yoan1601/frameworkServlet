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
## SINGLETON

Votre classe peut etre de type singleton (instance unique pour toute la session), pour cela il faut annoter votre classe par **@Scope(type = "singleton")** ,annotation importee depuis la librairie **etu1793.framework.annotationDao.Scope**

*exemple*: vous avez une classe nommee 'Client' et vous voulez qu'elle soit un singleton

    import etu1793.framework.annotationDao.Scope;

    @Scope(type = "singleton")
    public class Client {
        ...
    }

&nbsp;
## AUTHENTIFICATION
Si vous voulez ajouter des privilèges sur vos fonctions, procedez comme suit :

- Configurez votre **web.xml**:
 .ajoutez 1 init-param **isConnected** avec init-value de **votre choix**,
 .ajoutez 1 init-param **role** avec init-value de **votre choix**

- exemple :

        <web-app>
        <servlet>
            ...
            <init-param>
                <param-name>isConnected</param-name>
                <param-value>estConnecte</param-value>
            </init-param>
            <init-param>
                <param-name>role</param-name>
                <param-value>role</param-value>
            </init-param>
        </servlet>
        ...
        </web-app>

- Creez une **fonction d'authentification** qui prend en argument le login ,le mot de passe etc, qui serviront à connecter l'utilisateur et à lui affecter un role et de plus à configurer la session, dans le cas où l'authentification a échoué, vous pouvez faire une redirection dans la partie else { ... }
- exemple

        import etu1793.framework.annotationDao.auth;
        import etu1793.framework.annotationDao.UrlAnnotation;
        import etu1793.framework.annotationDao.ParamAnnotation;
        import etu1793.framework.modelView.ModelView;

        @UrlAnnotation(urlPattern = "auth.do")
        public ModelView authentify(@ParamAnnotation(description = "login") String login,
            @ParamAnnotation(description = "pwd") String pwd) {
            ModelView mv = new ModelView();

            if (login.equals("yoan") && pwd.equals("1234")) {
                mv.getSession().put("isConnected", true);
                mv.getSession().put("role", "autre");
                mv.setView("accueil.jsp");
            }

            else if (login.equals("admin") && pwd.equals("1234")) {
                mv.getSession().put("isConnected", true);
                mv.getSession().put("role", "admin");
                mv.setView("accueil.jsp");
            }

            else {
                mv.setView("index.jsp");
            }

            return mv;
        }

> ATTENTION : le role administrateur doit etre exactement **"admin"**

- Annotez vos methodes par **@auth(role = ...)** pour filtrer les privileges pouvant l'acceder
- exemple : la methode "save" ci-dessous **n'est accessible que par l'administrateur donc role = "admin"**

        import etu1793.framework.annotationDao.auth;
        import etu1793.framework.annotationDao.UrlAnnotation;
        import etu1793.framework.modelView.ModelView;

        @UrlAnnotation(urlPattern = "emp_save.do")
        @auth(role = "admin")
        public ModelView save() {
            ModelView mv = new ModelView();
            mv.setView("emp_result_save.jsp");
            mv.addItem("employe", this);
            return mv;
        }
> Notez que si vous voulez que la fonction soit **accessible par tous** : vous pouvez assigner d'**autre valeur à role de @auth(role)**, soit ne pas annoter la methode par @auth

. Creez votre formulaire d'authentification : il faut que les **names** des **inputs** soient **conformes** aux descriptions de **@ParamAnnotation** de votre fonction d'authentification (dans notre cas : login et pwd)

. exemple : dans notre cas login et pwd

        <form action="auth.do" method="post">
            <input type="text" name="login" id="login" required>
            <input type="password" name="pwd" id="pwd" required>
            <input type="submit" value="se connecter">
        </form>

> N'oubliez pas de configurer **action** de votre form, il doit être identique à la valeur de **@UrlAnnotation(urlPattern = "auth.do")** de la fonction d'authentification

&nbsp;
## SESSION
Vous pouvez recuperer les données de la session actuelle en suivant ces étapes :
- Ajoutez un attribut **HashMap<String, Object> session** dans votre classe, cet attribut se comportera comme l'HttpSession de la requete : le String sera la clé et l'Object la valeur
- Annotez votre méthode par **@Session** pour manipuler la session dans la méthode
- Exemple :

        import java.util.HashMap;
        import etu1793.framework.annotationDao.Session;
        import etu1793.framework.annotationDao.UrlAnnotation;

        public class Client {

            ---

            HashMap<String, Object> session;

            @Session
            @UrlAnnotation(urlPattern = "client_save.do")
            public ModelView save() {
                ModelView mv = new ModelView();
                mv.setView("client_result_save.jsp");

                System.out.println("les valeurs dans la session actuelle");
                for (Map.Entry<String, Object> entry : session.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    System.out.println(key + " -> " + value);
                }

                return mv;
            }

            ---

        }

&nbsp;
## API REST - JSON

### ATTENTION : Il faut mettre la librairie **gson.jar** dans le **lib du repertoire tomcat**

&nbsp;
### **Méthode 1**

- Au lieu d'aller vers une vue, vous pouvez utiliser la methode API REST en envoyant un JSON grace a votre URL. Si c'est le cas, mettez l'attribut **isJSON** du **modelView** en **true**. Le **JSON** envoye sera l'attribut HashMap **data** du **modelView**, vous pouvez y ajouter vos donnees en utilisant la fonction **ModelView.addItem(key, value)** avec "key" comme cle et "value" comme valeur de la donnee.
- exemple :

        @UrlAnnotation(urlPattern = "emp_all.json.do")
        public ModelView findAllJson() {

            ModelView mv = new ModelView();
            ArrayList<Employe> l = new ArrayList<>();
            Employe e = new Employe("jean");
            l.add(e);
            e = new Employe("jacque");
            l.add(e);
            e = new Employe("patrik");
            l.add(e);

            //ajout de la liste d'employe dans le data
            mv.addItem("listeAllEmp", l);

            //mettre isIsJSON en true
            mv.setIsJSON(true);

            return mv;
        }

&nbsp;
### **Méthode 2**
- Annotez votre méthode par **@RestAPI**, cela permettra de formater et d'envoyer directement en JSON votre retour

- exemple :

        import etu1793.framework.annotationDao.UrlAnnotation;
        import etu1793.framework.annotationDao.RestAPI;

        @RestAPI
        @UrlAnnotation(urlPattern = "client_findAll.do")
        public Client[] findAll() {
            Client c0 = new Client();
            c0.setNom("Jean");
            Client c1 = new Client();
            c1.setNom("joseph");
            Client c2 = new Client();
            c2.setNom("Jasper");

            Client[] listeClients = { c0, c1, c2 };
            return listeClients;
            // listeClients sera converti en JSON et sera envoyé en API Rest
        }

&nbsp;
# donnée vue vers model :
## 1. Utilisant les attributs de votre model
>NB: <strong>les "name" de vos données</strong> (exemple : formulaire) doivent <strong>TOUJOURS CORRESPONDRE AUX NOMS DES ATTRIBUTS de votre model</strong>
### --------------------------------- Upload de fichier-------------------------------------
Si vous voulez uploader un fichier depuis un formulaire
- L'attribut correspondant à votre fichier doit etre de type **FileUpload**  que vous importerez vie le paquet **etu1793.framework.utilitaire.FileUpload**
- le formulaire doit contenir l'attriibut **enctype="multipart/form-data"**
- exemple

        <form action="client_save.do" method="post" enctype="multipart/form-data">
                <p><input type="text" name="nom" id="nom" placeholder="nom" required></p>
                <p><input type="date" name="dateNaissance" id="dateNaissance" placeholder="dateNaissance" required></p>
                <p><input type="file" name="badge" id="badge" placeholder="badge" required></p>
                <p><input type="submit" value="save"></p>
        </form>

- dans fileUpload il y a l'attribut :
> String **name** : stock le nom du fichier (ex : badge.png)

> byte [ ] **file** : stock les bytes du fichier
### --------------------------------- FIN Upload de fichier-------------------------------------
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
