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
import jakarta.servlet.ServletConfig;
import jakarta.servlet.http.HttpSession;

@MultipartConfig
public class FrontServlet extends HttpServlet {

    HashMap<String, Mapping> mappingUrls;
    HashMap<String, Object> singletons;
    String refIsConnected;
    String refRole;
    int sessionState = 0;

    public void init() throws ServletException {
        try {
            String workingDir = System.getProperty("user.dir");
            ServletContext servletContext = this.getServletContext();
            String appName = servletContext.getContextPath();
            String repertoire = this.getServletContext().getRealPath("/WEB-INF/classes/");
            mappingUrls = Init.getUrlMethods(repertoire);
            singletons = Init.getSingletons(repertoire);

            // session
            refIsConnected = this.getInitParameter("isConnected");
            refRole = this.getInitParameter("role");
        } catch (Exception e) {
            System.out.print(e.getLocalizedMessage());
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            try {
                String workingDir = System.getProperty("user.dir");
                ServletContext servletContext = this.getServletContext();

                String appName = servletContext.getContextPath();

                String repertoire = this.getServletContext().getRealPath("/WEB-INF/classes/");

                String urlPattern = Utilitaire.getURLPattern(request);

                if (mappingUrls.containsKey(urlPattern) == true) {

                    // session
                    HttpSession session = request.getSession(); //ne pas creer de nouvelle session

                    Object methodeReturn = Utilitaire.getObjectReturn(mappingUrls.get(urlPattern), request, singletons,
                            refIsConnected, refRole, sessionState);

                    if (methodeReturn instanceof ModelView) {
                        ModelView mv = (ModelView) methodeReturn;

                        //SPRINT 15 - 2 remove session
                        Utilitaire.removeSession(session, mv);

                        System.out.println("mv.getIsJSON() " + mv.getIsJSON());
                        // JSON
                        if (mv.getIsJSON() == true) {

                            String json = Utilitaire.toJson(mv.getData());

                            System.out.println("Json request " + json);

                            // Définir la réponse JSON
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");

                            // Écrire la réponse JSON
                            out.print(json);
                            out.flush();
                        }

                        else {
                            // out.print("ok");
                            if (mv.getData() instanceof HashMap) {
                                for (Map.Entry<String, Object> entry : mv.getData().entrySet()) {
                                    String key = entry.getKey();
                                    Object o = entry.getValue();
                                    request.setAttribute(key, o);
                                }
                            }

                            if (mv.getSession().containsKey(refIsConnected) && mv.getSession().containsKey(refRole)) {
                                // matoa tafiditra ato de methode authentify izy zay
                                session = request.getSession(true); //creer une nouvelle session
                                Utilitaire.setSession(mv, session);
                                sessionState = 1;
                                System.out.println("authentification succes 2.0");
                                System.out.println("session isConnected " + session.getAttribute(refIsConnected));
                                System.out.println("session role " + session.getAttribute(refRole));
                                System.out.println("============================================");
                                //afficher les variables de session
                                Utilitaire.showSessionVar(session);
                            }

                            //SPRINT 15 - 1 
                            if(mv.getInvalidateSession() == true) {
                                session.invalidate();
                                System.out.println("invalidation de la session OK");
                                session = request.getSession(true);
                                System.out.println("creation de la nouvelle session OK");
                                System.out.println("redirection ... vers "+mv.getView());
                                response.sendRedirect(mv.getView());
                            }

                            RequestDispatcher dispat = request.getRequestDispatcher(mv.getView());
                            dispat.forward(request, response);
                        }
                    }

                    else { // JSON par annotation
                        System.out.println("JSON par annotation");
                        String json = Utilitaire.toJson(methodeReturn);

                        System.out.println("Json request " + json);

                        // Définir la réponse JSON
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        // Écrire la réponse JSON
                        out.print(json);
                        out.flush();
                    }

                } else {
                    out.print("methode referencée par " + urlPattern + " introuvable");
                }
            } catch (Exception e) {
                out.println(e.getMessage());
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
