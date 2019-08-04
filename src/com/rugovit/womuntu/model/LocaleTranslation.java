package com.rugovit.womuntu.model;

public class LocaleTranslation extends com.rugovit.womuntu.model.LocalisationElement {

    String elementIdAndroid;
    String elementIdIos;
    String translation;


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

    public String getTranslation() {
        return translation;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }

}
