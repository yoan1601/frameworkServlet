
package etu1793.framework.utilitaire;

import etu1793.framework.Mapping;
import etu1793.framework.modelView.ModelView;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;



public class Utilitaire {
    
    public static ModelView getMethodeMV(Mapping mapping) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String className = mapping.getClassName();
        String methodName = mapping.getMethod();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class clazz = loader.loadClass(className);
        Method methode = clazz.getMethod(methodName);
        Object o = clazz.getConstructor().newInstance();
        ModelView mv = (ModelView) methode.invoke(o);
        return mv;
    }

    public static String getURLPattern(HttpServletRequest request) throws Exception {
        //String rep = request.getPathInfo();
        String rep = request.getServletPath();
        if(rep == null){
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
