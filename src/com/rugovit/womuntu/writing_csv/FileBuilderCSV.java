package com.rugovit.womuntu.writing_csv;

import com.rugovit.womuntu.model.CsvRow;
import com.rugovit.womuntu.model.TranslationCsvBin;
import com.rugovit.womuntu.reading_csv.ExpectedFormatOfCSV;
import com.rugovit.womuntu.util.FileUtils;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;

public class FileBuilderCSV {

    public static void buildAndWriteCsvFile(TreeMap<Integer, CsvRow> list) throws IOException, URISyntaxException {
        String path = FileUtils.getCurrentDirctoryPath() +File.separator+ "translations_keys_added.csv";
        File file=new File(path);
        Writer outputWriter=new FileWriter(path);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        String[] headers= list.get(list.firstKey()).getHeader();
        writer.writeHeaders(headers);
        for(int i=1;list.size()>i;i++){
            String[] row = new String[headers.length];
            for (int j=0;headers.length>j;j++){
                 String cell=list.get(i).getCell(headers[j].toLowerCase());
                 row[j] = cell;
            }
            writer.writeRow(row);
        }
        // we must close the writer. This also closes the java.io.Writer you used to create the CsvWriter instance
        // note no checked exceptions are thrown here. If anything bad happens you'll get an IllegalStateException wrapping the original error.
        writer.close();
        if(file.exists()){
            System.out.println("CSV file build!!!");
        }
        else  System.out.println("ERROR! CSV file failed to build!");
    }
    public static void buildAndWriteCsvFile(ArrayList<TranslationCsvBin> list) throws IOException, URISyntaxException {
        String path = FileUtils.getCurrentDirctoryPath()+File.separator + "translations_resolved.csv";
        File file=new File(path);
        File directory=new File(path.replace("translations_resolved.csv",""));
        directory.mkdirs();
        Writer outputWriter=new FileWriter(path);
        CsvWriter writer = new CsvWriter(outputWriter, new CsvWriterSettings());
        ArrayList<String> headers=new ArrayList<>();
        headers.add(ExpectedFormatOfCSV.ANDROID_COMMENT.getDescription());
        headers.add(ExpectedFormatOfCSV.IOS_COMMENT.getDescription());
        headers.add(ExpectedFormatOfCSV.ANDROID_KEY.getDescription());
        headers.add(ExpectedFormatOfCSV.IOS_KEY.getDescription());
        HashMap<String,String> languages=new HashMap<>();
        for( TranslationCsvBin entry:list){
            for(Map.Entry<String,String> lang:entry.getTranslations().entrySet()){
                if(!languages.containsKey(lang.getKey())){
                    languages.put(lang.getKey(),lang.getKey());
                }
            }

        }
        for (Map.Entry<String,String> lang:languages.entrySet()){
            headers.add(lang.getKey());
        }
        writer.writeHeaders(headers);

            for (int i = 0; i < list .size(); i++) {
                String[] row = new String[headers.size()];
                if (list.get(i).getCommentAndroid() != null && !list.get(i).getCommentAndroid().isEmpty()) {
                    row[0] = list.get(i).getCommentAndroid();
                    row[1] = "";
                    row[2] = "";
                    row[3] = "";
                    row[4] = "";
                } else {
                    row[0] = "";
                    row[1] = "";
                    row[2] = list.get(i).getElementIdAndroid();
                    row[3] = list.get(i).getElementIdIos();
                    int j=1;
                    for(Map.Entry<String,String> lang:languages.entrySet()){
                        String translation=list.get(i).getTranslations().get(lang.getKey());
                        if(translation!=null)row[3+j] = translation;
                        j++;
                    }

                }
                writer.writeRow(row);
            }


        // we must close the writer. This also closes the java.io.Writer you used to create the CsvWriter instance
        // note no checked exceptions are thrown here. If anything bad happens you'll get an IllegalStateException wrapping the original error.
        writer.close();
        if(file.exists()){
            System.out.println("CSV file build!!!");
        }
        else  System.out.println("ERROR! CSV file failed to build!");
    }
    /*
    public void buildAndWriteCsvFile() throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, URISyntaxException, IOException {
        String path = FileUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()+"translations_resolved.csv";
        File file = new java.io.File( path);
        HeaderColumnNameMappingStrategy<TranslationCsvBin> strategy2 =
                new HeaderColumnNameMappingStrategy<TranslationCsvBin>();
        strategy2.setType(TranslationCsvBin.class);
        PrintWriter writer = new PrintWriter(file);
            StatefulBeanToCsv<TranslationCsvBin> beanToCsv = new StatefulBeanToCsvBuilder(writer)
                    .withMappingStrategy(strategy2)
                    .build();
            beanToCsv.write(list);
            writer.close();
        System.out.println("CSV file build!!!");
    }*/
}
