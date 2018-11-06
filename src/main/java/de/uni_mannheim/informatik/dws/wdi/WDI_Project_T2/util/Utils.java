package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

public class Utils {

    private static String[][] UMLAUT_REPLACEMENTS = {
    { "ä", "ae" }, { "ü", "ue" }, { "ö", "oe" }, { "ß", "ss" }
    };

    /**
     * List of non-description words.
     * TODO: Should be extended manually
     */

    //blacklisting approach
    private static String[] nonDescriptionWords = {
    "januar", "februar", "maerz", "april", "mai", "juni", "juli", "august", "september", "oktober",
    "november", "dezember",
    "tuev", "noch", "monate", "gebraucht", "tausch",
    "andere", "whatsapp", "sucht",
    "+",  "\"", "!", "/"
    };

    private static String removeNonDescriptionWords(String text) {
        for (int i = 0; i < nonDescriptionWords.length; i++) {
            if(text.contains(nonDescriptionWords[i])){
                text= text.replace(nonDescriptionWords[i], "");
            }
        }
        return text;
    }

    //whitelisting approach
    public static String keepDescriptionWords(String carDescriptionText) {

        String[] carDescriptionTextArray = carDescriptionText.split("\\W+");

        WhiteListSingleton st = WhiteListSingleton.getInstance();

        String returnString = "";

        for (String str : carDescriptionTextArray) {
            if (!st.set.contains(str)) {
                str = "";
            }

            returnString = returnString + " " + str;
        }

        return returnString;
    }

    private static String removeUnderscores(String text){
        return text.replace("_", " ");
    }

    private static String removeDuplicateWords(String s) {
        return Arrays.stream(s.split(" ")).distinct().collect(Collectors.joining(" "));
    }

    private static String replaceUmlaute(String orig) {
        String result = orig;
        for (int i = 0; i < UMLAUT_REPLACEMENTS.length; i++) {
            result = result.replace(UMLAUT_REPLACEMENTS[i][0], UMLAUT_REPLACEMENTS[i][1]);
        }
        return result;
    }

    public static String preprocessModel(String modelDescription) {
        String cleaned = Utils.removeUnderscores(modelDescription);
        cleaned = cleaned.toLowerCase();
        cleaned = Utils.replaceUmlaute(cleaned);
        //cleaned = Utils.removeNonDescriptionWords(cleaned);
        cleaned = keepDescriptionWords(cleaned);
        cleaned = Utils.removeDuplicateWords(cleaned);
        return cleaned;
    }

    public static String preprocessTransmission(String transmission) {
        transmission = transmission.toLowerCase();
        if (transmission.equals("manuell"))
            return "manual";
        if (transmission.equals("automatik"))
            return "automatic";
        return transmission;
    }
}

class WhiteListSingleton {
    private static WhiteListSingleton single_instance = null;

    public static WhiteListSingleton getInstance() {
        if (single_instance == null)
            single_instance = new WhiteListSingleton();
        return single_instance;
    }

    public static Set set;

    private WhiteListSingleton() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        File file = new File(classloader.getResource("data/terms-whitelist/listfile_car_emissions.txt").getFile());
        File file2 = new File(classloader.getResource("data/terms-whitelist/listfile_vehicles.txt").getFile());
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedReader br2 = new BufferedReader(new FileReader(file2));
            String whiteListString = br.readLine();
            String whiteListString2 = br2.readLine();
            String[] whiteListArray = whiteListString.split("\\W+");
            String[] whiteListArray2 = whiteListString2.split("\\W+");
            this.set = new HashSet<String>();

            for (String str : whiteListArray) {
                this.set.add(str.toLowerCase());
            }

            for (String str : whiteListArray2) {
                this.set.add(str.toLowerCase());
            }
        } catch (Exception e) {
            // nothing
        }
    }
}
