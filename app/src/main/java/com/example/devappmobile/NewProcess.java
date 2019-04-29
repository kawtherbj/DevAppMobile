package com.example.devappmobile;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.lang.reflect.Array;
import java.sql.SQLOutput;

public class NewProcess {

    private  String pro_uid;
    private  String tas_uid;
    private JSONObject variables;

    public NewProcess(String pro_uid, String tas_uid, JSONObject variables) {
        this.pro_uid = pro_uid;
        this.tas_uid = tas_uid;
        this.variables = variables;
    }

    public String getPro_uid() {
        return pro_uid;
    }

    public void setPro_uid(String pro_uid) {
        this.pro_uid = pro_uid;
    }

    public String getTas_uid() {
        return tas_uid;
    }

    public void setTas_uid(String tas_uid) {
        this.tas_uid = tas_uid;
    }

    public JSONObject getVariables() {
        return variables;
    }

    public void setVariables(JSONObject variables) {
        this.variables = variables;
    }
}
