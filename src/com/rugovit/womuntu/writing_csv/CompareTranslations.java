package com.rugovit.womuntu.writing_csv;

import com.rugovit.womuntu.model.LocaleComment;
import com.rugovit.womuntu.model.LocaleTranslation;
import com.rugovit.womuntu.model.LocalisationElement;
import com.rugovit.womuntu.model.TranslationCsvBin;

import java.util.*;

public class CompareTranslations {
    ArrayList<com.rugovit.womuntu.model.LocaleTranslation> androidList;
    ArrayList<com.rugovit.womuntu.model.LocaleTranslation> iosList;
    ArrayList<TranslationCsvBin> externalDuplicatesCompared;
    ArrayList<com.rugovit.womuntu.util.Pair<LocaleTranslation, LocaleTranslation>> translationPairsCompared = new ArrayList<>();
    ArrayList<com.rugovit.womuntu.model.LocaleTranslation> translationsExtraForAndroidCompared = new ArrayList<>();
    ArrayList<com.rugovit.womuntu.model.LocaleTranslation> translationsExtraForIosCompared = new ArrayList<>();

    ArrayList<com.rugovit.womuntu.util.Pair<ArrayList<LocaleTranslation>, ArrayList<LocaleTranslation>>> translationPairsDuplicatesCompared = new ArrayList<>();
    ArrayList<ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> translationDuplicateAndroidExtraCompared = new ArrayList<>();
    ArrayList<ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> translationDuplicateIosExtraCompared = new ArrayList<>();

    com.rugovit.womuntu.util.Pair<HashMap<String, LocaleTranslation>, HashMap<String, ArrayList<LocaleTranslation>>> androidDuplicatesResolutionPair;
    com.rugovit.womuntu.util.Pair<HashMap<String, LocaleTranslation>, HashMap<String, ArrayList<LocaleTranslation>>> iosDuplicatesResolutionPair;
    TreeMap<String, ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> androidListAllLocalisations = new TreeMap<>();
    TreeMap<String, ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> iosListAllLocalisations = new TreeMap<>();

    ArrayList<TranslationCsvBin> listForCsv;

    public CompareTranslations(ArrayList<LocalisationElement> androidList, ArrayList<LocalisationElement> iosList) {
        this.iosList = removeComments(iosList);
        this.androidList = removeComments(androidList);
    }

    public CompareTranslations(ArrayList<LocalisationElement> defoltAndroidList, ArrayList<LocalisationElement> defoltIosList, TreeMap<String, ArrayList<LocalisationElement>> androidListAllLocalisations, TreeMap<String, ArrayList<LocalisationElement>> iosListAllLocalisations) {
        for (Map.Entry<String, ArrayList<LocalisationElement>> element : iosListAllLocalisations.entrySet()) {
            this.iosListAllLocalisations.put(element.getKey(), removeComments(element.getValue()));
        }
        for (Map.Entry<String, ArrayList<LocalisationElement>> element : androidListAllLocalisations.entrySet()) {
            this.androidListAllLocalisations.put(element.getKey(), removeComments(element.getValue()));
        }
        this.iosList = removeComments(defoltIosList);
        this.androidList = removeComments(defoltAndroidList);
    }

    public CompareTranslations addExsternalDuplicatesCompared(ArrayList<TranslationCsvBin> externalDuplicatesCompared) {
        this.externalDuplicatesCompared = externalDuplicatesCompared;
        return this;
    }

    public CompareTranslations build() {
        androidDuplicatesResolutionPair = resolveDoubles(androidList);
        if (androidDuplicatesResolutionPair.getElement1().size() > 0) System.out.println("Found android duplicates!");
        iosDuplicatesResolutionPair = resolveDoubles(iosList);
        if (iosDuplicatesResolutionPair.getElement1().size() > 0) System.out.println("Found ios duplicates!");
        findTranlationPairs();
        sortDuplicatesFromExternnalDuplicatsSortedList();
        buildListForCsv();
        checkIfItIsAllThere();
        return this;
    }

    ///////////////////////////////////////////BUILDING METHODS//////////////////////////////////////////////////////////////////////////////////////////////////////
    private com.rugovit.womuntu.model.LocaleTranslation findAndRemoveLocaleTranslationByID(String idAndroid, String idIos, ArrayList<com.rugovit.womuntu.model.LocaleTranslation> list) {
        for (Iterator<com.rugovit.womuntu.model.LocaleTranslation> it = list.iterator(); it.hasNext(); ) {
            com.rugovit.womuntu.model.LocaleTranslation element = it.next();
            if (element.getElementIdAndroid()!=null&&idAndroid.equals(element.getElementIdAndroid())) {
                it.remove();
                return element;
            }
            if (element.getElementIdIos()!=null&&idIos.equals(element.getElementIdAndroid())) {
                it.remove();
                return element;
            }
        }
        return null;
    }

    private com.rugovit.womuntu.model.LocaleTranslation findLocaleTranslationByID(String idAndroid, String idIos, ArrayList<com.rugovit.womuntu.model.LocaleTranslation> list) {
        for (Iterator<com.rugovit.womuntu.model.LocaleTranslation> it = list.iterator(); it.hasNext(); ) {
            com.rugovit.womuntu.model.LocaleTranslation element = it.next();
            if (element.getElementIdAndroid()!=null&&idAndroid.equals(element.getElementIdAndroid())) {
                return element;
            }
            if (element.getElementIdIos()!=null&&idIos.equals(element.getElementIdIos())) {
                return element;
            }
        }
        return null;
    }

    private void sortDuplicatesFromExternnalDuplicatsSortedList() {
        if (externalDuplicatesCompared != null && externalDuplicatesCompared.size() > 0) {
            System.out.println("Applying duplicated sorted list...");

            for(Iterator<TranslationCsvBin > it = externalDuplicatesCompared.iterator(); it.hasNext(); ) {
                TranslationCsvBin  translationCsvBin = it.next();
                if (findLocaleTranslationByID(translationCsvBin.getElementIdAndroid(), null, androidDuplicatesResolutionPair.getElement1().get(translationCsvBin.getTranslations().get(translationCsvBin.getTranslations().firstKey()))) != null
                        && findLocaleTranslationByID(null, translationCsvBin.getElementIdIos(), iosDuplicatesResolutionPair.getElement1().get(translationCsvBin.getTranslations().get(translationCsvBin.getTranslations().firstKey()))) != null) {

                    com.rugovit.womuntu.model.LocaleTranslation localeTranslationAndroid=findAndRemoveLocaleTranslationByID(translationCsvBin.getElementIdAndroid(), null, androidDuplicatesResolutionPair.getElement1().get(translationCsvBin.getTranslations().get(translationCsvBin.getTranslations().firstKey())));
                    androidList.add(localeTranslationAndroid);
                    if(androidDuplicatesResolutionPair.getElement1().get(translationCsvBin.getTranslations().get(translationCsvBin.getTranslations().firstKey())).size()==0){
                        androidDuplicatesResolutionPair.getElement1().get(translationCsvBin.getTranslations().get(translationCsvBin.getTranslations().firstKey())).remove(translationCsvBin.getElementIdAndroid());
                    }
                    com.rugovit.womuntu.model.LocaleTranslation localeTranslationIos= findAndRemoveLocaleTranslationByID(null, translationCsvBin.getElementIdIos(), iosDuplicatesResolutionPair.getElement1().get(translationCsvBin.getTranslations().get(translationCsvBin.getTranslations().firstKey())));
                    iosList.add(localeTranslationIos);
                    if(iosDuplicatesResolutionPair.getElement1().get(translationCsvBin.getTranslations().get(translationCsvBin.getTranslations().firstKey())).size()==0){
                        iosDuplicatesResolutionPair.getElement1().get(translationCsvBin.getTranslations().get(translationCsvBin.getTranslations().firstKey())).remove(translationCsvBin.getElementIdAndroid());
                    }


                }
            }
        }
    }

    private void buildListForCsv() {
        listForCsv = new ArrayList<TranslationCsvBin>();

        ////FOUNDED MATCHES
        TranslationCsvBin comment= new TranslationCsvBin();
        comment.setCommentAndroid("FOUND MATCHES");
        listForCsv.add(comment);
        for (com.rugovit.womuntu.util.Pair<LocaleTranslation, LocaleTranslation> element : translationPairsCompared) {
            listForCsv.add(new TranslationCsvBin(element.getElement0().getElementIdAndroid(), element.getElement1().getElementIdIos(), element.getElement0().getTranslation()));
        }
        ///MULTIPLE MATCHES
        TranslationCsvBin comment2= new TranslationCsvBin();
        comment2.setCommentAndroid("MULTIPLE MATCHES");
        listForCsv.add(comment2);
        for (com.rugovit.womuntu.util.Pair<ArrayList<LocaleTranslation>, ArrayList<LocaleTranslation>> pair : translationPairsDuplicatesCompared) {
            ArrayList<com.rugovit.womuntu.model.LocaleTranslation> androidList = pair.getElement0();
            ArrayList<com.rugovit.womuntu.model.LocaleTranslation> iosList = pair.getElement1();
            TranslationCsvBin comment3= new TranslationCsvBin();
            comment3.setCommentAndroid("multiple match for: " + androidList.get(0).getTranslation());
            listForCsv.add(comment3);
            for (com.rugovit.womuntu.model.LocaleTranslation androidTranslation : androidList) {
                listForCsv.add(new TranslationCsvBin(androidTranslation.getElementIdAndroid(), null, androidTranslation.getTranslation()));
            }
            for (com.rugovit.womuntu.model.LocaleTranslation iosTranslation : iosList) {
                listForCsv.add(new TranslationCsvBin(null, iosTranslation.getElementIdIos(), iosTranslation.getTranslation()));
            }

        }
        ///MULTIPLE NON MATCHED
        TranslationCsvBin comment3= new TranslationCsvBin();
        comment3.setCommentAndroid("MULTIPLE NON MATCHED ANDROID");
        listForCsv.add(comment3);
        for (ArrayList<com.rugovit.womuntu.model.LocaleTranslation> list : translationDuplicateAndroidExtraCompared) {
            TranslationCsvBin comment4= new TranslationCsvBin();
            comment4.setCommentAndroid("android multiples of: " + list.get(0).getTranslation());
            listForCsv.add(comment4);
            for (com.rugovit.womuntu.model.LocaleTranslation element : list) {
                listForCsv.add(new TranslationCsvBin(element.getElementIdAndroid(), null, element.getTranslation()));
            }
        }
        TranslationCsvBin comment4= new TranslationCsvBin();
        comment4.setCommentAndroid("MULTIPLE NON MATCHED IOS");
        listForCsv.add(comment4);
        for (ArrayList<com.rugovit.womuntu.model.LocaleTranslation> list : translationDuplicateIosExtraCompared) {
            TranslationCsvBin comment5= new TranslationCsvBin();
            comment5.setCommentAndroid("ios multiples of: " + list.get(0).getTranslation());
            listForCsv.add(comment5);
            for (com.rugovit.womuntu.model.LocaleTranslation element : list) {
                listForCsv.add(new TranslationCsvBin(null, element.getElementIdIos(), element.getTranslation()));
            }
        }
        ///SINGLES NON MATCHED ANDROID
        TranslationCsvBin comment6= new TranslationCsvBin();
        comment6.setCommentAndroid("SINGLES NON MATCHED ANDROID");
        listForCsv.add(comment6);
        for (com.rugovit.womuntu.model.LocaleTranslation element : translationsExtraForAndroidCompared) {
            listForCsv.add(new TranslationCsvBin(element.getElementIdAndroid(), null, element.getTranslation()));
        }
        ///SINGLES NON MATCHED IOS
        TranslationCsvBin comment7= new TranslationCsvBin();
        comment7.setCommentAndroid("SINGLES NON MATCHED IOS");
        listForCsv.add(comment7);
        for (com.rugovit.womuntu.model.LocaleTranslation element : translationsExtraForIosCompared) {
            listForCsv.add(new TranslationCsvBin(null, element.getElementIdIos(), element.getTranslation()));
        }
        System.out.println("Finsinhed building list for csv!");

        ///ADD OTHER TRANSLATIONS
        addOtherTranslationsFromTheList();


    }

    private void addOtherTranslationsFromTheList() {
        if (androidListAllLocalisations.size() > 0 && iosListAllLocalisations.size() > 0) {//adds  additional languages  , it will  overwrite ios translations with  android if it finds  the sam keys
            for (Map.Entry<String, ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> element : iosListAllLocalisations.entrySet()) {
                putTranslationCsvBinListComparingWithTamplate(element.getKey(), element.getValue());
            }
            for (Map.Entry<String, ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> element : androidListAllLocalisations.entrySet()) {
                putTranslationCsvBinListComparingWithTamplate(element.getKey(), element.getValue());
            }
        }
    }

    private void putTranslationCsvBinListComparingWithTamplate(String language, ArrayList<com.rugovit.womuntu.model.LocaleTranslation> elementList) {
        //finding matches
        for (TranslationCsvBin templateElement : listForCsv) {
            for (com.rugovit.womuntu.model.LocaleTranslation element : elementList) {
                boolean foundAndroid = false;
                boolean foundIos = false;
                if (element.getElementIdIos() != null && templateElement.getElementIdIos() != null)
                    foundIos = element.getElementIdIos().equals(templateElement.getElementIdIos());
                if (element.getElementIdAndroid() != null && templateElement.getElementIdAndroid() != null)
                    foundAndroid = element.getElementIdAndroid().equals(templateElement.getElementIdAndroid());
                if (foundAndroid || foundIos) {
                    templateElement.setTranslation(language, element.getTranslation());
                    elementList.remove(element);
                    break;
                }
            }
        }
        //adding extra translations found  in  list
        for (com.rugovit.womuntu.model.LocaleTranslation element : elementList) {
            TranslationCsvBin translationCsvBin = new TranslationCsvBin(element.getElementIdAndroid(), element.getElementIdIos());
            translationCsvBin.setTranslation(language, element.getTranslation());
            listForCsv.add(translationCsvBin);

        }

    }

    private com.rugovit.womuntu.util.Pair<HashMap<String, LocaleTranslation>, HashMap<String, ArrayList<LocaleTranslation>>> resolveDoubles(List<com.rugovit.womuntu.model.LocaleTranslation> list) {

        HashMap<String, com.rugovit.womuntu.model.LocaleTranslation> noDuplicatesHashMap = new HashMap<>();
        HashMap<String, ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> duplicatesHashMap = new HashMap<>();
        for (com.rugovit.womuntu.model.LocaleTranslation element : list) {
            if (!noDuplicatesHashMap.containsKey(element.getTranslation())) {// no duplicates
                noDuplicatesHashMap.put(element.getTranslation(), element); // addin element
            } else {// resolve duplicates
                if (noDuplicatesHashMap.get(element.getTranslation()) != null) {  // if it is first duplicate
                    com.rugovit.womuntu.model.LocaleTranslation duplicateElement = noDuplicatesHashMap.get(element.getTranslation());
                    duplicatesHashMap.put(duplicateElement.getTranslation(), new ArrayList<>());
                    duplicatesHashMap.get(duplicateElement.getTranslation()).add(duplicateElement);// adding element allredy in noduplicates list
                    duplicatesHashMap.get(duplicateElement.getTranslation()).add(element);// adding duplicate element
                    noDuplicatesHashMap.put(element.getTranslation(), null);//setting  element as null for

                } else {  // more then one duplicate
                    duplicatesHashMap.get(element.getTranslation()).add(element);
                }
            }
        }
        Iterator it = duplicatesHashMap.entrySet().iterator();
        while (it.hasNext()) {// removes empty duplicates from the list
            Map.Entry pair = (Map.Entry) it.next();
            noDuplicatesHashMap.remove(pair.getKey());
        }
        return new com.rugovit.womuntu.util.Pair<>(noDuplicatesHashMap, duplicatesHashMap);
    }

    private void findTranlationPairs() {

        /////////////////////non duplicates
        HashMap<String, com.rugovit.womuntu.model.LocaleTranslation> androidNoDoublesList = new HashMap<>(androidDuplicatesResolutionPair.getElement0());
        HashMap<String, com.rugovit.womuntu.model.LocaleTranslation> iosNoDoublesList = new HashMap<>(iosDuplicatesResolutionPair.getElement0());


        Iterator it = androidNoDoublesList.entrySet().iterator();
        while (it.hasNext()) {// searches for   translation pairs and removes the  found pair from original list
            Map.Entry pair = (Map.Entry) it.next();
            if (iosNoDoublesList.containsKey(pair.getKey())) {
                translationPairsCompared.add(new com.rugovit.womuntu.util.Pair(pair.getValue(), iosNoDoublesList.get(pair.getKey())));
                it.remove();
                iosNoDoublesList.remove(pair.getKey());
            }
        }
        it = androidNoDoublesList.entrySet().iterator();
        while (it.hasNext()) {// searches for   translation pairs and removes the  found pair from original list this time with string proccesing
            Map.Entry pair = (Map.Entry) it.next();
            Iterator it2 = iosNoDoublesList.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry pair2 = (Map.Entry) it2.next();
                if (fullStringProccesing((String) pair2.getKey()).equals(fullStringProccesing((String) pair.getKey()))) {
                    translationPairsCompared.add(new com.rugovit.womuntu.util.Pair(pair.getValue(), iosNoDoublesList.get(pair2.getKey())));
                    it.remove();
                    it2.remove();
                    break;
                }
            }
        }
        translationsExtraForAndroidCompared.addAll(androidNoDoublesList.values());
        translationsExtraForIosCompared.addAll(iosNoDoublesList.values());

        ///////////////////duplicates
        HashMap<String, ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> androidDoublesList = new HashMap<>(androidDuplicatesResolutionPair.getElement1());
        HashMap<String, ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> iosDoublesList = new HashMap<>(iosDuplicatesResolutionPair.getElement1());
        it = androidDoublesList.entrySet().iterator();
        while (it.hasNext()) {///searches for pairs in nonduplicate android list comparing with duplicate isos list
            Map.Entry pair = (Map.Entry) it.next();
            if (iosDoublesList.containsKey(pair.getKey())) {
                translationPairsDuplicatesCompared.add(new com.rugovit.womuntu.util.Pair(pair.getValue(), iosDoublesList.get(pair.getKey())));
                it.remove();
                iosDoublesList.remove(pair.getKey());
            }
        }
        it = androidDoublesList.entrySet().iterator();
        while (it.hasNext()) {// searches for   translation pairs and removes the  found pair from original list this time with string proccesing
            Map.Entry pair = (Map.Entry) it.next();
            Iterator it2 = iosDoublesList.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry pair2 = (Map.Entry) it2.next();
                if (fullStringProccesing((String) pair2.getKey()).equals(fullStringProccesing((String) pair.getKey()))) {
                    translationPairsDuplicatesCompared.add(new com.rugovit.womuntu.util.Pair(pair.getValue(), iosDoublesList.get(pair2.getKey())));
                    it.remove();
                    it2.remove();
                    break;
                }
            }
        }
        translationDuplicateAndroidExtraCompared.addAll(androidDoublesList.values());
        translationDuplicateIosExtraCompared.addAll(iosDoublesList.values());
        System.out.println("Finsinhed comparing!");
    }

    private String fullStringProccesing(String string) {
        return removeSpecialCharacters(removeHtmlElements(string));
    }

    private String removeHtmlElements(String string) {
        return string.replaceAll("\\<.*?>", "").replaceAll("&.*?;", "");
    }

    private String removeSpecialCharacters(String string) {
        return string.toLowerCase().replace(" ", "").replace("\"", "").replace("%", "").replace("$", "").replace(".", "").replace("&", "").replace("#", "")
                .replace("™", "").replace("\n", "").replace(",", "").replace(";", "").replace(":", "").replace("?", "").replace("!", "").replace("'", "")
                .replace("*", "").replace("@", "".replace("’", "").replace("'", ""));
    }

    private void checkIfItIsAllThere() {
        System.out.println("Checking for errors...");
        int originalSize = iosList.size() + androidList.size();
        int poductSize = 0;
        poductSize += 2 * translationPairsCompared.size();
        poductSize += translationsExtraForAndroidCompared.size();
        poductSize += translationsExtraForIosCompared.size();

        for (ArrayList<com.rugovit.womuntu.model.LocaleTranslation> list : translationDuplicateAndroidExtraCompared) {

            for (com.rugovit.womuntu.model.LocaleTranslation element : list) {
                poductSize++;
            }
        }

        for (ArrayList<com.rugovit.womuntu.model.LocaleTranslation> list : translationDuplicateIosExtraCompared) {

            for (com.rugovit.womuntu.model.LocaleTranslation element : list) {
                poductSize++;
            }
        }
        for (com.rugovit.womuntu.util.Pair<ArrayList<LocaleTranslation>, ArrayList<LocaleTranslation>> pair : translationPairsDuplicatesCompared) {
            ArrayList<com.rugovit.womuntu.model.LocaleTranslation> androidList = pair.getElement0();
            ArrayList<com.rugovit.womuntu.model.LocaleTranslation> iosList = pair.getElement1();
            for (com.rugovit.womuntu.model.LocaleTranslation androidTranslation : androidList) {
                poductSize++;
            }
            for (com.rugovit.womuntu.model.LocaleTranslation iosTranslation : iosList) {
                poductSize++;
            }

        }
        if (originalSize == poductSize) {
            System.out.println("All good!");
        } else {
            System.out.println("Final list size difference ! ");
        }

    }

    private ArrayList<com.rugovit.womuntu.model.LocaleTranslation> removeComments(ArrayList<LocalisationElement> list) {
        ArrayList<com.rugovit.womuntu.model.LocaleTranslation> returnList = new ArrayList<>();
        Iterator<LocalisationElement> iterator = list.iterator();
        while (iterator.hasNext()) {
            LocalisationElement localeElement = iterator.next();
            if (localeElement instanceof LocaleComment) {
                iterator.remove();
            } else if (localeElement instanceof com.rugovit.womuntu.model.LocaleTranslation) {
                returnList.add((com.rugovit.womuntu.model.LocaleTranslation) localeElement);
            }

        }
        return returnList;
    }

    ////////////////////////////////////////////////GETTERS//////////////////////////////////////////////////////////////////////////////////////////////////////////
    public com.rugovit.womuntu.util.Pair<HashMap<String, LocaleTranslation>, HashMap<String, ArrayList<LocaleTranslation>>> getAndroidDuplicatesResolutionPair() {
        return androidDuplicatesResolutionPair;
    }

    public com.rugovit.womuntu.util.Pair<HashMap<String, LocaleTranslation>, HashMap<String, ArrayList<LocaleTranslation>>> getIosDuplicatesResolutionPair() {
        return iosDuplicatesResolutionPair;
    }

    public ArrayList<com.rugovit.womuntu.util.Pair<LocaleTranslation, LocaleTranslation>> getTranslationPairsCompared() {
        return translationPairsCompared;
    }

    public ArrayList<com.rugovit.womuntu.model.LocaleTranslation> getTranslationsExtraForAndroidCompared() {
        return translationsExtraForAndroidCompared;
    }

    public ArrayList<com.rugovit.womuntu.model.LocaleTranslation> getTranslationsExtraForIosCompared() {
        return translationsExtraForIosCompared;
    }

    public ArrayList<com.rugovit.womuntu.util.Pair<ArrayList<LocaleTranslation>, ArrayList<LocaleTranslation>>> getTranslationPairsDuplicatesCompared() {
        return translationPairsDuplicatesCompared;
    }

    public ArrayList<ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> getTranslationDuplicateAndroidExtraCompared() {
        return translationDuplicateAndroidExtraCompared;
    }

    public ArrayList<ArrayList<com.rugovit.womuntu.model.LocaleTranslation>> getTranslationDuplicateIosExtraCompared() {
        return translationDuplicateIosExtraCompared;
    }

    public ArrayList<TranslationCsvBin> getListForCsv() {
        return listForCsv;
    }

}
