package com.rugovit.womuntu.writing_locales;
import com.rugovit.womuntu.model.LocaleComment;
import com.rugovit.womuntu.model.LocalisationElement;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class WriteLocals {
    public static String LOCALE_COMMENT_SAPARATOR_EXTRA_FOUND_IN_CSV="EXTRA ELEMENTS FOUND IN CSV NEEDS MANUAL HANDLING";

    ///////////////////////////////////////////////////////////////////////////////////

    protected TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> adaptListToTemplateAndroid(TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationList, ArrayList<LocalisationElement> elementsTamplatesList) {
        TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationListAdaptedToTamplate = new TreeMap<>();

        TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationListTemp = new TreeMap<>(localisationList);
        for (Map.Entry<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> entry : localisationListTemp.entrySet()) {
            ArrayList<LocalisationElement> tempElementsList = entry.getValue();
            localisationListAdaptedToTamplate.put(entry.getKey(), adaptListAndroid(elementsTamplatesList, tempElementsList));
        }
        return localisationListAdaptedToTamplate;
    }
    protected TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> adaptListToTemplateIos(TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationList, ArrayList<LocalisationElement> elementsTamplatesList) {
        TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationListAdaptedToTamplate = new TreeMap<>();

        TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> localisationListTemp = new TreeMap<>(localisationList);
        for (Map.Entry<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<LocalisationElement>> entry : localisationListTemp.entrySet()) {
            ArrayList<LocalisationElement> tempElementsList = entry.getValue();
            localisationListAdaptedToTamplate.put(entry.getKey(), adaptListIos(elementsTamplatesList, tempElementsList));
        }
        return localisationListAdaptedToTamplate;
    }

    protected ArrayList<LocalisationElement> adaptListAndroid(ArrayList<LocalisationElement> tamplatesList, ArrayList<LocalisationElement> elementsList) {
        ArrayList<LocalisationElement> returnList = new ArrayList<>();
        for (LocalisationElement templateElement : tamplatesList) {
            boolean elementFound=false;
            for (LocalisationElement element : elementsList) {//searches trough csv file
                if (templateElement instanceof com.rugovit.womuntu.model.LocaleTranslation && element instanceof com.rugovit.womuntu.model.LocaleTranslation) {
                    if (((com.rugovit.womuntu.model.LocaleTranslation) templateElement).getElementIdAndroid().equals(((com.rugovit.womuntu.model.LocaleTranslation) element).getElementIdAndroid())) {// compares translations ids
                        returnList.add(element);
                        elementsList.remove(element);
                        elementFound=true;
                        break;
                    }
                }
            }
           if(!elementFound) returnList.add(templateElement);// if  it doesn't exist in csv file just add it
        }
        LocaleComment ele = new LocaleComment();
        returnList.add(ele);
        for ( LocalisationElement element : elementsList) {// adds un-paired elements
            if (element instanceof com.rugovit.womuntu.model.LocaleTranslation) {
                returnList.add(element);
            }
        }
        return returnList;
    }
    protected ArrayList<LocalisationElement> adaptListIos(ArrayList<LocalisationElement> tamplatesList, ArrayList<LocalisationElement> elementsList) {
        ArrayList<LocalisationElement> returnList = new ArrayList<>();
        for (LocalisationElement templateElement : tamplatesList) {
            boolean elementFound=false;
            for (LocalisationElement element : elementsList) {//searches trough csv file
                if (templateElement instanceof com.rugovit.womuntu.model.LocaleTranslation && element instanceof com.rugovit.womuntu.model.LocaleTranslation) {
                    if (((com.rugovit.womuntu.model.LocaleTranslation) templateElement).getElementIdIos().equals(((com.rugovit.womuntu.model.LocaleTranslation) element).getElementIdIos())) {// compares translations ids
                        returnList.add(element);
                        elementsList.remove(element);
                        elementFound=true;
                        break;
                    }
                }

            }
            if(!elementFound)returnList.add(templateElement);// if  it doesn't exist in csv file just add it
        }
        LocaleComment ele = new LocaleComment();
        ele.setCommentIos(LOCALE_COMMENT_SAPARATOR_EXTRA_FOUND_IN_CSV);
        for (LocalisationElement element : elementsList) {// adds un-paired elements
            if (element instanceof com.rugovit.womuntu.model.LocaleTranslation) {
                returnList.add(element);
            }
        }
        return returnList;
    }

}
