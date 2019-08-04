package com.rugovit.womuntu.writing_locales;
import com.rugovit.womuntu.model.LocalisationElement;
import com.sun.istack.NotNull;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class WriteAndroidLocales extends  WriteLocals{
    String projectPath=null;
    Locale defoltLocale;
    TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationList;
    ArrayList<LocalisationElement> elementsTamplatesList = null;
    public WriteAndroidLocales(@NotNull TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationList, @NotNull Locale defoltLocale ) {
        this.localisationList = localisationList;
        this.defoltLocale=defoltLocale;
    }

    public WriteAndroidLocales(@NotNull TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationList, @NotNull ArrayList<LocalisationElement> elementsTamplatesList, @NotNull Locale defoltLocale) {
        this.localisationList = adaptListToTemplateAndroid(localisationList, elementsTamplatesList);
        this.elementsTamplatesList = elementsTamplatesList;
        this.defoltLocale=defoltLocale;

    }
    public WriteAndroidLocales(@NotNull TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationList, @NotNull ArrayList<LocalisationElement> elementsTamplatesList, @NotNull Locale defoltLocale, String projectPath) {
        this.localisationList = adaptListToTemplateAndroid(localisationList, elementsTamplatesList);
        this.elementsTamplatesList = elementsTamplatesList;
        this.defoltLocale=defoltLocale;
        this.projectPath=projectPath;

    }
    public WriteAndroidLocales write() throws URISyntaxException, TransformerException, ParserConfigurationException, IOException {
        for(Map.Entry<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> entry:localisationList.entrySet()){
            com.rugovit.womuntu.util.FileUtils.writeAndroidStrings(getPathForLanguage(entry.getKey().getLocale()),entry.getValue());
        }
        return this;
    }
    private String getPathForLanguage(Locale locale) throws URISyntaxException {
        String folder;
        String separator = File.separator;
        if((locale.getLanguage()+locale.getCountry()).equals(defoltLocale.getLanguage()+defoltLocale.getCountry())){
            folder="values";
        }
        else if(locale.getCountry()!=null&&!locale.getCountry().isEmpty())  folder="values"+"-"+locale.getLanguage()+"-r"+locale.getCountry().toUpperCase();
        else  folder="values"+"-"+locale.getLanguage();
        if(projectPath==null)return  com.rugovit.womuntu.util.FileUtils.getCurrentDirctoryPath() +separator+"android"+separator+folder+separator+ "strings.xml";
        else return  projectPath+separator+folder+separator+ "strings.xml";
    }
}
