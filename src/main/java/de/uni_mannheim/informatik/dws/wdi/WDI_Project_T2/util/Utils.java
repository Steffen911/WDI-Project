package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {

    private static String[][] UMLAUT_REPLACEMENTS = {
            { "ä", "ae" }, { "ü", "ue" }, { "ö", "oe" }, { "ß", "ss" }, { "Ã©", "e" }
    };

    /**
     * List of non-description words.
     * TODO: Should be extended manually
     */
    private static String[] nonDescriptionWords = {
            "januar", "februar", "maerz", "april", "mai", "juni", "juli", "august", "september", "oktober",
            "november", "dezember",
            "tuev", "noch", "monate", "gebraucht", "tausch",
            "andere", "whatsapp", "sucht",
            "+",  "\"", "!", "/",  "Â¾", "pre", "post", "model", "onwards"
    };

    private static String removeNonDescriptionWords(String text) {
        for (int i = 0; i < nonDescriptionWords.length; i++) {
            if(text.contains(nonDescriptionWords[i])){
                text= text.replace(nonDescriptionWords[i], "");
            }
        }
        return text;
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
        cleaned = Utils.replaceUmlaute(cleaned);
        cleaned = Utils.removeNonDescriptionWords(cleaned);
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
