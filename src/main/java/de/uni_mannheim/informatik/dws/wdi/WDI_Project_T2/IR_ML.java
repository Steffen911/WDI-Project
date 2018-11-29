package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking.CarBlockingKeyByManufacturerGenerator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.*;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
import de.uni_mannheim.informatik.dws.winter.model.*;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.CSVCorrespondenceFormatter;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Random;

public class IR_ML {

    public static final Logger logger = WinterLogManager.activateLogger("default");
    private static ClassLoader classloader = Thread.currentThread().getContextClassLoader();

    private static HashedDataSet<Car, Attribute> carEmissions = new HashedDataSet<>();
    private static HashedDataSet<Car, Attribute> offerInt = new HashedDataSet<>();
    private static HashedDataSet<Car, Attribute> vehicles = new HashedDataSet<>();

    private static MatchingGoldStandard oceGs = new MatchingGoldStandard();
    private static MatchingGoldStandard ovecGs = new MatchingGoldStandard();
    private static MatchingGoldStandard cevecGs = new MatchingGoldStandard();

    public static void main(String[] args) throws Exception {
        // Load the datasets
        logger.info("Loading datasets...");

        logger.info("Loading car_emissions_dupfree...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/car_emissions_dupfree.xml").getFile()), "/target/car", carEmissions);

        logger.info("Loading offers_dupfree ...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/offers_dupfree.xml").getFile()), "/target/car", offerInt);

        logger.info("Loading vehicles_dupfree...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/vehicles_dupfree.xml").getFile()), "/target/car", vehicles);

        logger.info("Successfully loaded data sets");

        // create and trying different matching rules
        // String modelType1 = "SimpleLogistic";
        // String options1[] = new String[]{"-S"};
        // String modelType2 = "NaiveBayes";
        // String options2[] = new String[]{""};
        // String modelType3 = "RandomForest";
        // String options3[] = new String[]{""};
        // String modelType6 = "ClassificationViaRegression";
        // String options6[] = new String[]{""};
        // String modelType7 = "DecisionTable";
        // String options7[] = new String[]{""};
        // String modelType8 = "HoeffdingTree";
        // String options8[] = new String[]{""};
        // String modelType10 = "SMO";
        // String options10[] = new String[]{""};
        // String modelType11 = "REPTree";
        // String options11[] = new String[]{""};

        oceGs.loadFromCSVFile(new File(classloader.getResource("goldstandard/oce.csv").getFile()));
        ovecGs.loadFromCSVFile(new File(classloader.getResource("goldstandard/ovec.csv").getFile()));
        cevecGs.loadFromCSVFile(new File(classloader.getResource("goldstandard/cevec.csv").getFile()));

        //export RapidMiner features
        // disabled because performed only once, based on static comparators
        //exportRapidMinerFeatures(matchingRule, vehicles, offerInt, carEmissions);

        //Blocking
        logger.info("Initialize the blocker");
        StandardRecordBlocker<Car, Attribute> blocker = new StandardRecordBlocker<>(new CarBlockingKeyByManufacturerGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        //Loading test set (combined goldstandard)
        MatchingGoldStandard goldStandardTest = new MatchingGoldStandard();
        goldStandardTest.loadFromCSVFile(new File(classloader.getResource("goldstandard/combined.csv").getFile()));

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

            // Add all GS elements to the sample
            List<Pair<String, String>> fullGS = goldStandardTest.getPositiveExamples();
            fullGS.addAll(goldStandardTest.getNegativeExamples());
            for (Pair<String, String> corr : fullGS) {
                String first = corr.getFirst();
                String second = corr.getSecond();
                if (first.contains("offer") && offerInt.getRecord(first) != null) {
                    offers.add(offerInt.getRecord(first));
                }
                if (second.contains("offer") && offerInt.getRecord(second) != null) {
                    offers.add(offerInt.getRecord(second));
                }
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

        evaluateDataset("offers-caremissions", offersCarEmissionCorrespondences, oceGs);
        evaluateDataset("offers-vehicles", offersVehiclesCorrespondences, ovecGs);
        evaluateDataset("vehicles-caremissions", vehiclesCarEmissionCorrespondences, cevecGs);

    }

    private static void exportRapidMinerFeatures(WekaMatchingRule<Car, Attribute> matchingRule, HashedDataSet<Car, Attribute> vehicles,
                                                 HashedDataSet<Car, Attribute> offerInt, HashedDataSet<Car, Attribute> carEmissions) throws Exception {

        MatchingGoldStandard gsTrainingRapidMiner = new MatchingGoldStandard();
        gsTrainingRapidMiner.loadFromCSVFile(new File(classloader.getResource("goldstandard/train_rapidminer.csv").getFile()));

        //export comparator features for RapidMiner
        matchingRule.exportTrainingData(carEmissions, vehicles, gsTrainingRapidMiner, new File("data/output/carem_veh_features.csv"));
        matchingRule.exportTrainingData(offerInt, vehicles, gsTrainingRapidMiner, new File("data/output/offer_veh_features.csv"));
        matchingRule.exportTrainingData(offerInt, carEmissions, gsTrainingRapidMiner, new File("data/output/offer_carem_features.csv"));
    }

    private static void addComparatorsToMatchingRule(WekaMatchingRule<Car, Attribute> matchingRule) {
        matchingRule.addComparator(new CarFuelTypeComparatorLevenshtein());
        matchingRule.addComparator(new CarModelComparatorLevenshtein());
        matchingRule.addComparator(new CarModelComparatorMaximumTokenContainment());
        matchingRule.addComparator(new CarModelComparatorTokenizingJaccard());
        matchingRule.addComparator(new CarModelComparatorLevenshtein());
        matchingRule.addComparator(new CarTransmissionComparatorLevenshtein());
    }

    /**
     * Get correspondences for car emissions and offers.
     */
    private static Processable<Correspondence<Car, Attribute>> getOffersCarEmissionsCorrespondences(
    HashedDataSet<Car, Attribute> d1,
    HashedDataSet<Car, Attribute> d2
    ) throws Exception {

        // WEKA/IBk for Offers-CarEmissions (F1: .793)
        String modelType9 = "IBk";
        String options9[] = new String[]{""};
        WekaMatchingRule<Car, Attribute> matchingRuleOffCarEm = new WekaMatchingRule<>(0.7, modelType9, options9);
        matchingRuleOffCarEm.setBackwardSelection(true);
        addComparatorsToMatchingRule(matchingRuleOffCarEm);
        matchingRuleOffCarEm.activateDebugReport("data/output/debugResultsMatchingRuleOffVeh.csv", 1000);

        RuleLearner<Car, Attribute> learnerOffersCarEmissions = new RuleLearner<>();
        learnerOffersCarEmissions.learnMatchingRule(offerInt, carEmissions, null, matchingRuleOffCarEm, oceGs);

        // Add blocking strategy
        logger.info("Initialize the blocker");
        StandardRecordBlocker<Car, Attribute> blocker = new StandardRecordBlocker<>(new CarBlockingKeyByManufacturerGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Add matching engine
        MatchingEngine<Car, Attribute> engine = new MatchingEngine<>();
        return engine.runIdentityResolution(d1, d2, null, matchingRuleOffCarEm, blocker);
    }

    /**
     * Get correspondences for offers and vehicles.
     */
    private static Processable<Correspondence<Car, Attribute>> getOffersVehiclesCorrespondences(
    HashedDataSet<Car, Attribute> d1,
    HashedDataSet<Car, Attribute> d2
    ) throws Exception {
        // Add comparators
        logger.info("Add matchingrules");
        // WEKA/J48 for Offers-Vehicles (F1: .827)
        String modelType4 = "J48";
        String options4[] = new String[]{""};
        WekaMatchingRule<Car, Attribute> matchingRuleOffVeh = new WekaMatchingRule<>(0.7, modelType4, options4);
        matchingRuleOffVeh.setBackwardSelection(true);
        addComparatorsToMatchingRule(matchingRuleOffVeh);
        matchingRuleOffVeh.activateDebugReport("data/output/debugResultsMatchingRuleOffVeh.csv", 1000);

        RuleLearner<Car, Attribute> learnerOffersVehicles = new RuleLearner<>();
        learnerOffersVehicles.learnMatchingRule(offerInt, vehicles, null, matchingRuleOffVeh, ovecGs);

        // Add blocking strategy
        logger.info("Initialize the blocker");
        StandardRecordBlocker<Car, Attribute> blocker = new StandardRecordBlocker<>(new CarBlockingKeyByManufacturerGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Add matching engine
        MatchingEngine<Car, Attribute> engine = new MatchingEngine<>();
        return engine.runIdentityResolution(d1, d2, null, matchingRuleOffVeh, blocker);
    }

    /**
     * Get correspondences for car emissions and vehicles.
     */
    private static Processable<Correspondence<Car, Attribute>> getVehiclesCarEmissionCorrespondences(
    HashedDataSet<Car, Attribute> d1,
    HashedDataSet<Car, Attribute> d2
    ) throws Exception {

        // WEKA/AdaBoostM1for CarEmissions-Vehicles (F1: .950)
        String modelType5 = "AdaBoostM1";
        String options5[] = new String[]{""};
        WekaMatchingRule<Car, Attribute> matchingRuleCarEmVeh = new WekaMatchingRule<>(0.7, modelType5, options5);
        matchingRuleCarEmVeh.setBackwardSelection(true);

        addComparatorsToMatchingRule(matchingRuleCarEmVeh);
        matchingRuleCarEmVeh.activateDebugReport("data/output/debugResultsMatchingRuleCarEmVeh.csv", 1000);

        RuleLearner<Car, Attribute> learnerCarEmissionsVehicles = new RuleLearner<>();
        learnerCarEmissionsVehicles.learnMatchingRule(vehicles, carEmissions, null, matchingRuleCarEmVeh, cevecGs);

        // Add blocking strategy
        logger.info("Initialize the blocker");
        StandardRecordBlocker<Car, Attribute> blocker = new StandardRecordBlocker<>(new CarBlockingKeyByManufacturerGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Add matching engine
        MatchingEngine<Car, Attribute> engine = new MatchingEngine<>();
        return engine.runIdentityResolution(d1, d2, null, matchingRuleCarEmVeh, blocker);
    }

    /**
     * Get a random element from the given array.
     */
    private static Car getRandom(Car[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

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
}
