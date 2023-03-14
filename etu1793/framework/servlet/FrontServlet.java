package etu1793.framework.servlet;

import etu1793.framework.Mapping;
import etu1793.framework.init.Init;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import utilitaire.Utilitaire;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletContext;

public class FrontServlet extends HttpServlet {

    HashMap <String, Mapping> mappingUrls;

    public void init() throws ServletException {
        try {
            String workingDir = System.getProperty("user.dir");
            ServletContext servletContext = getServletContext();
            // Obtenir le nom de l'application à partir du ServletContext
            String appName = servletContext.getContextPath();
            String repertoire = workingDir + "/../webapps"+appName+"/WEB-INF/classes";
            mappingUrls = Init.getUrlMethods(repertoire);   
        } catch (Exception e) {
            System.out.print(e.getLocalizedMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            ServletContext servletContext = getServletContext();
            // Obtenir le nom de l'application à partir du ServletContext
            String appName = servletContext.getContextPath();
            out.print("app name "+appName);
            out.print("</br>");
            for(Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
                String key = entry.getKey();
                Mapping m = entry.getValue();
                out.print("URL "+ key);
                out.print("</br>");
                out.print("Classe "+ m.getClassName());
                out.print("</br>");
                out.print("Methode "+ m.getMethod());
                out.print("</br>");
                out.print("=======");
                out.print("</br>");
            }
            
            String urlPattern = Utilitaire.getURLPattern(request);
            String URL = urlPattern;
            out.println("URL "+URL);
            out.println("</br>");
            out.println("classe selected "+mappingUrls.get(urlPattern).getClassName());
            out.println("</br>");
            out.println("Method selected"+mappingUrls.get(urlPattern).getMethod());
            out.println("</br>");
            /*String workingDir = System.getProperty("user.dir");
            String repertoire = workingDir + "/../webapps"+request.getContextPath()+"/WEB-INF/classes";
            Method m = Init.getMethodAnnotedByUrlPattern(URL, repertoire);
            
            out.println("workingDir "+repertoire);
            out.println("</br>");
            out.println("URL "+urlPattern);
            out.println("</br>");

            if(m == null) {
                out.print("Aucune methode trouvée");
            }

            Mapping mappings = new Mapping();
            mappings.setClassName(m.getDeclaringClass().getName());
            mappings.setMethod(m.getName());
            mappingUrls.put(urlPattern, mappings);

            out.println("</br>");
            out.println("classe "+mappingUrls.get(urlPattern).getClassName());
            out.println("</br>");
            out.println("Method "+mappingUrls.get(urlPattern).getMethod());
            out.println("</br>");*/


        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
