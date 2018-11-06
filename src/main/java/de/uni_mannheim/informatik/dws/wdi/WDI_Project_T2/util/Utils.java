package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    private static String[][] UMLAUT_REPLACEMENTS = {
    { "ä", "ae" }, { "ü", "ue" }, { "ö", "oe" }, { "ß", "ss" }
    };

    /**
     * List of non-description words.
     * TODO: Should be extended manually
     */

    //whitelisting approach
    public static String keepDescriptionWords(String carDescriptionText) {

        // Convert descriptive String into array. Splitting by blank space (regex)
        String[] carDescriptionTextArray = carDescriptionText.split("\\W+");

        WhiteListSingleton st = WhiteListSingleton.getInstance();

        // Set because in the descriptions model or manufacturer names may occur multiple times. LinkedHashSet so that order of elements in set remains.
        Set<String> lhs = new LinkedHashSet<>();

        // For each word of the description (i.e. iterating over the LinkedHashSet), check if it is contained in the whitelist set.
        // If word of description is contained in the white-list, keep it. If not, it is ignored (not added to LinkedHashSet).
        for (String str : carDescriptionTextArray) {
            if (st.getSet().contains(str)) {
                lhs.add(str);
            }
        }

        String returnString = "";

        // Iterate over LinkedHashSet, which contains the allowed terms of a description string in their origin order. Concat the return String by those elements,
        // split by blank.
        for (String stringElement : lhs) {
            returnString += " " + stringElement;
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

