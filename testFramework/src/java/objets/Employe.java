package objets;

import etu1793.framework.annotationDao.UrlAnnotation;

public class Employe {
    @UrlAnnotation(urlPattern = "tenaManao")
    public void myMethode() {
        System.out.println("tena manao");
    }
}
