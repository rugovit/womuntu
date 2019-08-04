package com.rugovit.womuntu.reading_csv;
public enum ExpectedFormatOfCSV {
    ANDROID_COMMENT("ANDROID COMMENT"),IOS_COMMENT("IOS COMMENT"), ANDROID_KEY("ANDROID KEY"), IOS_KEY("IOS KEY");
     String description;
    ExpectedFormatOfCSV (String description) {
        this.description=description;
    }
    public String getDescription() {
        return description;
    }
    public static ExpectedFormatOfCSV getEnum(String value) {
        if(value == null)
            throw new IllegalArgumentException();
        for(ExpectedFormatOfCSV v : values())
            if(value.equalsIgnoreCase(v.getDescription())) return v;
        throw new IllegalArgumentException();
    }
    public static String getAllDescriptions(){
        String str="Commands: \n";
        for (ExpectedFormatOfCSV expectedFormatOfCSV:ExpectedFormatOfCSV.class.getEnumConstants()){
            str+="       -"+expectedFormatOfCSV.toString()+" : " +expectedFormatOfCSV.description+"\n";
        }
        return str;
    }
}
