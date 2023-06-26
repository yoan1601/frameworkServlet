package objets;

import etu1793.framework.annotationDao.Session;
import etu1793.framework.annotationDao.UrlAnnotation;
import etu1793.framework.annotationDao.ParamAnnotation;
import etu1793.framework.annotationDao.RestAPI;
import etu1793.framework.modelView.ModelView;
import etu1793.framework.utilitaire.FileUpload;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import etu1793.framework.annotationDao.Scope;

@Scope(type = "singleton")
public class Client {
    String nom;
    Date dateNaissance;
    FileUpload badge;
    private int nbAppels = 0;
    HashMap<String, Object> session;

    @RestAPI
    @UrlAnnotation(urlPattern = "client_findAll.do")
    public Client[] findAll() {
        Client c0 = new Client();
        c0.setNom("Jean");
        Client c1 = new Client();
        c1.setNom("joseph");
        Client c2 = new Client();
        c2.setNom("Jasper");

        Client[] lc = { c0, c1, c2 };
        return lc;
    }

    @Session
    @UrlAnnotation(urlPattern = "client_save.do")
    public ModelView save() {
        ModelView mv = new ModelView();
        mv.setView("client_result_save.jsp");
        mv.addItem("client", this);
        nbAppels++;
        mv.addItem("nbAppels", nbAppels);
        System.out.println("les valeurs dans la session actuelle");
        for (Map.Entry<String, Object> entry : getSession().entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            System.out.println(key + " -> " + value);
        }
        return mv;
    }

    public String getNom() {
        return this.nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Date getDateNaissance() {
        return this.dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public FileUpload getBadge() {
        return this.badge;
    }

    public void setBadge(FileUpload badge) {
        this.badge = badge;
    }

    public HashMap<String, Object> getSession() {
        return this.session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }

}
