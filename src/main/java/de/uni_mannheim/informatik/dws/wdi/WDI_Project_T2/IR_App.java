package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking.CarBlockingKeyByManufacturerGenerator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparators.CarManufModelComparatorJaccard;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.*;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.Performance;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
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
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/car_emissions_target.xml").getFile()), "/target/car", carEmissions);

        HashedDataSet<Car, Attribute> offers = new HashedDataSet<>();
        logger.info("Loading offer_target 1 and 2...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/offer_target_1.xml").getFile()), "/target/car", offers);
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/offer_target_2.xml").getFile()), "/target/car", offers);

//        HashedDataSet<Car, Attribute> regionEmissions = new HashedDataSet<>();
//        logger.info("Loading region_emissions_target...");
//        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/region_emissions_target.xml").getFile()), "/target/car", regionEmissions);
//
//        HashedDataSet<Car, Attribute> stations = new HashedDataSet<>();
//        logger.info("Loading station_target...");
//        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/station_target.xml").getFile()), "/target/car", stations);

        HashedDataSet<Car, Attribute> vehicles = new HashedDataSet<>();
        logger.info("Loading vehicles_target...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/vehicles_target.xml").getFile()), "/target/car", vehicles);

        logger.info("Successfully loaded data sets");

        // load the training set
        MatchingGoldStandard gsTraining = new MatchingGoldStandard();
        logger.info("Loading goldstandard:");
        logger.info("####");
        gsTraining.loadFromCSVFile(new File(classloader.getResource("goldstandard/train.csv").getFile()));

        // use linear combination matching rules
        LinearCombinationMatchingRule<Car, Attribute> matchingRule = new LinearCombinationMatchingRule<>(
                0.7);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", -1, gsTraining);

        // add comparators
        matchingRule.addComparator(new CarManufModelComparatorJaccard(), 0.5);

        // Initialize Matching Engine
        MatchingEngine<Car, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        logger.info("*\n*\tRunning identity resolution\n*");
        StandardRecordBlocker<Car, Attribute> blocker = new StandardRecordBlocker<Car, Attribute>(new CarBlockingKeyByManufacturerGenerator());

        Processable<Correspondence<Car, Attribute>> correspondences = engine.runIdentityResolution(
                carEmissions, offers, null, matchingRule, blocker);

        // Create a top-1 global matching
        correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/cars_correspondences.csv"), correspondences);

        // load the gold standard (test set)
        logger.info("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(classloader.getResource("goldstandard/test.csv").getFile()));

        logger.info("*\n*\tEvaluating result\n*");
        // evaluate your result
        MatchingEvaluator<Car, Attribute> evaluator = new MatchingEvaluator<Car, Attribute>();
        Performance perfTest = evaluator.evaluateMatching(correspondences,
                gsTest);

        // print the evaluation result
        logger.info("Academy Awards <-> Actors");
        logger.info(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        logger.info(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        logger.info(String.format(
                "F1: %.4f",perfTest.getF1()));
    }
}