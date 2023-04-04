package etu1793.framework.servlet;

import etu1793.framework.Mapping;
import etu1793.framework.init.Init;
import etu1793.framework.modelView.ModelView;
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
import etu1793.framework.utilitaire.Utilitaire;
import jakarta.servlet.RequestDispatcher;
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
            //String repertoire = workingDir + "/../webapps"+appName+"/WEB-INF/classes";
            //String repertoire = workingDir + "/build/classes";
            String repertoire = getServletContext().getRealPath("/WEB-INF/classes/");
            mappingUrls = Init.getUrlMethods(repertoire);   
        } catch (Exception e) {
            System.out.print(e.getLocalizedMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            String workingDir = System.getProperty("user.dir");
            ServletContext servletContext = getServletContext();
            // Obtenir le nom de l'application à partir du ServletContext
            String appName = servletContext.getContextPath();
            //String repertoire = workingDir + "/../webapps"+appName+"/WEB-INF/classes";
            //String repertoire = workingDir + "/build/classes";
            String repertoire = getServletContext().getRealPath("/WEB-INF/classes/");
            /*out.print("app name "+appName);
            out.print("</br>");
            out.print("user dir "+workingDir);
            out.print("</br>");
            out.print("rep de recherche "+repertoire);
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
            }*/
            
            //out.println("request.getPathInfo() "+request.getPathInfo());
            String urlPattern = Utilitaire.getURLPattern(request);
            //out.println("URL "+urlPattern);
            //out.println("</br>");
            if(mappingUrls.containsKey(urlPattern) == true) {
               //out.println("classe selected "+mappingUrls.get(urlPattern).getClassName());
               //out.println("</br>");
               //out.println("Method selected"+mappingUrls.get(urlPattern).getMethod());
               //out.println("</br>");
               ModelView mv = Utilitaire.getMethodeMV(mappingUrls.get(urlPattern));
               //out.println("modelView.getView -> "+mv.getView());
               //out.println("</br> OK");
               /*if(mv.getData() instanceof HashMap) {

               }*/
               RequestDispatcher dispat = request.getRequestDispatcher(mv.getView());
               dispat.forward(request, response); 
            }
            else {
                out.print("methode referencée par "+urlPattern+" introuvable");
            }
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
