package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLReader;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class IR_App {

    public static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main(String[] args) throws Exception {
        // Load the datasets
        logger.info("Loading datasets...");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        HashedDataSet<Car, Attribute> carEmissions = new HashedDataSet<>();
        logger.info("Loading car_emissions_target...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("car_emissions_target.xml").getFile()), "/target/car", carEmissions);

        HashedDataSet<Car, Attribute> offers = new HashedDataSet<>();
        logger.info("Loading offer_target 1 and 2...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("offer_target_1.xml").getFile()), "/target/car", offers);
        new CarXMLReader().loadFromXML(new File(classloader.getResource("offer_target_2.xml").getFile()), "/target/car", offers);

        HashedDataSet<Car, Attribute> regionEmissions = new HashedDataSet<>();
        logger.info("Loading region_emissions_target...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("region_emissions_target.xml").getFile()), "/target/car", regionEmissions);

        HashedDataSet<Car, Attribute> stations = new HashedDataSet<>();
        logger.info("Loading station_target...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("station_target.xml").getFile()), "/target/car", stations);

        HashedDataSet<Car, Attribute> vehicles = new HashedDataSet<>();
        logger.info("Loading vehicles_target...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("vehicles_target.xml").getFile()), "/target/car", vehicles);

        logger.info("Successfully loaded data sets");
    }
}
