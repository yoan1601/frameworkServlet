package objets;

import etu1793.framework.annotationDao.UrlAnnotation;
import etu1793.framework.modelView.ModelView;

public class Employe {
    @UrlAnnotation(urlPattern = "okay.do")
    public ModelView toIndex() {
        System.out.println("tena manao");
        ModelView mv = new ModelView();
        mv.setView("coucou.html");
        return mv;
    }
}
