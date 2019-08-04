package com.rugovit.womuntu;

public enum Command {
    automagic("Builds localisation files, if in current project directory from csv file found in current directory using  those file as template"),
    m("Set the mode of the manager, with argumants: " +
            "\n                                  wc - build csv from combined iOS and Android Strings files" +
            "\n                                  ws - build strings from cvs " +
            "\n                                  wcc - build strings from csv assumes file names localisation.csv for original file to be filed and localisation_with_keys.csv " +
            "\n                                  wjc - build json strings from csv in separate files in respect to language" +
            "\n                                  wjcs - build json strings from csv in single file" +
            " \n  Example: m wjc"),
    i("Set the name of ios strings file in containing folder, if omitted it will assume default  name: Localizable.strings, or Localizables folder  "),
    a("Set the name of Android strings file in containing folder, if omitted it will assume default  name: strings.xml, or res folder"),
    it("Set the name of ios file in containing folder used as template, if omitted it will assume default  name: Localizable.strings"),
    at("Set the name of Android file in containing folder used as template, if omitted it will assume default  name: strings.xml"),
    c("Name of the CVS file");
    String description;
    Command(String description) {
        this.description=description;
    }
    public String getDescription() {
        return description;
    }
    public static Command getEnum(String value) {
        if(value == null)
            throw new IllegalArgumentException();
        for(Command v : values())
            if(value.equalsIgnoreCase(v.getDescription())) return v;
        throw new IllegalArgumentException();
    }
    public static String getAllDescriptions(){
        String str="Commands: \n";
        for (Command command:Command.class.getEnumConstants()){
            str+="       -"+command.toString()+" : " +command.description+"\n";
        }
        return str;
    }
}
