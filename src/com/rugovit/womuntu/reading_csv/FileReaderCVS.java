package com.rugovit.womuntu.reading_csv;

import com.rugovit.womuntu.model.CsvRowTranslation;
import com.rugovit.womuntu.util.FileUtils;
import com.sun.istack.NotNull;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.util.*;

public class FileReaderCVS {
    private static final String ERROR_COULD_NOT_FIND_LANGUAGE = "Could not find the language!!";
    private static final String ERROR_COULD_NOT_FIND_COUNTRY = "Could not find the country!";
    String path;
    ArrayList<com.rugovit.womuntu.model.CsvRowTranslation> rows;
    TreeMap<String, Integer> headerIndex = null;
    TreeMap<com.rugovit.womuntu.util.LocaleWrapper, Integer> languageIndex = null;
    HashMap<String, String> countryLongShort = new HashMap<>();
    HashMap<String, String> languagesLongShort = new HashMap<>();
    HashMap<String, String> languagesShort3Letters = new HashMap<>();
    HashMap<String, String> languagesShort2Letters = new HashMap<>();
    HashMap<String, String> countryShort = new HashMap<>();
    ArrayList<com.rugovit.womuntu.model.TranslationCsvBin> localesLanguagesTranslationCsvBin;
    TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<com.rugovit.womuntu.model.LocalisationElement>> localesLanguages = new TreeMap<>();
    TreeMap<Integer, com.rugovit.womuntu.model.CsvRow> localesCsvRow ;
    ArrayList<String> allRows;
    public FileReaderCVS(@NotNull String fileName) throws URISyntaxException {
        this.path = FileUtils.getCurrentDirctoryPath() +File.separator+ fileName;
        Locale[] locals = DateFormat.getAvailableLocales();
        for (Locale loc : locals) {

            countryShort.put(loc.getCountry().toLowerCase(), loc.getCountry().toLowerCase());
            countryLongShort.put(loc.getDisplayCountry().toLowerCase(), loc.getCountry().toLowerCase());
            String longlanguag = loc.getDisplayLanguage(Locale.ENGLISH).toLowerCase();
            languagesLongShort.put(longlanguag, loc.getLanguage());
            Locale.getISOLanguages();
            languagesShort3Letters.put(loc.getISO3Language(), loc.getLanguage());
            languagesShort2Letters.put(loc.getLanguage(), loc.getLanguage());
        }
    }

    public FileReaderCVS readCVS() {

        InputStreamReader inputStreamReader = null;
        try {
            CsvParserSettings settings = new CsvParserSettings();
            settings.getFormat().setLineSeparator("\n");
            // creates a CSV parser
            CsvParser parser = new CsvParser(settings);

            // parses all rows in one go.
            InputStream inputStream = new FileInputStream(path);
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            List<String[]> allRows = parser.parseAll(inputStreamReader);
            /*for (String[] row : allRows) {
                System.out.println(Arrays.toString(row));
            }*/

            rows = extractRows(allRows);
            localesLanguages = extractLocalLanguages(rows);
            localesLanguagesTranslationCsvBin=extractTranslationCsvBinList();
            localesCsvRow=extractCsvRows(allRows);

        } catch (Exception ee) {
            ee.printStackTrace();
        } finally {
            try {
                if (inputStreamReader != null) inputStreamReader.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }

        return this;
    }

    public ArrayList<com.rugovit.womuntu.model.CsvRowTranslation> getRows() {
        return rows;
    }

    public TreeMap<Integer, com.rugovit.womuntu.model.CsvRow> getLocalesCsvRow() {
        return localesCsvRow;
    }
    public TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<com.rugovit.womuntu.model.LocalisationElement>> getLocalesLanguages() {
        return localesLanguages;
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private  TreeMap<Integer, com.rugovit.womuntu.model.CsvRow> extractCsvRows(List<String[]> allRows ){
        TreeMap<Integer, com.rugovit.womuntu.model.CsvRow> returnList=new TreeMap<>();
        String[] header=null;
        String[] headerUnchanged=null;
        int i=0;
        for (String[] row : allRows) {
            if (header == null) {
                header=row.clone();
                headerUnchanged=row.clone();
                for(int j=0; j<header.length;j++){
                    if(header[j]!=null) header[j]=header[j].toLowerCase();
                }
            } else{
                com.rugovit.womuntu.model.CsvRow csvRow=new com.rugovit.womuntu.model.CsvRow();
                for(int j=0; j<header.length;j++){
                    String headerName=header[j];
                    String value=row[j];
                    csvRow.putCell(headerName,value);
                }
                csvRow.setHeader(headerUnchanged);
                returnList.put(i,csvRow);
            }
            i++;
        }
        return  returnList;
    }
    private ArrayList<com.rugovit.womuntu.model.TranslationCsvBin> extractTranslationCsvBinList() {
        ArrayList<com.rugovit.womuntu.model.TranslationCsvBin> returnList = new ArrayList<>();
        for (com.rugovit.womuntu.model.LocalisationElement localisationElement : localesLanguages.get(new com.rugovit.womuntu.util.LocaleWrapper(new Locale("en")))) {
            if (localisationElement instanceof com.rugovit.womuntu.model.LocaleTranslation) {

                com.rugovit.womuntu.model.TranslationCsvBin translationCsvBin=new com.rugovit.womuntu.model.TranslationCsvBin(((com.rugovit.womuntu.model.LocaleTranslation) localisationElement).getElementIdAndroid(),((com.rugovit.womuntu.model.LocaleTranslation) localisationElement).getElementIdIos());

                for(Map.Entry<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<com.rugovit.womuntu.model.LocalisationElement>> element:localesLanguages.entrySet()){
                         for (com.rugovit.womuntu.model.LocalisationElement locElement:element.getValue()){
                             if(locElement instanceof com.rugovit.womuntu.model.LocaleTranslation){
                                 if((((com.rugovit.womuntu.model.LocaleTranslation)locElement).getElementIdAndroid()!=null&&((com.rugovit.womuntu.model.LocaleTranslation) localisationElement)!=null )&&
                                         ((com.rugovit.womuntu.model.LocaleTranslation)locElement).getElementIdAndroid().equals(((com.rugovit.womuntu.model.LocaleTranslation) localisationElement).getElementIdAndroid())){
                                     translationCsvBin.setTranslation(element.getKey().getLocale().getLanguage(),((com.rugovit.womuntu.model.LocaleTranslation) locElement).getTranslation());
                                 }
                             }
                         }
                }
                returnList.add(translationCsvBin);

            } else if (localisationElement instanceof com.rugovit.womuntu.model.LocaleComment) {
                com.rugovit.womuntu.model.TranslationCsvBin translationCsvBin=new com.rugovit.womuntu.model.TranslationCsvBin();
                translationCsvBin.setCommentAndroid(((com.rugovit.womuntu.model.LocaleComment)localisationElement).getCommentAndoid());
                translationCsvBin.setCommentAndroid(((com.rugovit.womuntu.model.LocaleComment) localisationElement).getCommentIos());
                returnList.add(translationCsvBin);
            }
        }
        return returnList;
    }

    private TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<com.rugovit.womuntu.model.LocalisationElement>> extractLocalLanguages(ArrayList<com.rugovit.womuntu.model.CsvRowTranslation> tempRows) {
        TreeMap<com.rugovit.womuntu.util.LocaleWrapper, ArrayList<com.rugovit.womuntu.model.LocalisationElement>> locElements = new TreeMap<>();
        int i = 0;
        for (Map.Entry<com.rugovit.womuntu.util.LocaleWrapper, Integer> entry : languageIndex.entrySet()) {
            Locale lang = entry.getKey().getLocale();
            ArrayList<com.rugovit.womuntu.model.LocalisationElement> list = new ArrayList<>();
            for (com.rugovit.womuntu.model.CsvRowTranslation row : rows) {
               if(row.getLocalisationElements().size()>i) list.add(row.getLocalisationElements().get(i));
            }
            locElements.put(new com.rugovit.womuntu.util.LocaleWrapper(lang), list);
            i++;
        }
        return locElements;
    }

    private ArrayList<com.rugovit.womuntu.model.CsvRowTranslation> extractRows(List<String[]> allRows) {
        ArrayList<com.rugovit.womuntu.model.CsvRowTranslation> rows = new ArrayList<>();
        for (String[] row : allRows) {
            if (headerIndex == null) {
                extractIndexes(row);
            } else{
                rows.addAll(extractRow(row));
            }
        }
        return rows;
    }



    private List<com.rugovit.womuntu.model.CsvRowTranslation> extractRow(String[] row) {
        List<com.rugovit.womuntu.model.CsvRowTranslation> csvRowTranslationList= new ArrayList<>();
        String androidKey = row[headerIndex.get(com.rugovit.womuntu.reading_csv.ExpectedFormatOfCSV.ANDROID_KEY.toString().replace("_", " "))];
        String iosKey = row[headerIndex.get(com.rugovit.womuntu.reading_csv.ExpectedFormatOfCSV.IOS_KEY.toString().replace("_", " "))];
        if(androidKey!=null)androidKey = androidKey.replace(" ","").replace("\n","");
        if(iosKey!=null)iosKey = iosKey.replace(" ","").replace("\n","");

        //allowing multiple keys in single key cell
        String [] androidKeys=null;
        String [] iosKeys=null;
        if(androidKey!=null)androidKeys=androidKey.split(",");
        else androidKeys=new String[0];
        if(iosKey!=null)iosKeys=iosKey.split(",");
        else iosKeys=new String[0];

        int multipleKeysCount=0;
        if(iosKeys.length>androidKeys.length) multipleKeysCount=iosKeys.length;
        else if(iosKeys.length<androidKeys.length)multipleKeysCount=androidKeys.length;
        else multipleKeysCount=iosKeys.length;
        for(int i=0; i<multipleKeysCount;i++) {

            com.rugovit.womuntu.model.CsvRowTranslation csvRowTranslation = new CsvRowTranslation();
            String androidComment=null;
            try {
                androidComment = row[headerIndex.get(com.rugovit.womuntu.reading_csv.ExpectedFormatOfCSV.ANDROID_COMMENT.toString().replace("_", " "))];
                csvRowTranslation.setAndroidComment(androidComment);
            }
            catch (NullPointerException e){
                //System.out.println("No Android Comment field");
            }
            String iosComment=null;
            try {
                iosComment = row[headerIndex.get(com.rugovit.womuntu.reading_csv.ExpectedFormatOfCSV.IOS_COMMENT.toString().replace("_", " "))];
                csvRowTranslation.setIosComment(iosComment);
            }
            catch (NullPointerException e){
                //System.out.println("No Ios Comment field");
            }
            csvRowTranslation.setAndroidKey(androidKey);
            csvRowTranslation.setIosKey(iosKey);
            ArrayList<com.rugovit.womuntu.model.LocalisationElement> localisationElements = new ArrayList<>();
            if ((iosComment != null && !iosComment.isEmpty()) || (androidComment != null && !androidComment.isEmpty())) {
                for (Map.Entry<com.rugovit.womuntu.util.LocaleWrapper, Integer> entry : languageIndex.entrySet()) {
                    com.rugovit.womuntu.model.LocaleComment localeComment = new com.rugovit.womuntu.model.LocaleComment();
                    localeComment.setCommentAndoid(androidComment);
                    localeComment.setCommentIos(iosComment);
                    localeComment.setLocale(entry.getKey().getLocale());
                    localisationElements.add(localeComment);
                }
            }
            if ((iosKey != null && !iosKey.isEmpty()) || (androidKey != null && !androidKey.isEmpty())) {
                for (Map.Entry<com.rugovit.womuntu.util.LocaleWrapper, Integer> entry : languageIndex.entrySet()) {
                    com.rugovit.womuntu.model.LocaleTranslation translation = new com.rugovit.womuntu.model.LocaleTranslation();
                    if(androidKeys.length>i)translation.setElementIdAndroid(androidKeys[i]);
                    if(iosKeys.length>i)translation.setElementIdIos(iosKeys[i]);
                    translation.setTranslation(row[entry.getValue()]);
                    translation.setLocale(entry.getKey().getLocale());
                    localisationElements.add(translation);
                }
            }
            csvRowTranslation.setLocalisationElements(localisationElements);
            csvRowTranslationList.add(csvRowTranslation);
        }
        return csvRowTranslationList;
    }

    private void extractIndexes(String[] row) {
        headerIndex = new TreeMap<>();
        languageIndex = new TreeMap<>();
        for (int i = 0; i < row.length; i++) {
            String header = row[i];
            com.rugovit.womuntu.reading_csv.ExpectedFormatOfCSV format = extractFormatElement(header);
            if (format != null) {
                headerIndex.put(format.toString().replace("_", " "), i);
            } else if(header!=null){
                com.rugovit.womuntu.util.LocaleWrapper local = extractLanguageFormat(header);
                if (local != null) {
                    languageIndex.put(local, i);
                }
            }
        }
    }

    private com.rugovit.womuntu.reading_csv.ExpectedFormatOfCSV extractFormatElement(String header) {

        for (com.rugovit.womuntu.reading_csv.ExpectedFormatOfCSV format : com.rugovit.womuntu.reading_csv.ExpectedFormatOfCSV.values()) {
            String formatString = format.toString().replace("_", " ");
            if (formatString.equals(header)) return format;
        }
        return null;
    }

    private String extractShortLanguage(String language) {
        language=language.replace(" ","");
        if (languagesLongShort.containsKey(language.toLowerCase())) {
            return languagesLongShort.get(language.toLowerCase());
        } else if (languagesShort3Letters.containsKey(language.toLowerCase())) {
            return languagesShort3Letters.get(language.toLowerCase());
        } else if (languagesShort2Letters.containsKey(language.toLowerCase())) {
            return languagesShort2Letters.get(language.toLowerCase());
        } else {
            System.out.println(ERROR_COULD_NOT_FIND_LANGUAGE + " " + language);
            return null;
        }
    }

    private String extractShortCountry(String country) {
        country=country.replace(" ","");
        if (countryLongShort.containsKey(country.toLowerCase())) {
            return countryLongShort.get(country.toLowerCase());
        } else if (countryShort.containsKey(country.toLowerCase())) {
            return countryShort.get(country.toLowerCase());
        } else {
            System.out.println(ERROR_COULD_NOT_FIND_COUNTRY + " " + country);
            return null;
        }
    }

    private com.rugovit.womuntu.util.LocaleWrapper extractLanguageFormat(String header)  {
        header = header.toLowerCase();
        String[] languageCode=header.split("-");
        String region = null;
        String shortRegion = null;
        if (languageCode.length>1) {
            region = languageCode[1];
            shortRegion = extractShortCountry(region);
        }
        Locale locale;
        String shortLang=extractShortLanguage(languageCode[0]);
        if (shortRegion != null) {
            if(shortLang==null) return null;
            locale = new Locale(shortLang, shortRegion);
        } else{
            if(shortLang==null) return null;
            locale = new Locale(shortLang);
        }
        String langugaeTag = locale.toLanguageTag();
        Locale localeFromLanguageTag = Locale.forLanguageTag(langugaeTag);
        return new com.rugovit.womuntu.util.LocaleWrapper(localeFromLanguageTag);

    }

    public ArrayList<com.rugovit.womuntu.model.TranslationCsvBin> getLocalesLanguagesTranslationCsvBin() {
        return localesLanguagesTranslationCsvBin;
    }
}
