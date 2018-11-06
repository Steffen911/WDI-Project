package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util;

import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

public class WhiteListSingleton {

    public static final Logger logger = WinterLogManager.activateLogger("default");

    private static WhiteListSingleton single_instance = null;

    public static WhiteListSingleton getInstance() {
        if (single_instance == null)
            single_instance = new WhiteListSingleton();
        return single_instance;
    }

    public static Set getSet() {
        return set;
    }

    private static Set set;

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
            this.set = new HashSet<String>();
            logger.error("Failed loading white-list files. Class: WhiteListSingeleton.");
        }
    }
}
