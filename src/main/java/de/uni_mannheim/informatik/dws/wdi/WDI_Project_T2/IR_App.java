package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking.CarBlockingKeyByManufacturerGenerator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarFuelTypeComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarModelComparatorMaximumTokenContainment;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarTransmissionComparatorLevenshtein;
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
        logger.info("Loading car_emissions_dupfree...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/car_emissions_dupfree.xml").getFile()), "/target/car", carEmissions);

        HashedDataSet<Car, Attribute> offerInt = new HashedDataSet<>();
        logger.info("Loading offers_dupfree ...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/offers_dupfree.xml").getFile()), "/target/car", offerInt);

        HashedDataSet<Car, Attribute> stations = new HashedDataSet<>();
        logger.info("Loading station_target...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/station_target.xml").getFile()), "/target/car", stations);

        HashedDataSet<Car, Attribute> vehicles = new HashedDataSet<>();
        logger.info("Loading vehicles_dupfree...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/vehicles_dupfree.xml").getFile()), "/target/car", vehicles);

        logger.info("Successfully loaded data sets");

        // Prepare reusable datasets and parameters
        int blockSize = 1000;
        int iterations = 1;
        logger.info("matching " + blockSize * iterations + " random offers with carEmissions");
        Car[] carOffers = offerInt.get().toArray(new Car[]{});
        Processable<Correspondence<Car, Attribute>> offersCarEmissionCorrespondences = null;
        Processable<Correspondence<Car, Attribute>> offersVehiclesCorrespondences = null;
        Processable<Correspondence<Car, Attribute>> vehiclesCarEmissionCorrespondences = null;
        for (int i = 0; i < iterations; i++) {

            System.gc();

            // Prepare a sampled offers dataset
            HashedDataSet<Car, Attribute> offers = new HashedDataSet<>();
            for (int j = 0; j < blockSize; j++) {
                offers.add(getRandom(carOffers));
            }

            logger.info("Start the matching for iteration " + i + "/" + iterations);

            Processable<Correspondence<Car, Attribute>> corr;

            /*
             * Offers - Car Emissions
             */
            corr = getOffersCarEmissionsCorrespondences(offers, carEmissions);
            if (offersCarEmissionCorrespondences == null) {
                offersCarEmissionCorrespondences = corr;
            } else {
                for (Correspondence<Car, Attribute> correspondence : corr.get()) {
                    offersCarEmissionCorrespondences.add(correspondence);
                }
            }

            /*
             * Offers - Vehicles
             */
            corr = getOffersVehiclesCorrespondences(offers, vehicles);
            if (offersVehiclesCorrespondences == null) {
                offersVehiclesCorrespondences = corr;
            } else {
                for (Correspondence<Car, Attribute> correspondence : corr.get()) {
                    offersVehiclesCorrespondences.add(correspondence);
                }
            }

            /*
             * Offers - Vehicles
             */
            corr = getVehiclesCarEmissionCorrespondences(vehicles, carEmissions);
            if (vehiclesCarEmissionCorrespondences == null) {
                vehiclesCarEmissionCorrespondences = corr;
            } else {
                for (Correspondence<Car, Attribute> correspondence : corr.get()) {
                    vehiclesCarEmissionCorrespondences.add(correspondence);
                }
            }

            logger.info("Successfully completed the matching for iteration " + (i + 1) + "/" + iterations);

        }

        MatchingGoldStandard goldStandardTest = new MatchingGoldStandard();
        goldStandardTest.loadFromCSVFile(new File(classloader.getResource("goldstandard/test.csv").getFile()));

        evaluateDataset("offers-caremissions", offersCarEmissionCorrespondences, goldStandardTest);
        evaluateDataset("offers-vehicles", offersVehiclesCorrespondences, goldStandardTest);
        evaluateDataset("vehicles-caremissions", vehiclesCarEmissionCorrespondences, goldStandardTest);
    }

    /**
     * Write the evaluation results to the given name and log precision, recall and f1.
     */
    private static void evaluateDataset(String name, Processable<Correspondence<Car, Attribute>> corr, MatchingGoldStandard gs) throws Exception {
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/" + name + ".csv"), corr);
        logger.info("Successfully wrote " + name + " to data/output/...");

        logger.info("Starting the result evaluation...");
        MatchingEvaluator<Car, Attribute> evaluator = new MatchingEvaluator<>();
        Performance performance = evaluator.evaluateMatching(corr, gs);

        logger.info("Results for " + name);
        logger.info(String.format("Precision: %.4f", performance.getPrecision()));
        logger.info(String.format("Recall: %.4f", performance.getRecall()));
        logger.info(String.format("F1: %.4f", performance.getF1()));
    }

    /**
     * Get correspondences for car emissions and offers.
     */
    private static Processable<Correspondence<Car, Attribute>> getOffersCarEmissionsCorrespondences(
        HashedDataSet<Car, Attribute> d1,
        HashedDataSet<Car, Attribute> d2
    ) throws Exception {
        // Add comparators
        logger.info("Add matchingrules");
        LinearCombinationMatchingRule<Car, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.65);
        matchingRule.addComparator(new CarModelComparatorMaximumTokenContainment(), 0.5);
        matchingRule.addComparator(new CarFuelTypeComparatorLevenshtein(), 0.3);
        matchingRule.addComparator(new CarTransmissionComparatorLevenshtein(), 0.2);

        // Add blocking strategy
        logger.info("Initialize the blocker");
        StandardRecordBlocker<Car, Attribute> blocker = new StandardRecordBlocker<>(new CarBlockingKeyByManufacturerGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Add matching engine
        MatchingEngine<Car, Attribute> engine = new MatchingEngine<>();
        return engine.runIdentityResolution(d1, d2, null, matchingRule, blocker);
    }

    /**
     * Get correspondences for offers and vehicles.
     * For the moment we reuse the parameterization in offers - car emissions, but if we want
     * to have independent matching rules and comparators we may just copy the body of the other
     * method and adjust those settings.
     */
    private static Processable<Correspondence<Car, Attribute>> getOffersVehiclesCorrespondences(
        HashedDataSet<Car, Attribute> d1,
        HashedDataSet<Car, Attribute> d2
    ) throws Exception {
        return getOffersCarEmissionsCorrespondences(d1, d2);
    }

    /**
     * Get correspondences for car emissions and vehicles.
     */
    private static Processable<Correspondence<Car, Attribute>> getVehiclesCarEmissionCorrespondences(
        HashedDataSet<Car, Attribute> d1,
        HashedDataSet<Car, Attribute> d2
    ) throws Exception {
        return getOffersCarEmissionsCorrespondences(d1, d2);
    }

    /**
     * Get a random element from the given array.
     */
    private static Car getRandom(Car[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

}
