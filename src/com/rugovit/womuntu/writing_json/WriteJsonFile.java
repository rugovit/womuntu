package com.rugovit.womuntu.writing_json;

import com.rugovit.womuntu.model.LocalisationElement;
import com.rugovit.womuntu.writing_json.model.TranslationJsonElement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rugovit.womuntu.util.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class WriteJsonFile {


    public static void writeJsonobjectInSeperateFiles(TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> list, boolean saparateAndroidAndIos) {
        for (Map.Entry<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> entry : list.entrySet()) {
            String langcode = entry.getKey().getLocale().getLanguage();
            if (entry.getKey().getLocale().getCountry() != null && !entry.getKey().getLocale().getCountry().isEmpty())
                langcode += "-" + entry.getKey().getLocale().getCountry().toLowerCase();
            writeJsonObject(langcode, convertToJasonArray(langcode, entry.getValue(),saparateAndroidAndIos));
        }
    }

    public static void writeJsonobjectInSingleFile(TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> list, boolean saparateAndroidAndIos) {
        ArrayList<TranslationJsonElement> allStrings = new ArrayList<>();
        for (Map.Entry<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> entry : list.entrySet()) {
            String langcode = entry.getKey().getLocale().getLanguage();
            if (entry.getKey().getLocale().getCountry() != null && !entry.getKey().getLocale().getCountry().isEmpty())
                langcode += "-" + entry.getKey().getLocale().getCountry().toLowerCase();
            allStrings.addAll(convertToJasonArray(langcode, entry.getValue(),saparateAndroidAndIos));
        }
        writeJsonObject("all_strings", allStrings);
    }

    private static ArrayList<TranslationJsonElement> convertToJasonArray(String langcode, ArrayList<LocalisationElement> list,boolean saparateAndroidAndIos) {
        ArrayList<TranslationJsonElement> jsonElements = new ArrayList<>();
        for (LocalisationElement element : list) {
            if (element instanceof com.rugovit.womuntu.model.LocaleTranslation) {
                if(!saparateAndroidAndIos) {
                    com.rugovit.womuntu.model.LocaleTranslation locale = (com.rugovit.womuntu.model.LocaleTranslation) element;
                    TranslationJsonElement translationJsonElement = new TranslationJsonElement();
                    translationJsonElement.setAndroidKey(locale.getElementIdAndroid());
                    translationJsonElement.setIosKey(locale.getElementIdIos());
                    translationJsonElement.setValue(locale.getTranslation());
                    translationJsonElement.setLangcode(langcode);
                    translationJsonElement.setClient("ffy");
                    translationJsonElement.setId(java.util.UUID.randomUUID().toString());
                    translationJsonElement.setCreated(System.currentTimeMillis());
                    translationJsonElement.setUpdated(System.currentTimeMillis());
                    jsonElements.add(translationJsonElement);
                }
                else{
                    com.rugovit.womuntu.model.LocaleTranslation locale = (com.rugovit.womuntu.model.LocaleTranslation) element;
                    if(locale.getElementIdIos()!=null&&!locale.getElementIdIos().isEmpty()) {
                        TranslationJsonElement translationJsonElement = new TranslationJsonElement();
                        translationJsonElement.setIosKey(locale.getElementIdIos());
                        translationJsonElement.setValue(locale.getTranslation());
                        translationJsonElement.setLangcode(langcode);
                        translationJsonElement.setClient("ffy");
                        translationJsonElement.setId(java.util.UUID.randomUUID().toString());
                        translationJsonElement.setCreated(System.currentTimeMillis());
                        translationJsonElement.setUpdated(System.currentTimeMillis());
                        jsonElements.add(translationJsonElement);
                    }
                    if(locale.getElementIdAndroid()!=null&&!locale.getElementIdAndroid().isEmpty()) {
                        TranslationJsonElement translationJsonElement = new TranslationJsonElement();
                        translationJsonElement.setAndroidKey(locale.getElementIdAndroid());
                        translationJsonElement.setValue(locale.getTranslation());
                        translationJsonElement.setLangcode(langcode);
                        translationJsonElement.setClient("ffy");
                        translationJsonElement.setId(java.util.UUID.randomUUID().toString());
                        translationJsonElement.setCreated(System.currentTimeMillis());
                        translationJsonElement.setUpdated(System.currentTimeMillis());
                        jsonElements.add(translationJsonElement);
                    }
                }
            }
        }
        return jsonElements;
    }

    public static boolean writeJsonObject(String prefix, ArrayList<TranslationJsonElement> array) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String strJson = gson.toJson(array);
        FileWriter writer = null;
        System.out.println("\nWriting " + prefix + "_localisation.json");
        try {
            writer = new FileWriter(FileUtils.getCurrentDirctoryPath() + File.separator + prefix + "_localisation.json");
            writer.write(strJson);
            System.out.println("\nSuccess!");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}

