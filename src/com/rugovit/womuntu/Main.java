package com.rugovit.womuntu;

import com.rugovit.womuntu.model.CsvRow;
import com.rugovit.womuntu.writing_csv.AddKeysToCSV;
import com.rugovit.womuntu.writing_csv.CompareTranslations;
import com.rugovit.womuntu.writing_csv.FileBuilderCSV;
import com.rugovit.womuntu.model.LocalisationElement;
import com.rugovit.womuntu.reading_csv.FileReaderCVS;
import com.rugovit.womuntu.writing_json.WriteJsonFile;
import com.rugovit.womuntu.writing_locales.WriteAndroidLocales;
import com.rugovit.womuntu.writing_locales.WriteIosLocales;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class Main {

    public static Locale DEFAULT_LANGUAGE =Locale.ENGLISH;
    public static void main(String[] args) {

        HashMap<String, String> commands = getCommands(args);
        if (commands != null && commands.size() > 0) {
            runProgram(commands);
        } else {
            System.out.println(Command.getAllDescriptions());
        }


    }

    private static void runProgram(HashMap<String, String> commands) {
        try {
            if(commands.containsKey(Command.automagic.toString())){
                runAutomagic();
            }
            else if (commands.containsKey(Command.m.toString())) {
                runModes(commands);
            } else {
                System.out.println("Error! Need to define mode with -m");
                System.out.println(Command.getAllDescriptions());
                return;
            }
        } catch (Exception e) {
            if (e.equals(FileNotFoundException.class)) {
                System.out.println("Error no strings file found!!! Make sure you have put the file in a same directory as this jar.");
            } else {
                System.out.println(e.toString());
            }
            e.printStackTrace();
        }
        System.out.println("\n\n\nFinished!\n\n\n");

    }
    private static void runAutomagic() throws Exception {
            File[] csvFiles= com.rugovit.womuntu.util.FileUtils.findCsvFilesInThisDirectory();
            if(csvFiles.length==1){
                com.rugovit.womuntu.util.ProjectType projectType= com.rugovit.womuntu.util.FileUtils.getProjectType();
                if(projectType!=null&&projectType.equals(com.rugovit.womuntu.util.ProjectType.ANDROID)){
                    automagicAndroid();
                }
                else if(projectType!=null&&projectType.equals(com.rugovit.womuntu.util.ProjectType.IOS)){
                    automagicIos();
                }
                else {
                    System.out.println("Error! Automagic should be called from project's root directory!");
                }
            }
            else if(csvFiles.length>1){
                System.out.println("Error! There is more then one csv file in this directory!");
                System.out.println("\n");
                for(File file:csvFiles){
                    System.out.println(file.getName()+"\n");
                }
            }
            else if(csvFiles.length==0){
                System.out.println("Error!  There is no csv files in this directory!");
            }

    }
    private static void runModes(HashMap<String, String> commands) throws Exception {
        if (commands.get(Command.m.toString()).equals("wc")) {
            String[]  androidResFolderList= com.rugovit.womuntu.util.FileUtils.getPathsOfAndroidResDirectories();
            String[]  iosResFolderList= com.rugovit.womuntu.util.FileUtils.getPathsOfIosResDirectories();
            if(androidResFolderList!=null&&androidResFolderList.length>0 && iosResFolderList!=null&&iosResFolderList.length>0){
                System.out.println("iOS folder read");
                System.out.println("Android folder read");
                TreeMap<String,ArrayList<LocalisationElement>> androidListAllLocalisations= com.rugovit.womuntu.util.FileUtils.readAndroidLocalisationFolder();
                TreeMap<String,ArrayList<LocalisationElement>> iosListAllLocalisations= com.rugovit.womuntu.util.FileUtils.readIosLocalisationFolder();
                CompareTranslations compareTranslations=new CompareTranslations(androidListAllLocalisations.get(DEFAULT_LANGUAGE.getDisplayLanguage()),iosListAllLocalisations.get(DEFAULT_LANGUAGE.getDisplayLanguage()),androidListAllLocalisations,iosListAllLocalisations);
                /*   String file="duplicate_compared.csv";
                if(FileUtils.checkIfFileExists(file)){
                    System.out.println("Reading duplicate_compared.csv" );
                    compareTranslations.addExsternalDuplicatesCompared(


                            (new FileReaderCVS(file)).readCVS().getLocalesLanguagesTranslationCsvBin());
                }*/
                FileBuilderCSV.buildAndWriteCsvFile((compareTranslations).build().getListForCsv());

            }
            else {
                ArrayList androidList = com.rugovit.womuntu.util.FileUtils.readAndroidLocales(commands.containsKey(Command.a.toString()) ? com.rugovit.womuntu.util.FileUtils.getCurrentDirctoryPath()+File.separator+commands.get(Command.a.toString()) : com.rugovit.womuntu.util.FileUtils.getCurrentDirctoryPath()+File.separator+ "strings.xml");
                System.out.println("Android file read");
                ArrayList<LocalisationElement> iosList = com.rugovit.womuntu.util.FileUtils.readIOSLocales(commands.containsKey(Command.i.toString()) ? com.rugovit.womuntu.util.FileUtils.getCurrentDirctoryPath()+File.separator+ commands.get(Command.i.toString()) : com.rugovit.womuntu.util.FileUtils.getCurrentDirctoryPath()+File.separator+ "Localizable.strings");
                System.out.println("iOS file read");
                 FileBuilderCSV.buildAndWriteCsvFile((new CompareTranslations(androidList, iosList)).build().getListForCsv());
            }
        } else if (commands.get(Command.m.toString()).equals("ws")) {
            TreeMap<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> localesLanguages= (new FileReaderCVS("translations.csv")).readCVS().getLocalesLanguages();
            Locale defoltLocale= DEFAULT_LANGUAGE ;
            (new WriteAndroidLocales(localesLanguages,defoltLocale)).write();
            (new WriteIosLocales(localesLanguages)).write();
        }
        else if (commands.get(Command.m.toString()).equals("wcc")) {
            TreeMap<Integer, CsvRow>  localisation=(new FileReaderCVS("localisation.csv")).readCVS().getLocalesCsvRow();
            TreeMap<Integer, CsvRow>  localisationWithKeys=(new FileReaderCVS("localisation_with_keys.csv")).readCVS().getLocalesCsvRow();
            localisationWithKeys=(new AddKeysToCSV()).addCsvData(localisation,localisationWithKeys).addKeysToLocalisationList();
            FileBuilderCSV.buildAndWriteCsvFile(localisationWithKeys);

        }
        else if(commands.get(Command.m.toString()).equals("wjc")){
            File[] csvFiles= com.rugovit.womuntu.util.FileUtils.findCsvFilesInThisDirectory();
            if(csvFiles.length==1){
                TreeMap<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> localesLanguages= (new FileReaderCVS(csvFiles[0].getName())).readCVS().getLocalesLanguages();
                WriteJsonFile.writeJsonobjectInSeperateFiles(localesLanguages,false);
            }
            else if(csvFiles.length>1){
                System.out.println("There is more then one csv file in this directory!");
                System.out.println("\n");
                for(File file:csvFiles){
                    System.out.println(file.getName()+"\n");
                }
                System.out.println("\n Combining all csv");
                TreeMap<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> localesLanguages=new TreeMap<>();
                for(File file:csvFiles){
                    System.out.println("-------------------------------\nAdding : "+file.getName()+" to list"+"\n");
                    TreeMap<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> listToAdd=  (new FileReaderCVS(file.getName())).readCVS().getLocalesLanguages();
                    for(Map.Entry<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> entry:listToAdd.entrySet()){
                        if(!localesLanguages.containsKey(entry.getKey()))localesLanguages.put(entry.getKey(),entry.getValue());
                        else{
                            ArrayList<LocalisationElement> tempList= localesLanguages.get(entry.getKey());
                            tempList.addAll(entry.getValue());
                            localesLanguages.put(entry.getKey(),tempList);
                        }
                    }
                }
                WriteJsonFile.writeJsonobjectInSeperateFiles(localesLanguages,true);

            }
            else if(csvFiles.length==0){
                System.out.println("Error!  There is no csv files in this directory!");

            }
        }
        else if(commands.get(Command.m.toString()).equals("wjcs")){
            File[] csvFiles= com.rugovit.womuntu.util.FileUtils.findCsvFilesInThisDirectory();
            if(csvFiles.length==1){
                TreeMap<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> localesLanguages= (new FileReaderCVS(csvFiles[0].getName())).readCVS().getLocalesLanguages();
                WriteJsonFile.writeJsonobjectInSingleFile(localesLanguages,true);
            }
            else if(csvFiles.length>1){
                System.out.println("There is more then one csv file in this directory!");
                System.out.println("\n");
                for(File file:csvFiles){
                    System.out.println(file.getName()+"\n");
                }
                System.out.println("\n Combining all csv:");
                TreeMap<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> localesLanguages=new TreeMap<>();
                for(File file:csvFiles){
                    System.out.println("-------------------------------\nAdding : "+file.getName()+" to list"+"\n");
                    TreeMap<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> listToAdd=  (new FileReaderCVS(file.getName())).readCVS().getLocalesLanguages();
                    for(Map.Entry<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> entry:listToAdd.entrySet()){
                        if(!localesLanguages.containsKey(entry.getKey()))localesLanguages.put(entry.getKey(),entry.getValue());
                        else{
                            ArrayList<LocalisationElement> tempList= localesLanguages.get(entry.getKey());
                            tempList.addAll(entry.getValue());
                            localesLanguages.put(entry.getKey(),tempList);
                        }
                    }
                }
                WriteJsonFile.writeJsonobjectInSingleFile(localesLanguages,true);

            }
            else if(csvFiles.length==0){
                System.out.println("Error!  There is no csv files in this directory!");

            }
        }

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void automagicAndroid() throws URISyntaxException, IOException, SAXException, ParserConfigurationException, TransformerException {
        File[] csvFiles= com.rugovit.womuntu.util.FileUtils.findCsvFilesInThisDirectory();
        TreeMap<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> localesLanguages= (new FileReaderCVS(csvFiles[0].getName())).readCVS().getLocalesLanguages();
        ArrayList<LocalisationElement> androidList = com.rugovit.womuntu.util.FileUtils.readAndroidLocales(com.rugovit.womuntu.util.FileUtils.getAndroidProjectLocalisationFolderPath()+File.separator+"values"+File.separator+ "strings.xml");
        Locale defoltLocale=DEFAULT_LANGUAGE;
        (new WriteAndroidLocales(localesLanguages,androidList,defoltLocale, com.rugovit.womuntu.util.FileUtils.getAndroidProjectLocalisationFolderPath())).write();
    }
    private static void automagicIos() throws Exception {
        File[] csvFiles= com.rugovit.womuntu.util.FileUtils.findCsvFilesInThisDirectory();
        TreeMap<com.rugovit.womuntu.util.LocaleWrapper,ArrayList<LocalisationElement>> localesLanguages= (new FileReaderCVS(csvFiles[0].getName())).readCVS().getLocalesLanguages();
        ArrayList<LocalisationElement> iosList = com.rugovit.womuntu.util.FileUtils.readIOSLocales(com.rugovit.womuntu.util.FileUtils.getIosProjectLocalisationFolderPath()+File.separator+"en.lproj"+File.separator+ "Localizable.strings");
        (new WriteIosLocales(localesLanguages,iosList, com.rugovit.womuntu.util.FileUtils.getIosProjectLocalisationFolderPath())).write();
    }
    private static HashMap<String, String> getCommands(String[] args) {
        HashMap<String, String> commandsMap = new HashMap<>();
        String lastCommand = null;
        for (String command : args) {
            if (lastCommand == null) {
                if (command.equals(Command.m.toString()) || command.equals(Command.automagic.toString()) && checkIfCommandExist(command)) {
                    commandsMap.put(command.replaceFirst("-", ""), "");
                } else {
                    System.out.println("Error! " + command + " Not a real command");
                    System.out.println(Command.getAllDescriptions());
                    return null;
                }
            } else if (lastCommand.equals(Command.m.toString()) || lastCommand.equals(Command.automagic.toString())) {
                    commandsMap.put(lastCommand.replaceFirst("-", ""), command);

            } else {
                if ( checkIfCommandExist(command)) {
                    System.out.println("Error! " + command + " Not a real command");
                    System.out.println(Command.getAllDescriptions());
                    return null;
                }
                else{
                    System.out.println("Error!  use: "+Command.m.toString() + " or " +Command.automagic.toString()+" to set program modes");
                }
            }
            lastCommand = command;
        }
        return commandsMap;
    }

    private static boolean checkIfCommandExist(String comand) {
        String str = comand.replaceFirst("-", "");
        for (Command command : Command.class.getEnumConstants()) {
            if (command.toString().equals(str)) return true;
        }
        return false;
    }


}
