
package etu1793.framework.utilitaire;

import etu1793.framework.Mapping;
import etu1793.framework.modelView.ModelView;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.ZoneId;

public class Utilitaire {

    @SuppressWarnings("rawtypes")

    public static Object[] getListeObjetsParametres(Method m, HttpServletRequest request) throws Exception {
        Parameter [] lp = m.getParameters();
        Object[] rep = new Object[lp.length];

        Map<String, String[]> parameterMap = request.getParameterMap();
        for (int i = 0; i < lp.length; i++) {
            System.out.println("isNamePresent "+lp[i].isNamePresent());
            System.out.println("nom param "+ lp[i].getName());
            Annotation[] annotes=lp[i].getAnnotations();
            for (Annotation annotation : annotes) {
                if(annotation.annotationType().getSimpleName().equals("ParamAnnotation")) {
                    String valStr = request.getParameter(annotation.annotationType().getMethod("description").invoke(annotation).toString());
                    if(valStr != null) {
                        Class typeParametre = lp[i].getType();
                        if (typeParametre == int.class) {
                            int intValue = Integer.parseInt(valStr);
                            rep[i] = intValue;
                        }
                        else if (typeParametre == Integer.class) {
                            Integer intValue = Integer.parseInt(valStr);
                            rep[i] = intValue;
                        }
                        else if (typeParametre == double.class) {
                            double doubleValue = Double.parseDouble(valStr);
                            rep[i] = doubleValue;
                        }
                        else if (typeParametre == Double.class) {
                            Double doubleValue = Double.parseDouble(valStr);
                            rep[i] = doubleValue;
                        }
                        else if (typeParametre == boolean.class) {
                            boolean booleanValue = Boolean.parseBoolean(valStr);
                            rep[i] = booleanValue;
                        } else if (typeParametre == String.class) {
                            rep[i] = valStr;
                        }
                        else if (typeParametre == Date.class) {
                            LocalDate localDate = LocalDate.parse(valStr.replaceAll("\"", ""), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                            rep[i] = date;
                        }
                    }
                    else {
                        rep[i] = null;
                    }
                }
            }
        }

        return rep;
    }


    public static Object getObjetAttributSetted(Class clazz ,HttpServletRequest request) throws Exception {
        Object o = clazz.getConstructor().newInstance();

        //set des attributs correspondants
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (String parameterName : parameterMap.keySet()) {
            //verifier ra nom ana attribut ao amle classe ilay parametre
            if(attributeExists(clazz, parameterName) == true) {
                Field field = clazz.getDeclaredField(parameterName);
                field.setAccessible(true);
                Class typeAttribut = field.getType();
                String[] parameterValues = parameterMap.get(parameterName);
                String valStr = parameterValues[0];
                if (typeAttribut == int.class) {
                    int intValue = Integer.parseInt(valStr);
                    field.setInt(o, intValue);
                }
                else if (typeAttribut == Integer.class) {
                    Integer intValue = Integer.parseInt(valStr);
                    field.set(o, intValue);
                }
                else if (typeAttribut == double.class) {
                    double doubleValue = Double.parseDouble(valStr);
                    field.setDouble(o, doubleValue);
                }
                else if (typeAttribut == Double.class) {
                    Double doubleValue = Double.parseDouble(valStr);
                    field.set(o, doubleValue);
                }
                else if (typeAttribut == boolean.class) {
                    boolean booleanValue = Boolean.parseBoolean(valStr);
                    field.setBoolean(o, booleanValue);
                } else if (typeAttribut == String.class) {
                    field.set(o, valStr);
                }
                else if (typeAttribut == Date.class) {
                    LocalDate localDate = LocalDate.parse(valStr.replaceAll("\"", ""), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    field.set(o, date);
                }
            }
        }
        return o;
    }

    public static ModelView getMethodeMV(Mapping mapping, HttpServletRequest request) throws Exception {
        String className = mapping.getClassName();
        String methodName = mapping.getMethod();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class clazz = loader.loadClass(className);
        Method methode = null;
        for (Method m : clazz.getDeclaredMethods()) {
            if(methodName == m.getName()) {
                methode = m;
            }
        }

        if(methode == null) throw new Exception("aucune methode ne correspond à "+methodName);

        Object o = getObjetAttributSetted(clazz, request);
        ModelView mv = null;

        if(methode.getParameters().length > 0) {
            Object[] arguments = getListeObjetsParametres(methode, request);
            mv = (ModelView) methode.invoke(o, arguments);
        }
        else {
            mv = (ModelView) methode.invoke(o);
        }
        return mv;
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
