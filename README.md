# womuntu
Building iOs and Android string translation files from shared CSV file. Building CSV file from translation files. Building JSON from CSV file


Commands: 
       -automagic : Builds localisation files, if in current project directory from csv file found in current directory using  those file as template
       -m : Set the mode of the manager, with argumants: 
                                  wc - build csv from combined iOS and Android Strings files
                                  ws - build strings from cvs 
                                  wcc - build strings from csv assumes file names localisation.csv for original file to be filed and localisation_with_keys.csv 
                                  wjc - build json strings from csv in separate files in respect to language
                                  wjcs - build json strings from csv in single file 
  Example: m wjc
       -i : Set the name of ios strings file in containing folder, if omitted it will assume default  name: Localizable.strings, or Localizables folder  
       -a : Set the name of Android strings file in containing folder, if omitted it will assume default  name: strings.xml, or res folder
       -it : Set the name of ios file in containing folder used as template, if omitted it will assume default  name: Localizable.strings
       -at : Set the name of Android file in containing folder used as template, if omitted it will assume default  name: strings.xml
       -c : Name of the CVS file
