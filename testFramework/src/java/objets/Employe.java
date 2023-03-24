package objets;

import etu1793.framework.annotationDao.UrlAnnotation;
import etu1793.framework.modelView.ModelView;

public class Employe {
    @UrlAnnotation(urlPattern = "tenaManao")
    public ModelView toIndex() {
        System.out.println("tena manao");
        ModelView mv = new ModelView();
        mv.setView("index.html");
        return mv;
    }
}
