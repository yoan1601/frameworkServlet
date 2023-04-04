package objets;

import java.util.ArrayList;
import java.util.HashMap;

import etu1793.framework.annotationDao.UrlAnnotation;
import etu1793.framework.modelView.ModelView;

public class Employe {

    String nom;

    public Employe() {}
    public Employe(String n) {
        setNom(n);
    }

    public void setNom(String n) {
        nom = n;
    }

    public String getNom() {
        return nom;
    }

    @UrlAnnotation(urlPattern = "findAll.do")
    public ModelView findAll() {
        ModelView mv = new ModelView();
        mv.setView("emp.jsp");
        mv.setData(new HashMap<String, Object>());
        ArrayList <Employe> l = new ArrayList<>();
        Employe e = new Employe("jean");
        l.add(e);
        e = new Employe("jacque");
        l.add(e);
        e = new Employe("patrik");
        l.add(e);
        mv.addItem("listeAllEmp", l);
        return mv;
    }

    @UrlAnnotation(urlPattern = "okay.do")
    public ModelView toIndex() {
        System.out.println("tena manao");
        ModelView mv = new ModelView();
        //mv.setView("coucou.html");
        mv.setView("coucou.jsp");
        return mv;
    }
}
