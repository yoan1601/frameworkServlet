package etu1793.framework.servlet;

import etu1793.framework.Mapping;
import etu1793.framework.init.Init;
import etu1793.framework.modelView.ModelView;
import etu1793.framework.utilitaire.Utilitaire;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.ServletContext;
import jakarta.servlet.annotation.MultipartConfig;

@MultipartConfig
public class FrontServlet extends HttpServlet {

    HashMap<String, Mapping> mappingUrls;

    public void init() throws ServletException {
        try {
            String workingDir = System.getProperty("user.dir");
            ServletContext servletContext = getServletContext();
            String appName = servletContext.getContextPath();
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

            String appName = servletContext.getContextPath();

            String repertoire = getServletContext().getRealPath("/WEB-INF/classes/");

            String urlPattern = Utilitaire.getURLPattern(request);

            if (mappingUrls.containsKey(urlPattern) == true) {
                ModelView mv = Utilitaire.getMethodeMV(mappingUrls.get(urlPattern), request);

                out.print("ok");
                if (mv.getData() instanceof HashMap) {
                    for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                        String key = entry.getKey();
                        Object o = entry.getValue();
                        request.setAttribute(key, o);
                    }
                }
                RequestDispatcher dispat = request.getRequestDispatcher(mv.getView());
                dispat.forward(request, response);
            } else {
                out.print("methode referencée par " + urlPattern + " introuvable");
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
