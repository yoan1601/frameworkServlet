package init;

import etu1793.framework.annotationDao.ParamAnnotation;
import etu1793.framework.annotationDao.Scope;
import etu1793.framework.annotationDao.UrlAnnotation;
import etu1793.framework.annotationDao.Login;
import etu1793.framework.modelView.ModelView;

@Scope(type = "singleton")
public class Init {

    @UrlAnnotation(urlPattern = "auth.do")
    @Login
    public ModelView authentify(@ParamAnnotation(description = "login") String login,
            @ParamAnnotation(description = "pwd") String pwd) {
        ModelView mv = new ModelView();

        if (login.equals("yoan") && pwd.equals("1234")) {
            System.out.println("login " + login + " pwd " + pwd);
            mv.getSession().put("isConnected", true);
            mv.getSession().put("role", "autre");
            mv.getSession().put("toSuppress", "zavatra");
            mv.setView("accueil.jsp");
        }

        else if (login.equals("admin") && pwd.equals("1234")) {
            System.out.println("login " + login + " pwd " + pwd);
            mv.getSession().put("isConnected", true);
            mv.getSession().put("role", "admin");
            mv.getSession().put("toSuppress", "zavatra");
            mv.setView("accueil.jsp");
        }

        else {
            mv.setInvalidateSession(true);
            mv.setView("index.jsp");
        }

        return mv;
    }
}
