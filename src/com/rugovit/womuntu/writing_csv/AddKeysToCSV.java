package com.rugovit.womuntu.writing_csv;

import com.rugovit.womuntu.model.CsvRow;
import com.rugovit.womuntu.reading_csv.ExpectedFormatOfCSV;
import com.rugovit.womuntu.Main;

import java.util.Map;
import java.util.TreeMap;

public class AddKeysToCSV  {
    TreeMap<Integer, CsvRow> localisation;
    TreeMap<Integer, CsvRow>  localisationWithKeys;

    public AddKeysToCSV() {
    }
    public AddKeysToCSV addCsvData(TreeMap<Integer, CsvRow> localisation, TreeMap<Integer, CsvRow>  localisationWithKeys){
        this.localisation=localisation;
        this.localisationWithKeys=localisationWithKeys;
        return this;
    }
    public TreeMap<Integer, CsvRow> addKeysToLocalisationList(){
        String androidKeyColumnName= ExpectedFormatOfCSV.ANDROID_KEY.getDescription();
        String iosKeyColumnName= ExpectedFormatOfCSV.IOS_KEY.getDescription();
        String englishColumnNameLong= Main.DEFAULT_LANGUAGE.getDisplayLanguage() ;
        String englishColumnNameShort= Main.DEFAULT_LANGUAGE .getLanguage();


        for(Map.Entry<Integer,CsvRow> entry: localisation.entrySet()){
            String  translation =entry.getValue().getCell(englishColumnNameLong.toLowerCase());
            if(translation ==null||translation .isEmpty()){
                translation=entry.getValue().getCell(englishColumnNameShort.toLowerCase());
            }
            for(Map.Entry<Integer,CsvRow> entryKey:localisationWithKeys.entrySet()){
                String androidKeyLocal2=entryKey.getValue().getCell(androidKeyColumnName.toLowerCase());
                String iosKeyLocal2=entryKey.getValue().getCell(iosKeyColumnName.toLowerCase());
                String  translation2=entryKey.getValue().getCell(englishColumnNameLong.toLowerCase());
                if(translation2==null||translation2.isEmpty()){// if could not find by long name
                    translation2=entry.getValue().getCell(englishColumnNameShort.toLowerCase());
                }
                if((androidKeyLocal2!=null&&!androidKeyLocal2.isEmpty())&&(iosKeyLocal2!=null&&!iosKeyLocal2.isEmpty())){
                    //only if  there is both keys in row
                    if(translation!=null&&translation2!=null&&translation.equals(translation2)){
                        entry.getValue().putCell(androidKeyColumnName.toLowerCase(),androidKeyLocal2 );
                        entry.getValue().putCell(iosKeyColumnName.toLowerCase(),iosKeyLocal2 );
                    }
                }

            }
        }
        return localisation;
    }

}
