package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking.CarBlockingKeyByManufacturerGenerator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarFuelTypeComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarModelComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarTransmissionComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.Car_Model_MaximumOfTokenContainment_Comparator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
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
import java.util.Random;

public class IR_App {

    public static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main(String[] args) throws Exception {
        // Load the datasets
        logger.info("Loading datasets...");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        HashedDataSet<Car, Attribute> carEmissions = new HashedDataSet<>();
        logger.info("Loading car_emissions_target...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/car_emissions_target.xml").getFile()), "/target/car", carEmissions);

        HashedDataSet<Car, Attribute> offerInt = new HashedDataSet<>();
        logger.info("Loading offer_target - dupfree ...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/offers_dupfree.xml").getFile()), "/target/car", offerInt);

        HashedDataSet<Car, Attribute> stations = new HashedDataSet<>();
        logger.info("Loading station_target...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/station_target.xml").getFile()), "/target/car", stations);

        HashedDataSet<Car, Attribute> vehicles = new HashedDataSet<>();
        logger.info("Loading vehicles_target...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/vehicles_target.xml").getFile()), "/target/car", vehicles);

        logger.info("Successfully loaded data sets");

        // Load the goldstandard training set
        MatchingGoldStandard goldStandardTrain = new MatchingGoldStandard();
        logger.info("Loading the goldstandard training set...");
        goldStandardTrain.loadFromCSVFile(new File(classloader.getResource("goldstandard/train.csv").getFile()));

        // Prepare reusable datasets and parameters
        int blockSize = 900;
        int iterations = 3;
        logger.info("matching " + blockSize * iterations + " random offers with carEmissions");
        Car[] carOffers = offerInt.get().toArray(new Car[]{});
        Processable<Correspondence<Car, Attribute>> correspondences = null;
        for (int i = 0; i < iterations; i++) {

            System.gc();

            // Add comparators
            logger.info("Add matchingrules");
            LinearCombinationMatchingRule<Car, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.65);
            matchingRule.activateDebugReport("C:\\Users\\Wifo\\workspace_ItelliJ\\WDI-Project\\src\\main\\resources\\data\\output\\debugResultsMatchingRule.csv", -1, goldStandardTrain);
            matchingRule.addComparator(new Car_Model_MaximumOfTokenContainment_Comparator(), 0.5);
            matchingRule.addComparator(new CarFuelTypeComparatorLevenshtein(), 0.3);
            matchingRule.addComparator(new CarTransmissionComparatorLevenshtein(), 0.2);

            // Add blocking strategy
            logger.info("Initialize the blocker");
            StandardRecordBlocker<Car, Attribute> blocker = new StandardRecordBlocker<>(new CarBlockingKeyByManufacturerGenerator());
            blocker.setMeasureBlockSizes(true);
            blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

            // Add matching engine
            MatchingEngine<Car, Attribute> engine = new MatchingEngine<>();

            // prepare data set
            // Take the first x samples from the offers dataset
            HashedDataSet<Car, Attribute> offers = new HashedDataSet<>();
            for (int j = 0; j < blockSize; j++) {
                offers.add(getRandom(carOffers));
            }

            // Start the matching
            logger.info("Start the matching for iteration " + i + "/" + iterations);
            Processable<Correspondence<Car, Attribute>> corr = engine.runIdentityResolution(offers, carEmissions, null, matchingRule, blocker);
            if (correspondences == null) {
                correspondences = corr;
            } else {
                for (Correspondence<Car, Attribute> correspondence : corr.get()) {
                    correspondences.add(correspondence);
                }
            }
            logger.info("Successfully completed the matching for iteration " + i + "/" + iterations + " for a total of " + correspondences.size() + " correspondences");

        }

        new CSVCorrespondenceFormatter().writeCSV(new File("C:\\Users\\Wifo\\workspace_ItelliJ\\WDI-Project\\src\\main\\resources\\data\\output\\offers_car_emissions_correspondences.csv"), correspondences);
        logger.info("Successfully wrote the correspondences to data/output/...");

        MatchingGoldStandard goldStandardTest = new MatchingGoldStandard();
        goldStandardTest.loadFromCSVFile(new File(classloader.getResource("goldstandard/test.csv").getFile()));

        logger.info("Starting the result evaluation...");
        MatchingEvaluator<Car, Attribute> evaluator = new MatchingEvaluator<>();
        Performance performance = evaluator.evaluateMatching(correspondences, goldStandardTest);

        logger.info("Results for offers <-> car_emissions");
        logger.info(String.format("Precision: %.4f", performance.getPrecision()));
        logger.info(String.format("Recall: %.4f", performance.getRecall()));
        logger.info(String.format("F1: %.4f", performance.getF1()));
    }

    private static Car getRandom(Car[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }


}
