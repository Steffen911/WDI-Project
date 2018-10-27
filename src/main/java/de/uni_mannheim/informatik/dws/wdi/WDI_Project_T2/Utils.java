package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {

    private static String[][] UMLAUT_REPLACEMENTS = {
            { new String("ä"), "ae" }, { new String("ü"), "ue" }, { new String("ö"), "oe" },
            { new String("ß"), "ss" }
    };


    private static String[] nonDescriptionWords = {
            "januar", "februar", "maerz", "april", "mai", "juni", "juli", "august", "september", "oktober",
            "november", "dezember",
            "tuev", "noch", "monate", "gebraucht", "tausch",
            "andere", "whatsapp", "sucht",
            "+",  "\"", "!", "/"
    };
    //TODO extend manually



    public static String removeNonDescriptionWords(String text) {
        for (int i = 0; i < nonDescriptionWords.length; i++) {
            if(text.contains(nonDescriptionWords[i])){
                text= text.replace(nonDescriptionWords[i], "");
            }

        }
        return text;
    }

    public static String removeUnderscores(String text){
        return text.replace("_", " ");

    }

    public static String removeDuplicateWords(String s) {
        return Arrays.stream(s.split(" ")).distinct().collect(Collectors.joining(" "));
    }

    public static String replaceUmlaute(String orig) {
        String result = orig;

        for (int i = 0; i < UMLAUT_REPLACEMENTS.length; i++) {
            result = result.replace(UMLAUT_REPLACEMENTS[i][0], UMLAUT_REPLACEMENTS[i][1]);
        }

        return result;
    }
}


