
package etu1793.framework.utilitaire;

import etu1793.framework.Mapping;
import etu1793.framework.annotationDao.RestAPI;
import etu1793.framework.annotationDao.Session;
import etu1793.framework.annotationDao.auth;
import etu1793.framework.annotationDao.Login;
import etu1793.framework.modelView.ModelView;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.ZoneId;
import jakarta.servlet.http.Part;
import java.util.Collection;
import java.nio.file.Files;
import java.util.HashMap;
import jakarta.servlet.http.HttpSession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Utilitaire {

    @SuppressWarnings("rawtypes")

    public static void removeSession(HttpSession session, ModelView mv) throws Exception {
        if (session != null) {
            for (int i = 0; i < mv.getRemoveSession().size(); i++) {
                String toRemove = mv.getRemoveSession().get(i);
                if (session.getAttribute(toRemove) != null) {
                    session.removeAttribute(toRemove);
                }
            }
        }
    }

    public static void showSessionVar(HttpSession session) throws Exception {
        System.out.println("Variables in session right now : ");
        if (session != null) {
            // Récupérer l'énumérateur des noms d'attributs de session
            Enumeration<String> attributeNames = session.getAttributeNames();

            // Parcourir les noms d'attributs et afficher les valeurs correspondantes
            while (attributeNames.hasMoreElements()) {
                String attributeName = attributeNames.nextElement();
                Object attributeValue = session.getAttribute(attributeName);

                // Faites quelque chose avec l'attribut (attributeName, attributeValue)
                // Par exemple, l'afficher dans la console
                System.out.println(attributeName + " : " + attributeValue);
            }
        }
    }

    public static boolean isRestAPIJSON(Method m) {
        RestAPI restAPI = (RestAPI) m.getAnnotation(RestAPI.class);
        if (restAPI == null)
            return false;
        return true;
    }

    public static String toJson(Object object) {
        try {
            System.out.println("toJson");
            GsonBuilder builder = new GsonBuilder();
            System.out.println("builder " + builder);
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            String jsonString = gson.toJson(object);
            System.out.println("jsonString " + jsonString);
            return jsonString;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    static void processUserSession(Method methode, Object object, HttpSession httpSession) throws Exception {
        Session session = (Session) methode.getAnnotation(Session.class);
        if (session != null) { // mila session
            Class clazz = methode.getDeclaringClass();
            try {
                Field sess = clazz.getDeclaredField("session");
                sess.setAccessible(true);

                HashMap<String, Object> attrSessionValue = new HashMap<>();

                Enumeration<String> attributeNames = httpSession.getAttributeNames();

                while (attributeNames.hasMoreElements()) {
                    String attributeName = attributeNames.nextElement();
                    Object attributeValue = httpSession.getAttribute(attributeName);
                    attrSessionValue.put(attributeName, attributeValue);
                }

                sess.set(object, attrSessionValue);
                System.out.println("session setted and available for the user in the methode " + methode.getName());

            } catch (Exception e) {
                throw new Exception("la methode " + methode.getName()
                        + " utilise la session cependant la classe declarante " + clazz.getSimpleName()
                        + " ne contient pas d'attribut HashMap<String, Object> session pour stocker la session");
            }

        }
    }

    public static void setSession(ModelView mv, HttpSession session) throws Exception {
        HashMap<String, Object> mvSession = mv.getSession();
        for (Map.Entry<String, Object> entry : mvSession.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            session.setAttribute(key, value);
        }
        /*
         * session.setAttribute(refIsConnected, mv.getSession().get(refIsConnected));
         * session.setAttribute(refRole, mv.getSession().get(refRole));
         */
    }

    static void verifIfNecessaryAuth(Method methode, HttpServletRequest request, String refIsConnected, String refRole,
            int sessionState)
            throws Exception {
        HttpSession session = request.getSession();
        Object isCo = session.getAttribute(refIsConnected);

        if (isCo == null) {
            if (sessionState > 0)
                throw new Exception("Oops ! No user connected");
        }

        if (isAnnotedMethodAuth(methode)) {
            auth au = (auth) methode.getAnnotation(auth.class);
            String roleNecessaire = au.role();

            if (((String) session.getAttribute(refRole)).equalsIgnoreCase("admin") == false) { // si autre que admin
                if (roleNecessaire.equalsIgnoreCase("admin") == true) { // mila privilege admin
                    throw new Exception("access denied de la methode " + methode.getName() + " privilege : "
                            + roleNecessaire + " , votre role : " + session.getAttribute(refRole));
                }
            }
            System.out.println("== methode " + methode.getName() + " auth necessaire");
            System.out.println("== role necessaire " + roleNecessaire + " votre role " + session.getAttribute(refRole));
        }
    }

    public static boolean isAnnotedMethodAuth(Method m) {

        auth au = (auth) m.getAnnotation(auth.class);
        if (au != null)
            return true;

        return false;

    }

    // avec upload
    public static Object getObjetAttributSetted(Class clazz, HttpServletRequest request,
            HashMap<String, Object> singletons) throws Exception {
        Object o = null;
        String className = clazz.getSimpleName();

        // si classe singleton
        if (singletons.containsKey(className) == true) {
            System.out.println(className + " est un singleton");
            o = singletons.get(className);
            if (o == null) {
                System.out.println(className + " : objet encore null");
                o = clazz.getConstructor().newInstance();
                singletons.put(className, o);
            } else {
                System.out.println(className + " : objet deja instance ... reset des valeurs des attributs");
                Field[] allFields = o.getClass().getDeclaredFields();
                Class typeAttribut = null;
                for (Field field : allFields) {
                    field.setAccessible(true);
                    typeAttribut = field.getType();
                    if (typeAttribut == int.class || typeAttribut == double.class) {
                        field.set(o, 0);
                        System.out
                                .println(field.getName() + " : " + typeAttribut.getSimpleName() + " reinitialise en 0");
                    } else {
                        field.set(o, null);
                        System.out.println(
                                field.getName() + " : " + typeAttribut.getSimpleName() + " reinitialise en null");
                    }

                }
            }
        }
        // sinon
        else {
            o = clazz.getConstructor().newInstance();
        }

        // set des attributs correspondants
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String parameterName : parameterMap.keySet()) {
            // verifier ra nom ana attribut ao amle classe ilay parametre
            if (attributeExists(clazz, parameterName) == true) {
                Field field = clazz.getDeclaredField(parameterName);
                field.setAccessible(true);
                Class typeAttribut = field.getType();
                String[] parameterValues = parameterMap.get(parameterName);
                String valStr = parameterValues[0];
                if (typeAttribut == int.class) {
                    int intValue = Integer.parseInt(valStr);
                    field.setInt(o, intValue);
                } else if (typeAttribut == Integer.class) {
                    Integer intValue = Integer.parseInt(valStr);
                    field.set(o, intValue);
                } else if (typeAttribut == double.class) {
                    double doubleValue = Double.parseDouble(valStr);
                    field.setDouble(o, doubleValue);
                } else if (typeAttribut == Double.class) {
                    Double doubleValue = Double.parseDouble(valStr);
                    field.set(o, doubleValue);
                } else if (typeAttribut == boolean.class) {
                    boolean booleanValue = Boolean.parseBoolean(valStr);
                    field.setBoolean(o, booleanValue);
                } else if (typeAttribut == String.class) {
                    field.set(o, valStr);
                } else if (typeAttribut == Date.class) {
                    LocalDate localDate = LocalDate.parse(valStr.replaceAll("\"", ""),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    field.set(o, date);
                }
            }
        }

        String contentType = request.getContentType();

        if (contentType != null && contentType.startsWith("multipart/")) {
            try {
                // traitement en cas de input type = file
                // Récupérer tous les objets Part du formulaire
                Collection<Part> parts = request.getParts();
                for (Part part : parts) {
                    if (attributeExists(clazz, part.getName()) == true) {
                        Field field = clazz.getDeclaredField(part.getName());
                        field.setAccessible(true);
                        Class typeAttribut = field.getType();
                        if (typeAttribut == FileUpload.class) {
                            FileUpload fileUpload = getFileUploadAfterTraitement(part);
                            field.set(o, fileUpload);
                        }
                    }
                }
            } catch (Exception e) {
                // Gestion de l'exception si la requête n'est pas multipart ou en cas d'erreur
                // de traitement multipart
            }
        }

        return o;
    }

    public static FileUpload getFileUploadAfterTraitement(Part part) throws Exception {
        FileUpload fileUpload = new FileUpload();

        // Récupérer le name du champ file
        // String name = part.getName();

        // Extraire le nom du fichier
        String nomFichier = getNomFichier(part);
        fileUpload.setName(nomFichier);

        // Récupérer les octets (bytes) du fichier
        byte[] octetsFichier = lireOctetsFichier(part);

        // Faites ce que vous voulez avec le name, le nom du fichier et les octets
        // Par exemple, enregistrez le fichier dans un emplacement spécifique
        // enregistrerFichier(name, nomFichier, octetsFichier);

        fileUpload.setFile(octetsFichier);

        return fileUpload;

    }

    // Méthode utilitaire pour lire les octets (bytes) à partir d'un objet Part
    public static byte[] lireOctetsFichier(Part part) throws Exception {
        return part.getInputStream().readAllBytes();
    }

    // Méthode utilitaire pour enregistrer les octets (bytes) dans un fichier
    public static void enregistrerFichier(String name, String nomFichier, byte[] octetsFichier) throws Exception {
        // Spécifiez le chemin d'enregistrement du fichier en utilisant le name
        String cheminEnregistrement = "./" + name + "_" + nomFichier;

        // Enregistrez les octets dans le fichier en utilisant Files.write()
        Files.write(Path.of(cheminEnregistrement), octetsFichier);
    }

    public static String getNomFichier(Part part) {
        String contenuDisposition = part.getHeader("content-disposition");
        String[] elements = contenuDisposition.split(";");
        for (String element : elements) {
            if (element.trim().startsWith("filename")) {
                return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }

    public static Object[] getListeObjetsParametres(Method m, HttpServletRequest request) throws Exception {
        Parameter[] lp = m.getParameters();
        Object[] rep = new Object[lp.length];

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (int i = 0; i < lp.length; i++) {
            System.out.println("isNamePresent " + lp[i].isNamePresent());
            System.out.println("nom param " + lp[i].getName());
            Annotation[] annotes = lp[i].getAnnotations();
            for (Annotation annotation : annotes) {
                if (annotation.annotationType().getSimpleName().equals("ParamAnnotation")) {
                    String valStr = request.getParameter(
                            annotation.annotationType().getMethod("description").invoke(annotation).toString());
                    if (valStr != null) {
                        Class typeParametre = lp[i].getType();
                        if (typeParametre == int.class) {
                            int intValue = Integer.parseInt(valStr);
                            rep[i] = intValue;
                        } else if (typeParametre == Integer.class) {
                            Integer intValue = Integer.parseInt(valStr);
                            rep[i] = intValue;
                        } else if (typeParametre == double.class) {
                            double doubleValue = Double.parseDouble(valStr);
                            rep[i] = doubleValue;
                        } else if (typeParametre == Double.class) {
                            Double doubleValue = Double.parseDouble(valStr);
                            rep[i] = doubleValue;
                        } else if (typeParametre == boolean.class) {
                            boolean booleanValue = Boolean.parseBoolean(valStr);
                            rep[i] = booleanValue;
                        } else if (typeParametre == String.class) {
                            rep[i] = valStr;
                        } else if (typeParametre == Date.class) {
                            LocalDate localDate = LocalDate.parse(valStr.replaceAll("\"", ""),
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            rep[i] = date;
                        }
                    } else {
                        rep[i] = null;
                    }
                }
            }
        }

        return rep;
    }

    public static ArrayList<Object> getObjectReturn(Mapping mapping, HttpServletRequest request,
            HashMap<String, Object> singletons, String refIsConnected, String refRole, int sessionState)
            throws Exception {
        System.out.println("============================");
        String className = mapping.getClassName();
        String methodName = mapping.getMethod();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class clazz = loader.loadClass(className);
        Method methode = null;
        for (Method m : clazz.getDeclaredMethods()) {
            if (methodName == m.getName()) {
                methode = m;
            }
        }

        if (methode == null)
            throw new Exception("aucune methode ne correspond à " + methodName);

        System.out.println("methode " + methodName);

        ArrayList<Object> retour = new ArrayList<>();
        retour.add(methode);

        // authentification
        HttpSession session = request.getSession(false);
        // boolean isNew = session.isNew();
        // System.out.println("session.isNew " + isNew);
        // if (isNew == false) {
        Login loginAnnot = (Login) methode.getAnnotation(Login.class);
        if(loginAnnot == null) {
            verifIfNecessaryAuth(methode, request, refIsConnected, refRole, sessionState);
        }
        // }

        Object o = getObjetAttributSetted(clazz, request, singletons);

        // sprint 12
        processUserSession(methode, o, session);

        ModelView mv = null;
        Object toRestAPI = new Object();

        if (isRestAPIJSON(methode) == false) {
            if (methode.getParameters().length > 0) {
                Object[] arguments = getListeObjetsParametres(methode, request);
                Object objet = methode.invoke(o, arguments);
                if(objet instanceof ModelView) {
                    mv = (ModelView) objet;
                    System.out.println("methode " + methodName);
                    retour.add(mv);
                    return retour;
                }
                else {
                    retour.add(objet);
                    return retour;
                }
            } else {
                Object objet = methode.invoke(o);
                if(objet instanceof ModelView) {
                    mv = (ModelView) objet;
                    retour.add(mv);
                    return retour;
                }
                else {
                    retour.add(objet);
                    return retour;
                }
            }
        } else {
            if (methode.getParameters().length > 0) {
                Object[] arguments = getListeObjetsParametres(methode, request);
                toRestAPI = methode.invoke(o, arguments);
            } else {
                toRestAPI = methode.invoke(o);
            }
            System.out.println("methode utilisant REST API " + methodName);
            
            retour.add(toRestAPI);
            return retour;
        }

    }

    public static boolean attributeExists(Class clazz, String attributeName) {
        try {
            Field field = clazz.getDeclaredField(attributeName);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    public static String getURLPattern(HttpServletRequest request) throws Exception {
        // String rep = request.getPathInfo();
        String rep = request.getServletPath();
        if (rep == null) {
            rep = "/";
            return rep;
        }
        // raha asorina le / eo am voalohany
        return rep.substring(1);
    }

    public static ArrayList<Object> constructObjectByClasses(Class[] classes) throws Exception {
        Object o = new Object();
        ArrayList<Object> rep = new ArrayList<>();
        for (Class classe : classes) {
            Constructor c = classe.getConstructor();
            o = c.newInstance();
            rep.add(o);
        }
        return rep;
    }

    public static Class[] getClasses(String pckgname) throws ClassNotFoundException {
        ArrayList<Class> classes = new ArrayList<>();

        File directory = null;
        try {
            ClassLoader cld = Thread.currentThread().getContextClassLoader();
            if (cld == null) {
                throw new ClassNotFoundException("la classe loader ne peut etre obtenue.");
            }
            String path = pckgname.replace('.', '/');
            URL resource = cld.getResource(path);
            if (resource == null) {
                throw new ClassNotFoundException("pas de ressource dans le paquet " + path);
            }
            directory = new File(resource.getFile());
        } catch (NullPointerException x) {
            throw new ClassNotFoundException(pckgname + " (" + directory + ") n'est pas un paquet valide");
        }
        if (directory.exists()) {
            // avoir liste des fichiers dans le paquet
            String[] files = directory.list();
            for (String file : files) {
                // .class ihany no alaina
                if (file.endsWith(".class")) {
                    classes.add(Class.forName(pckgname + '.' + file.substring(0, file.length() - 6)));
                }
            }
        } else {
            throw new ClassNotFoundException(pckgname + " n'est pas un paquet valide");
        }
        Class[] classesA = new Class[classes.size()];
        classes.toArray(classesA);
        return classesA;
    }

    public static void displayFieldsName(ArrayList<Field> lf) {
        for (int i = 0; i < lf.size(); i++) {
            Field get = lf.get(i);
            System.out.println("---> " + get.getName());
        }
    }
}
