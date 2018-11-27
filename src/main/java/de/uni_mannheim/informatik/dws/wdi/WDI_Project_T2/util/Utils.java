package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {

    private static String[][] UMLAUT_REPLACEMENTS = {
    { "ä", "ae" }, { "ü", "ue" }, { "ö", "oe" }, { "ß", "ss" }
    };

    private static String[] IGNORE_WORDS = {"door", "tuer", "edition", "start", "stop", "assistant", "august", "november", "february", "march", "april", "june", "july", "august", "september", "october", "november", "december"};


    private static Pattern patternYears = Pattern.compile("(19[0-9]{2}$)|(2[0-9]{3}$)|(19[0-9]{2})|(2[0-9]{3})");

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
    private static String ignoreWords(String text){
        String result = text;
        for (int i = 0; i < IGNORE_WORDS.length; i++) {
            result = result.replace(IGNORE_WORDS[i], "");
        }
        return result;
    }

    private static String removeDuplicateWords(String s) {
        return Arrays.stream(s.split(" ")).distinct().collect(Collectors.joining(" "));
    }

    private static String removeYears(String text){
        Matcher matcher = patternYears.matcher(text);
        return matcher.replaceAll("");
    }

    private static String replaceUmlaute(String orig) {
        String result = orig;
        for (int i = 0; i < UMLAUT_REPLACEMENTS.length; i++) {
            result = result.replace(UMLAUT_REPLACEMENTS[i][0], UMLAUT_REPLACEMENTS[i][1]);
        }
        return result;
    }

    public static String preprocessModel(String modelDescription) {
        if (modelDescription.equals("")){
            return modelDescription;
        }
        String cleaned = Utils.removeUnderscores(modelDescription);
        cleaned = Utils.removeYears(cleaned);
        cleaned = Utils.replaceUmlaute(cleaned);
        cleaned = keepDescriptionWords(cleaned);
        cleaned = Utils.ignoreWords(cleaned);
        cleaned = Utils.removeDuplicateWords(cleaned);
        return cleaned.trim();
    }

    public static String preprocessTransmission(String transmission) {
        transmission = transmission.toLowerCase();
        if (transmission.equals("manuell"))
            return "manual";
        if (transmission.equals("automatik"))
            return "automatic";
        return transmission;
    }

    public static String cleanModelDescriptionDataFusion(String modelDescription){
        if (modelDescription.equals("")){
            return modelDescription;
        }
        String cleaned = Utils.removeUnderscores(modelDescription);
        cleaned = Utils.replaceUmlaute(cleaned);
        cleaned = keepDescriptionWords(cleaned);
        cleaned = Utils.ignoreWords(cleaned);
        cleaned = Utils.removeDuplicateWords(cleaned);
        return cleaned.trim();

    }
}

