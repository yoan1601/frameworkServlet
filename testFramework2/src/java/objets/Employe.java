package objets;

import etu1793.framework.annotationDao.UrlAnnotation;
import etu1793.framework.annotationDao.ParamAnnotation;
import etu1793.framework.modelView.ModelView;
import java.util.ArrayList;
import java.util.Date;
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

    public Employe(String n, Integer a) {
        setNom(n);
        setAge(a);
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

    @UrlAnnotation(urlPattern = "emp_find_by_id.do")
    public ModelView findById(@ParamAnnotation(description = "id") Integer id,@ParamAnnotation(description = "salut") String salut,@ParamAnnotation(description = "date") Date date) {
        ModelView mv = new ModelView();
        mv.setView("emp_fiche.jsp");
        //mv.setData(new HashMap<String, Object>());
        ArrayList <Employe> l = new ArrayList<>();
        Employe e = new Employe("jean", 18);
        l.add(e);
        e = new Employe("jacque", 32);
        l.add(e);
        e = new Employe("patrik", 25);
        l.add(e);
        mv.addItem("employe", l.get(id));
        mv.addItem("id", id);
        mv.addItem("salut", salut);
        mv.addItem("date", date);
        return mv;
    }

    @UrlAnnotation(urlPattern = "emp_save.do")
    public ModelView save() {
        ModelView mv = new ModelView();
        mv.setView("emp_result_save.jsp");
        //mv.setData(new HashMap<String, Object>());
        System.out.println("nom "+getNom());
        System.out.println("age "+getAge());
        mv.addItem("employe", this);
        return mv;
    }

    @UrlAnnotation(urlPattern = "emp_findAll.do")
    public ModelView findAll() {
        ModelView mv = new ModelView();
        mv.setView("emp.jsp");
        //mv.setData(new HashMap<String, Object>());
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
