package com.rugovit.womuntu.model;

import java.util.ArrayList;

public class CsvRowTranslation {
    String androidComment;
    String iosComment;
    String androidKey;
    String iosKey;
    ArrayList<com.rugovit.womuntu.model.LocalisationElement> localisationElements;

    public String getAndroidComment() {
        return androidComment;
    }

    public void setAndroidComment(String androidComment) {
        this.androidComment = androidComment;
    }

    public String getIosComment() {
        return iosComment;
    }

    public void setIosComment(String iosComment) {
        this.iosComment = iosComment;
    }

    public String getAndroidKey() {
        return androidKey;
    }

    public void setAndroidKey(String androidKey) {
        this.androidKey = androidKey;
    }

    public String getIosKey() {
        return iosKey;
    }

    public void setIosKey(String iosKey) {
        this.iosKey = iosKey;
    }

    public ArrayList<com.rugovit.womuntu.model.LocalisationElement> getLocalisationElements() {
        return localisationElements;
    }

    public void setLocalisationElements(ArrayList<com.rugovit.womuntu.model.LocalisationElement> localisationElements) {
        this.localisationElements = localisationElements;
    }
}
