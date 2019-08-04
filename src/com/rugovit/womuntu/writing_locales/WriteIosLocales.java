package com.rugovit.womuntu.writing_locales;
import com.rugovit.womuntu.model.LocalisationElement;
import com.sun.istack.NotNull;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class WriteIosLocales extends  WriteLocals {
    TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationList;
    ArrayList<LocalisationElement> elementsTamplatesList = null;
    String projectPath;
    public WriteIosLocales(@NotNull TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationList) {
        this.localisationList = localisationList;
    }

    public WriteIosLocales(@NotNull TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationList, @NotNull ArrayList<LocalisationElement> elementsTamplatesList) {
        this.localisationList = adaptListToTemplateIos(localisationList, elementsTamplatesList);
        this.elementsTamplatesList = elementsTamplatesList;

    }
    public WriteIosLocales(@NotNull TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationList, @NotNull ArrayList<LocalisationElement> elementsTamplatesList, String projectPath) {
        this.localisationList = adaptListToTemplateIos(localisationList, elementsTamplatesList);
        this.elementsTamplatesList = elementsTamplatesList;
        this.projectPath=projectPath;

    }
    public WriteIosLocales write() throws URISyntaxException {
        for(Map.Entry<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> entry:localisationList.entrySet()){
            com.rugovit.womuntu.util.FileUtils.writeIosStrings(getPathForLanguage(entry.getKey().getLocale()),entry.getValue());
        }
        return this;
    }
    private String getPathForLanguage(Locale locale) throws URISyntaxException {
        String folder;
        String separator = File.separator;
        if(locale.getCountry()!=null&&!locale.getCountry().isEmpty())  folder=locale.getLanguage()+"-"+locale.getCountry().toUpperCase()+".lproj";
        else  folder= locale.getLanguage()+".lproj";
        if(projectPath==null)return  com.rugovit.womuntu.util.FileUtils.getCurrentDirctoryPath() +separator+"ios"+separator+folder+separator+ "Localizable.strings";
        else return  projectPath+separator+folder+separator+ "Localizable.strings";
    }
}
