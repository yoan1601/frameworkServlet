
package etu1793.framework;
import etu1793.framework.annotationDao.UrlAnnotation;


public class Mapping {
    String className;
    String method;

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

    @UrlAnnotation(urlPattern = "tenaManao")
    public void myMethode() {
        System.out.println("tena manao");
    }
}
