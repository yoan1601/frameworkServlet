package etu1793.framework.modelView;

import java.util.HashMap;

public class ModelView {
    String view;
    HashMap<String, Object> data;

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
}
