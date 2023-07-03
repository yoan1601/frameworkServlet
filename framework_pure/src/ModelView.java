package etu1793.framework.modelView;

import java.util.ArrayList;
import java.util.HashMap;

public class ModelView {
    String view;
    HashMap<String, Object> data = new HashMap<String, Object>();
    HashMap<String, Object> session = new HashMap<String, Object>();
    boolean isJSON = false;
    boolean invalidateSession = false;
    ArrayList<String> removeSession = new ArrayList<>();

    public ArrayList<String> getRemoveSession() {
        return removeSession;
    }

    public void setRemoveSession(ArrayList<String> removeSession) {
        this.removeSession = removeSession;
    }

    public boolean getInvalidateSession() {
        return invalidateSession;
    }

    public void setInvalidateSession(boolean invalidateSession) {
        this.invalidateSession = invalidateSession;
    }

    public HashMap<String, Object> getSession() {
        return this.session;
    }

    public void setSession(HashMap<String, Object> session) {
        this.session = session;
    }

    public void addItem(String key, Object value) {
        data.put(key, value);
    }

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> Data) {
        this.data = Data;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public boolean getIsJSON() {
        return isJSON;
    }

    public void setIsJSON(boolean isJSON) {
        this.isJSON = isJSON;
    }
}
