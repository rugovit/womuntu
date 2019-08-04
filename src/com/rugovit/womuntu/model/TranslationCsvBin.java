package com.rugovit.womuntu.model;

import com.rugovit.womuntu.Main;
import com.sun.istack.NotNull;

import java.util.ArrayList;
import java.util.TreeMap;


public class TranslationCsvBin {
    String commentAndroid;
    String commentIos;
    String elementIdAndroid;
    String elementIdIos;
    TreeMap<String,String> translations=new TreeMap<>();
    ArrayList<String> allHeaders;
    ArrayList<String> allValues;
    public TranslationCsvBin(){
    }
    public TranslationCsvBin(String elementIdAndroid, String elementIdIos) {
        this.elementIdAndroid = elementIdAndroid;
        this.elementIdIos = elementIdIos;
    }
    public TranslationCsvBin(String elementIdAndroid, String elementIdIos,String translationDefalt) {
        this.elementIdAndroid = elementIdAndroid;
        this.elementIdIos = elementIdIos;
        this.translations.put(Main.DEFAULT_LANGUAGE.getDisplayLanguage(),translationDefalt);
    }
    public TranslationCsvBin(String elementIdAndroid, String elementIdIos,@NotNull TreeMap<String,String> translations) {
        this.elementIdAndroid = elementIdAndroid;
        this.elementIdIos = elementIdIos;
        this.translations = translations;
    }

    public String getCommentAndroid() {
        return commentAndroid;
    }

    public void setCommentAndroid(String commentAndroid) {
        this.commentAndroid = commentAndroid;
    }

    public String getCommentIos() {
        return commentIos;
    }

    public void setCommentIos(String commentIos) {
        this.commentIos = commentIos;
    }

    public String getElementIdAndroid() {
        return elementIdAndroid;
    }

    public void setElementIdAndroid(String elementIdAndroid) {
        this.elementIdAndroid = elementIdAndroid;
    }

    public String getElementIdIos() {
        return elementIdIos;
    }

    public void setElementIdIos(String elementIdIos) {
        this.elementIdIos = elementIdIos;
    }

    public TreeMap<String,String> getTranslations() {
        return translations;
    }

    public void setTranslation(@NotNull String language, @NotNull String translation) {
        translations.put(language,translation);
    }

    public ArrayList<String> getAllHeaders() {
        return allHeaders;
    }

    public void setAllHeaders(ArrayList<String> allHeaders) {
        this.allHeaders = allHeaders;
    }

    public ArrayList<String> getAllValues() {
        return allValues;
    }

    public void setAllValues(ArrayList<String> allValues) {
        this.allValues = allValues;
    }
}
