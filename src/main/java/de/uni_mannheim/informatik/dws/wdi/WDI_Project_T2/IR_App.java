package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking.CarBlockingKeyByManufacturerGenerator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking.CarBlockingKeyByStationIdGenerator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking.CarBlockingKeyByZipGenerator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.*;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Random;

public class IR_App {

    public static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main(String[] args) throws Exception {
        // Load the datasets
        logger.info("Loading datasets...");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        HashedDataSet<Car, Attribute> carEmissions = IR_App.loadData("car_emissions_dupfree");
        HashedDataSet<Car, Attribute> offerInt = IR_App.loadData("offers_dupfree");
        HashedDataSet<Car, Attribute> stations = IR_App.loadData("station_target");
        HashedDataSet<Car, Attribute> regionEmissions = IR_App.loadData("region_emissions_target");
        HashedDataSet<Car, Attribute> vehicles = IR_App.loadData("vehicles_dupfree");

        logger.info("Successfully loaded data sets");

        MatchingGoldStandard goldStandardTest = new MatchingGoldStandard();
        goldStandardTest.loadFromCSVFile(new File(classloader.getResource("goldstandard/combined.csv").getFile()));
        logger.info("Successfully loaded the goldstandard");

        // Prepare reusable datasets and parameters
        int blockSize = 1000;
        int iterations = 1;
        logger.info("matching " + blockSize * iterations + " random offers with carEmissions");
        Car[] carOffers = offerInt.get().toArray(new Car[]{});
        Processable<Correspondence<Car, Attribute>> offersCarEmissionCorrespondences = null;
        Processable<Correspondence<Car, Attribute>> offersVehiclesCorrespondences = null;
        Processable<Correspondence<Car, Attribute>> vehiclesCarEmissionCorrespondences = null;
        Processable<Correspondence<Car, Attribute>> stationsRegionEmissionCorrespondences = null;
        Processable<Correspondence<Car, Attribute>> offersStationsCorrespondences = null;
        for (int i = 0; i < iterations; i++) {

            System.gc();

            // Prepare a sampled offers dataset
            HashedDataSet<Car, Attribute> offers = new HashedDataSet<>();
            for (int j = 0; j < blockSize; j++) {
                offers.add(getRandom(carOffers));
            }

            // Add all GS elements to the sample
            List<Pair<String, String>> fullGS = goldStandardTest.getPositiveExamples();
            fullGS.addAll(goldStandardTest.getNegativeExamples());
            for(Pair<String, String> corr : fullGS) {
                String first = corr.getFirst();
                String second = corr.getSecond();
                if (first.contains("offer") && offerInt.getRecord(first) != null) {
                    offers.add(offerInt.getRecord(first));
                }
                if (second.contains("offer") && offerInt.getRecord(second) != null) {
                    offers.add(offerInt.getRecord(second));
                }
            }

            offers.add(offerInt.getRecord("offer_1"));
            offers.add(offerInt.getRecord("offer_3"));
            offers.add(offerInt.getRecord("offer_50"));
            offers.add(offerInt.getRecord("offer_63"));
            offers.add(offerInt.getRecord("offer_5"));
            offers.add(offerInt.getRecord("offer_935"));
            offers.add(offerInt.getRecord("offer_936"));
            offers.add(offerInt.getRecord("offer_3336"));
            offers.add(offerInt.getRecord("offer_9800"));
            offers.add(offerInt.getRecord("offer_9819"));
            offers.add(offerInt.getRecord("offer_9820"));
            offers.add(offerInt.getRecord("offer_9828"));
            offers.add(offerInt.getRecord("offer_9814"));
            offers.add(offerInt.getRecord("offer_9853"));
            offers.add(offerInt.getRecord("offer_9873"));
            offers.add(offerInt.getRecord("offer_9883"));

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

            /*
             * Stations - Region Emissions
             */
            corr = getStationsRegionEmissionCorrespondences(stations, regionEmissions);
            if (stationsRegionEmissionCorrespondences == null) {
                stationsRegionEmissionCorrespondences = corr;
            } else {
                for (Correspondence<Car, Attribute> correspondence : corr.get()) {
                    stationsRegionEmissionCorrespondences.add(correspondence);
                }
            }

            /*
             * Offers - Stations
             */
            corr = getOffersStationsCorrespondences(offers, stations);
            if (offersStationsCorrespondences == null) {
                offersStationsCorrespondences = corr;
            } else {
                for (Correspondence<Car, Attribute> correspondence : corr.get()) {
                    offersStationsCorrespondences.add(correspondence);
                }
            }

            logger.info("Successfully completed the matching for iteration " + (i + 1) + "/" + iterations);

        }

        MatchingGoldStandard oceGs = new MatchingGoldStandard();
        oceGs.loadFromCSVFile(new File(classloader.getResource("goldstandard/oce.csv").getFile()));
        MatchingGoldStandard ovecGs = new MatchingGoldStandard();
        ovecGs.loadFromCSVFile(new File(classloader.getResource("goldstandard/ovec.csv").getFile()));
        MatchingGoldStandard cevecGs = new MatchingGoldStandard();
        cevecGs.loadFromCSVFile(new File(classloader.getResource("goldstandard/cevec.csv").getFile()));
        MatchingGoldStandard sreGs = new MatchingGoldStandard();
        sreGs.loadFromCSVFile(new File(classloader.getResource("goldstandard/sre.csv").getFile()));
        MatchingGoldStandard osGs = new MatchingGoldStandard();
        osGs.loadFromCSVFile(new File(classloader.getResource("goldstandard/os.csv").getFile()));
        logger.info("Successfully loaded the goldstandards");

        evaluateDataset("offers-caremissions", offersCarEmissionCorrespondences, oceGs);
        evaluateDataset("offers-vehicles", offersVehiclesCorrespondences, ovecGs);
        evaluateDataset("vehicles-caremissions", vehiclesCarEmissionCorrespondences, cevecGs);
        evaluateDataset("stations-regionEmissions", stationsRegionEmissionCorrespondences, sreGs);
        evaluateDataset("offers-stations", offersStationsCorrespondences, osGs);
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
        // Add comparators
        logger.info("Add matchingrules");
        LinearCombinationMatchingRule<Car, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.5);
        matchingRule.addComparator(new CarModelComparatorTokenizingJaccard(), 0.5);
        matchingRule.addComparator(new CarFuelTypeComparatorLevenshtein(), 0.1);
        matchingRule.addComparator(new CarTransmissionComparatorLevenshtein(), 0.4);

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
     * Get correspondences for car emissions and vehicles.
     */
    private static Processable<Correspondence<Car, Attribute>> getVehiclesCarEmissionCorrespondences(
        HashedDataSet<Car, Attribute> d1,
        HashedDataSet<Car, Attribute> d2
    ) throws Exception {
        return getOffersCarEmissionsCorrespondences(d1, d2);
    }

    /**
     * Get correspondences for stations and region emissions.
     */
    private static Processable<Correspondence<Car, Attribute>> getOffersStationsCorrespondences(
        HashedDataSet<Car, Attribute> d1,
        HashedDataSet<Car, Attribute> d2
    ) throws Exception {
        // Add comparators
        logger.info("Add matchingrules");
        LinearCombinationMatchingRule<Car, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.9);
        matchingRule.addComparator(new CarZipComparatorAbsoluteDifference(), 1.0);

        // Add blocking strategy
        logger.info("Initialize the blocker");
        StandardRecordBlocker<Car, Attribute> blocker = new StandardRecordBlocker<>(new CarBlockingKeyByZipGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Add matching engine
        MatchingEngine<Car, Attribute> engine = new MatchingEngine<>();
        return engine.runIdentityResolution(d1, d2, null, matchingRule, blocker);
    }

    /**
     * Get correspondences for offers and stations.
     */
    private static Processable<Correspondence<Car, Attribute>> getStationsRegionEmissionCorrespondences(
        HashedDataSet<Car, Attribute> d1,
        HashedDataSet<Car, Attribute> d2
    ) throws Exception {
        // Add comparators
        logger.info("Add matchingrules");
        LinearCombinationMatchingRule<Car, Attribute> matchingRule = new LinearCombinationMatchingRule<>(0.9);
        matchingRule.addComparator(new CarStationComparatorLevenshtein(), 1.0);

        // Add blocking strategy
        logger.info("Initialize the blocker");
        StandardRecordBlocker<Car, Attribute> blocker = new StandardRecordBlocker<>(new CarBlockingKeyByStationIdGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Add matching engine
        MatchingEngine<Car, Attribute> engine = new MatchingEngine<>();
        return engine.runIdentityResolution(d1, d2, null, matchingRule, blocker);
    }

    /**
     * Get a random element from the given array.
     */
    private static Car getRandom(Car[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    /**
     * Load a dataset with the given name into memory and return it.
     */
    private static HashedDataSet<Car, Attribute> loadData(String fileName) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        HashedDataSet<Car, Attribute> ds = new HashedDataSet<>();
        logger.info("Loading " + fileName + "...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/" + fileName + ".xml").getFile()), "/target/car", ds);
        return ds;
    }

}
