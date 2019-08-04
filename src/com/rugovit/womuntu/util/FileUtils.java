package com.rugovit.womuntu.util;

import com.rugovit.womuntu.model.LocalisationElement;
import com.sun.istack.NotNull;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import static com.rugovit.womuntu.Main.DEFAULT_LANGUAGE;

public final class FileUtils {
    public static TreeMap<String,ArrayList<com.rugovit.womuntu.model.LocalisationElement>> readAndroidLocalisationFolder() throws URISyntaxException, IOException, SAXException, ParserConfigurationException {
        String[] paths=  getPathsOfAndroidResDirectories();
        TreeMap<String,ArrayList<com.rugovit.womuntu.model.LocalisationElement>> returnList=new TreeMap<>();
        for(String path:paths){

            String[] splited= path.split(File.separator);
            String fodlerName=splited[splited.length-1];
            if(fodlerName.contains("values")) {
                if (fodlerName.equals("values")) {
                    returnList.put(DEFAULT_LANGUAGE.getDisplayLanguage(), readAndroidLocales(path + File.separator + "strings.xml"));
                } else {
                    String lang = "";
                    lang = fodlerName.replace("values-", "");
                    if (lang.contains("-r")) {
                        String[] langSplited = lang.split("-");
                        lang = langSplited[0] + "(" + langSplited[1] + ")";
                    }
                    returnList.put(lang, readAndroidLocales(path + File.separator + "strings.xml"));
                }
            }
        }
        return returnList;
    }
    public static TreeMap<String,ArrayList<com.rugovit.womuntu.model.LocalisationElement>> readIosLocalisationFolder() throws Exception {
        String[] paths=  getPathsOfIosResDirectories();
        TreeMap<String,ArrayList<com.rugovit.womuntu.model.LocalisationElement>> returnList=new TreeMap<>();
        for(String path:paths){
            String[] splited= path.split(File.separator);
            String fodlerName=splited[splited.length-1];
            if(fodlerName.contains(".lproj")) {
                if (fodlerName.equals("en.lproj")) {
                    returnList.put(DEFAULT_LANGUAGE.getDisplayLanguage(), readIOSLocales(path + File.separator + "Localizable.strings"));
                } else {
                    String lang = "";
                    lang = fodlerName.replace(".lproj", "");
                    if (lang.contains("-")) {
                        String[] langSplited = lang.split("-");
                        lang = langSplited[0] + "(" + langSplited[1] + ")";
                    }
                    returnList.put(lang, readIOSLocales(path + File.separator + "Localizable.strings"));
                }
            }
        }
        return returnList;
    }
    public static List<com.rugovit.womuntu.model.LocaleTranslation> readAndroidLocalesJAXB(@NotNull String fileName) throws Exception {
        List<com.rugovit.womuntu.model.LocaleTranslation> array;
        String path = getCurrentDirctoryPath() +File.separator + fileName;//"strings.xml";
        File file = new java.io.File(path);
        JAXBContext jaxbContext = JAXBContext.newInstance(com.rugovit.womuntu.model.LocaleTranslations.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        jaxbUnmarshaller.setListener(new Unmarshaller.Listener() {

            @Override
            public void beforeUnmarshal(Object target, Object parent) {
                super.beforeUnmarshal(target, parent);
            }
        });
        com.rugovit.womuntu.model.LocaleTranslations emps = (com.rugovit.womuntu.model.LocaleTranslations) jaxbUnmarshaller.unmarshal(file);
        array = emps.getTranslations();
        return array;
    }
    public static String getCurrentDirctoryPath() throws URISyntaxException {
        File file=new File(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        String returnString=file.getPath();
        if(returnString.endsWith(".jar")) returnString=returnString.replace(file.getName(),"");
        return returnString;    }
    public static ArrayList<com.rugovit.womuntu.model.LocalisationElement> readAndroidLocales(@NotNull String path) throws ParserConfigurationException, IOException, SAXException {
        ArrayList<com.rugovit.womuntu.model.LocalisationElement> array = new ArrayList<>();
        File fXmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getChildNodes().item(0).getChildNodes();
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node node = nList.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (((Element) node).getTagName().equals("string")) {
                    com.rugovit.womuntu.model.LocaleTranslation trnslation = new com.rugovit.womuntu.model.LocaleTranslation();
                    trnslation.setTranslation((node).getTextContent());
                    trnslation.setElementIdAndroid(((Element) node).getAttribute("name"));
                    array.add(trnslation);
                }
            } else if (node.getNodeType() == Element.COMMENT_NODE) {
                Comment comment = (Comment) node;
                com.rugovit.womuntu.model.LocaleComment localeComment = new com.rugovit.womuntu.model.LocaleComment();
                localeComment.setCommentAndoid(comment.getNodeValue());
                array.add(localeComment);

            }
        }
        return array;

    }

    public static boolean writeIosStrings(String filePath, ArrayList<com.rugovit.womuntu.model.LocalisationElement> listToWrite) {
        try {
            File directory = new File(filePath.replace("Localizable.strings", ""));
            if(!directory.exists()) directory.mkdirs();
            File delete= new File(filePath);
            if(delete.exists()) delete.delete();
            FileWriter writer = new FileWriter(filePath, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for (com.rugovit.womuntu.model.LocalisationElement element : listToWrite) {
                if (element instanceof com.rugovit.womuntu.model.LocaleTranslation) {
                    if (((com.rugovit.womuntu.model.LocaleTranslation) element).getElementIdIos() != null && !((com.rugovit.womuntu.model.LocaleTranslation) element).getElementIdIos().isEmpty()) {
                        bufferedWriter.write("\"" + ((com.rugovit.womuntu.model.LocaleTranslation) element).getElementIdIos() + "\"" + " = " + "\"" + ((com.rugovit.womuntu.model.LocaleTranslation) element).getTranslation() + "\";");
                        bufferedWriter.newLine();
                    }
                } else if (element instanceof com.rugovit.womuntu.model.LocaleComment) {
                    if (((com.rugovit.womuntu.model.LocaleComment) element).getCommentIos() != null) {
                        bufferedWriter.write("//" + ((com.rugovit.womuntu.model.LocaleComment) element).getCommentIos());
                        bufferedWriter.newLine();
                    }
                }
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public static boolean writeAndroidStrings(String filePath, ArrayList<com.rugovit.womuntu.model.LocalisationElement> listToWrite) throws TransformerException, ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("resources");
        doc.appendChild(rootElement);
        rootElement.appendChild(doc.createTextNode("\n"));

        // string element

        for (com.rugovit.womuntu.model.LocalisationElement element : listToWrite) {
            if (element instanceof com.rugovit.womuntu.model.LocaleTranslation) {
                if (((com.rugovit.womuntu.model.LocaleTranslation) element).getElementIdAndroid() != null && !((com.rugovit.womuntu.model.LocaleTranslation) element).getElementIdAndroid().isEmpty()) {
                    Element string = doc.createElement("string");
                    Attr attr = doc.createAttribute("name");
                    attr.setValue(((com.rugovit.womuntu.model.LocaleTranslation) element).getElementIdAndroid());
                    string.setAttributeNode(attr);
                    string.setTextContent(((com.rugovit.womuntu.model.LocaleTranslation) element).getTranslation());
                    rootElement.appendChild(string);
                    rootElement.appendChild(doc.createTextNode("\n"));
                }
            } else if (element instanceof com.rugovit.womuntu.model.LocaleComment) {
                if (((com.rugovit.womuntu.model.LocaleComment) element).getCommentAndoid() != null) {
                    Comment comment = doc.createComment(((com.rugovit.womuntu.model.LocaleComment) element).getCommentAndoid());
                    rootElement.appendChild(comment);
                    rootElement.appendChild(doc.createTextNode("\n"));
                }
            }
        }
        // write the content into xml file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        File file = new File(filePath);
        if(file.exists()) file.delete();
        File directory = new File(filePath.replace("strings.xml", ""));
        directory.mkdirs();
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
        return file.exists();
    }

    public static ArrayList<com.rugovit.womuntu.model.LocalisationElement> readIOSLocales(@NotNull  String path) throws Exception {
        String content = new Scanner(new File(path)).useDelimiter("\\Z").next();
        return parseIOSStrings(content);
    }

    private static ArrayList<com.rugovit.womuntu.model.LocalisationElement> parseIOSStrings(String content) {
        ArrayList<LocalisationElement> array = new ArrayList<>();
        content = content.replaceAll("/\\*([^*]|[\\r\\n])*\\*/", "");//removes multiline comments
        content=content.replaceAll("(\r?\n)+", "\n"); //removes multiple new lines with single new line
        String[] stringsArray = content.split("\n");// splits by key value pairs
        int i = 0;
        for (String pair : stringsArray) {
            pair = pair.replace("\n", "");
            if (pair.startsWith("//")) {// keeps  single lines comments
                pair = pair.replaceFirst("//", "");
                com.rugovit.womuntu.model.LocaleComment localeComment = new com.rugovit.womuntu.model.LocaleComment();
                localeComment.setCommentAndoid(pair);
                array.add(localeComment);
            } else {
                String[] pairArray = pair.split("\"=\"|\" =\"|\"= \"|\" = \"");// splits by " = " with space in diffrent configuaration
               if(pairArray.length==2) {
                   com.rugovit.womuntu.model.LocaleTranslation st = new com.rugovit.womuntu.model.LocaleTranslation();
                   st.setElementIdIos(pairArray[0].replaceAll("\"", "")); // removes new lines and  " ;
                   st.setTranslation(pairArray[1].replaceAll("\"", "").replaceAll(";", ""));
                   array.add(st);
               }
            }
            i++;
        }
        return array;
    }

    public static ProjectType getProjectType() throws URISyntaxException {
        ProjectType projectType = null;
        if(checkIfInAndroidProject()){
            projectType=ProjectType.ANDROID;
        }
        else if(checkIfInIosProject()){
            projectType=ProjectType.IOS;
        }
        return projectType;
    }

    public static boolean checkIfInIosProject() throws URISyntaxException {
        String path=getIosProjectLocalisationFolderPath();
        return (new File(path)).exists();
    }

    public static boolean checkIfInAndroidProject() throws URISyntaxException {
         String path=getAndroidProjectLocalisationFolderPath();
         return (new File(path)).exists();
    }
    public static boolean checkIfFileExists(String file) throws URISyntaxException {
        String sep = File.separator;
        file = getCurrentDirctoryPath() +sep+file;
        return (new File(file)).exists();
    }
    public static String getAndroidProjectLocalisationFolderPath() throws URISyntaxException {
        //TODO ind smarter way of getting pah for diffrent projcts
        String path = getCurrentDirctoryPath() ;
        String sep = File.separator;
        path+=sep+"app"+sep+"src"+sep+"main"+sep+"res";
        return path;
    }
    public static String getIosProjectLocalisationFolderPath() throws URISyntaxException {
        //TODO ind smarter way of getting pah for diffrent projcts
        String path = getCurrentDirctoryPath();
        String sep = File.separator;
        path+=sep+"UFO"+sep+"Resources"+sep+"Localizables";
        return path;
    }
    public static  File[] findCsvFilesInThisDirectory() throws URISyntaxException {
        String path = getCurrentDirctoryPath();
        return findCsvFilesInDirectory(path);

    }
    public static  File[] findCsvFilesInDirectory( String path){
        File dir = new File(path);
        return dir.listFiles((dir1, filename) -> filename.endsWith(".csv"));

    }
    public static String[] getPathsOfAndroidResDirectories() throws URISyntaxException {
        String path = getCurrentDirctoryPath();
        path+=File.separator+"res";

        return getPathsCOntaningDirectories(path);
    }
    public static String[] getPathsCOntaningDirectories(String path){
        File file = new File(path);
        String[] paths = file.list();
        if(paths!=null) {
            for (int i = 0; paths.length > i; i++) {
                paths[i] = path + File.separator + paths[i];
            }
        }
        return paths;
    }
    public static String[] getPathsOfIosResDirectories() throws URISyntaxException {
        String path = getCurrentDirctoryPath();
        path+=File.separator+"Localizables";

        return getPathsCOntaningDirectories(path);
    }



}
