package deconnect;

import etu1793.framework.annotationDao.ParamAnnotation;
import etu1793.framework.annotationDao.UrlAnnotation;
import etu1793.framework.modelView.ModelView;

public class Logout {
    @UrlAnnotation(urlPattern = "logout.do")
    public ModelView logout() {
        ModelView mv = new ModelView();

        mv.setInvalidateSession(true);
        mv.setView("index.jsp");

        return mv;
    }
}
