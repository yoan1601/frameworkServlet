package objets;

import etu1793.framework.annotationDao.UrlAnnotation;
import etu1793.framework.modelView.ModelView;
import java.util.ArrayList;
import java.util.HashMap;


public class Employe {

    private String nom;
    private Integer age;

	public Employe() {}

    public Integer getAge() {
		return this.age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

    public Employe(String n) {
        setNom(n);
    }

    public void setNom(String n) {
        nom = n;
    }

    public String getNom() {
        return nom;
    }

    @UrlAnnotation(urlPattern = "emp_save.do")
    public ModelView save() {
        ModelView mv = new ModelView();
        mv.setView("emp_result_save.jsp");
        mv.setData(new HashMap<String, Object>());
        System.out.println("nom "+getNom());
        System.out.println("age "+getAge());
        mv.addItem("employe", this);
        return mv;
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
