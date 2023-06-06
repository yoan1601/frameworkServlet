package objets;

import etu1793.framework.annotationDao.UrlAnnotation;
import etu1793.framework.annotationDao.ParamAnnotation;
import etu1793.framework.modelView.ModelView;
import etu1793.framework.utilitaire.FileUpload;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import etu1793.framework.annotationDao.Scope;

@Scope(type = "singleton")
public class Client {
    String nom;
    Date dateNaissance;
    FileUpload badge;
    private int nbAppels = 0;

    @UrlAnnotation(urlPattern = "client_save.do")
    public ModelView save() {
        ModelView mv = new ModelView();
        mv.setView("client_result_save.jsp");
        mv.addItem("client", this);
        nbAppels++;
        mv.addItem("nbAppels",nbAppels);
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

}
