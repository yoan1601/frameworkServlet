package etu1793.framework.init;

import etu1793.framework.Mapping;
import etu1793.framework.annotationDao.Scope;
import etu1793.framework.annotationDao.UrlAnnotation;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import etu1793.framework.utilitaire.Utilitaire;

public class Init {

    public static HashMap<String, Object> getSingletons(String repertoire) throws Exception {
        HashMap<String, Object> singletons = new HashMap<String, Object>();
        ArrayList<Class> allClasses = getAllClasses(repertoire);
        Scope scope = null;
        for (Class clazz : allClasses) {
            if(isAnnotedClass(clazz, "Scope") == true) {
                scope = (Scope) clazz.getAnnotation(Scope.class);
                if(scope.type().equalsIgnoreCase("singleton") == true) {
                    singletons.put(clazz.getSimpleName(), null);
                }
            }
        }

        return singletons;
    }


    public static boolean isAnnotedClass(Class c, String annotName) {
        if(annotName.equalsIgnoreCase("Scope")) {
            Scope s = (Scope) c.getAnnotation(Scope.class);
            if(s != null) return true;
        }
        return false;
    }

    public static ArrayList<Class> getAllClasses(String repertoire) throws Exception{
        ArrayList<Class> rep = new ArrayList<>();
        File dossier = new File(repertoire);
        Class[] allClass = null;
        //System.out.println("workingDir "+ workingDir);
        ArrayList <String> pkg = new ArrayList<>();
        pkg = Init.listPackagesWithClasses(dossier, "", pkg);
        for(int i = 0; i < pkg.size(); i++) {
            //System.out.println(pkg.get(i));
            allClass = Utilitaire.getClasses(pkg.get(i));
            for (Class clazz : allClass) {
                rep.add(clazz);
            }
        }
        return rep;
    }

    public static HashMap<String, Mapping> getUrlMethods(String repertoire) throws Exception {
        HashMap<String, Mapping> rep = new HashMap<String, Mapping>();
        Object [] allClassObj = getAllClasses(repertoire).toArray();
        Class [] allClass = new Class[allClassObj.length];
        String url = "";
        UrlAnnotation ua = null;
        Mapping mapping = new Mapping();
        for (int i = 0; i < allClass.length; i++) {
            allClass[i] = (Class) allClassObj[i];
        }
        for (Class clazz : allClass) {
            //System.out.println("> classe "+ clazz.getName());
            Method [] lm = clazz.getMethods();
            for (Method m : lm) {
                if(isAnnotedMethod(m, "UrlAnnotation") == true) {
                    ua = (UrlAnnotation) m.getAnnotation(UrlAnnotation.class);
                    url = ua.urlPattern();
                    mapping = new Mapping();
                    mapping.setClassName(m.getDeclaringClass().getName());
                    mapping.setMethod(m.getName());
                    rep.put(url, mapping);
                }
            }
        }
        return rep;
    }

    public static Method getMethodAnnotedByUrlPattern(String urlPattern, String repertoire) throws Exception {
        //String workingDir = System.getProperty("user.dir");
        File dossier = new File(repertoire);
        Class[] allClass = null;
        //System.out.println("workingDir "+ workingDir);
        ArrayList <String> pkg = new ArrayList<>();
        UrlAnnotation ua = null;
        pkg = Init.listPackagesWithClasses(dossier, "", pkg);
        for(int i = 0; i < pkg.size(); i++) {
            //System.out.println(pkg.get(i));
            allClass = Utilitaire.getClasses(pkg.get(i));
            for (Class clazz : allClass) {
                //System.out.println("> classe "+ clazz.getName());
                Method [] lm = clazz.getMethods();
                for (Method m : lm) {
                    if(isAnnotedMethod(m, "UrlAnnotation") == true) {
                        ua = (UrlAnnotation) m.getAnnotation(UrlAnnotation.class);
                        if(urlPattern.equals(ua.urlPattern()) == true) {
                            return m;
                        }
                    }
                }
            }
        }
        return null;
    }

    public static boolean isAnnotedMethod(Method m, String annotName) {

        if (annotName.equalsIgnoreCase("UrlAnnotation")) {
            UrlAnnotation ua = (UrlAnnotation) m.getAnnotation(UrlAnnotation.class);
            if(ua != null) return true;
        }

        return false;

    }

    public static ArrayList<String> listPackagesWithClasses(File directory, String packageName, ArrayList<String> rep) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    String newPackageName = packageName + file.getName() + ".";
                    listPackagesWithClasses(file, newPackageName, rep);
                } else if (file.getName().endsWith(".class")) {
                    String className = file.getName().substring(0, file.getName().length() - 6);
                    String fullClassName = packageName + className;
                    String packageNameWithoutDot = packageName.substring(0, packageName.length() - 1);
                    String packageNameOnly = packageNameWithoutDot.isEmpty() ? "<default package>"
                            : packageNameWithoutDot;
                    // System.out.println(packageNameOnly);
                    rep.add(packageNameOnly);
                }
            }
        }
        return rep;
    }
}
