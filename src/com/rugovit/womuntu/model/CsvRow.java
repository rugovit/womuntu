package com.rugovit.womuntu.model;

import java.util.HashMap;

public class CsvRow {
    HashMap<String,String> data=new HashMap<>();
    String[]  header=null;
    public String getCell(String columnName){
        return data.get(columnName);
    }
    public void putCell(String columnName, String value){
        data.put(columnName,value);
    }
    public boolean containsValue(String columnName,String value){
        return data.get(columnName).contains(value);
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public String[]  getHeader() {
        return header;
    }

    public void setHeader(String[]  header) {
        this.header = header;
    }
}
