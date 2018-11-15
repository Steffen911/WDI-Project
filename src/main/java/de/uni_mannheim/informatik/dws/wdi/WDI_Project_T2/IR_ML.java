package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking.CarBlockingKeyByManufacturerGenerator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarFuelTypeComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarModelComparatorMaximumTokenContainment;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarModelComparatorTokenizingJaccard;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarModelComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator.CarTransmissionComparatorLevenshtein;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.algorithms.RuleLearner;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.StandardRecordBlocker;
import de.uni_mannheim.informatik.dws.winter.matching.rules.WekaMatchingRule;
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

public class IR_ML {

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

        HashedDataSet<Car, Attribute> vehicles = new HashedDataSet<>();
        logger.info("Loading vehicles_dupfree...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/vehicles_dupfree.xml").getFile()), "/target/car", vehicles);

        logger.info("Successfully loaded data sets");

        // create and trying different matching rules
        String modelType1 = "SimpleLogistic";
        String options1[] = new String[]{"-S"};
        String modelType2 = "NaiveBayes";
        String options2[] = new String[]{""};
        String modelType3 = "RandomForest";
        String options3[] = new String[]{""};
        String modelType4 = "J48";
        String options4[] = new String[]{""};
        String modelType5 = "AdaBoostM1";
        String options5[] = new String[]{""};
        String modelType6 = "ClassificationViaRegression";
        String options6[] = new String[]{""};
        String modelType7 = "DecisionTable";
        String options7[] = new String[]{""};
        String modelType8 = "HoeffdingTree";
        String options8[] = new String[]{""};
        String modelType9 = "IBk";
        String options9[] = new String[]{""};
        String modelType10 = "SMO";
        String options10[] = new String[]{""};
        String modelType11 = "REPTree";
        String options11[] = new String[]{""};

        // Set the classifier. For trying different classifiers, simply replace modeltype and options in the WekaMatchingRule.
        WekaMatchingRule<Car, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType11, options11);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000);

        // Forward / Backward Selection for incremental feature creation / pruning
        //matchingRule.setForwardSelection(true);
        matchingRule.setBackwardSelection(true);

        // add comparators
        matchingRule.addComparator(new CarFuelTypeComparatorLevenshtein());
        matchingRule.addComparator(new CarModelComparatorLevenshtein());
        matchingRule.addComparator(new CarModelComparatorMaximumTokenContainment());
        matchingRule.addComparator(new CarModelComparatorTokenizingJaccard());
        matchingRule.addComparator(new CarModelComparatorLevenshtein());
        matchingRule.addComparator(new CarTransmissionComparatorLevenshtein());

        //loading training_set
        MatchingGoldStandard gsTraining = new MatchingGoldStandard();
        gsTraining.loadFromCSVFile(new File(classloader.getResource("goldstandard/train.csv").getFile()));

        //export comparator features for RapidMiner
        // matchingRule.exportTrainingData(carEmissions, vehicles, gsTraining, new File("data/output/features.csv"));

        // train the matching rule's model
        RuleLearner<Car, Attribute> learner = new RuleLearner<>();
        learner.learnMatchingRule(offerInt, vehicles, null, matchingRule, gsTraining);
        System.out.println(String.format("Matching rule is:\n%s", matchingRule.getModelDescription()));

        //Blocking
        logger.info("Initialize the blocker");
        StandardRecordBlocker<Car, Attribute> blocker = new StandardRecordBlocker<>(new CarBlockingKeyByManufacturerGenerator());
        blocker.setMeasureBlockSizes(true);
        blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

        // Initialize Matching Engine
        MatchingEngine<Car, Attribute> engine = new MatchingEngine<>();

        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        Processable<Correspondence<Car, Attribute>> correspondences = engine.runIdentityResolution(
        offerInt, vehicles, null, matchingRule,
        blocker);

        // Write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/offers_vehicle.csv"), correspondences);

        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        MatchingGoldStandard gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File(classloader.getResource("goldstandard/test.csv").getFile()));

        //Result evaluation
        System.out.println("*\n*\tEvaluating result\n*");
        MatchingEvaluator<Car, Attribute> evaluator = new MatchingEvaluator<>();
        Performance perfTest = evaluator.evaluateMatching(correspondences,
        gsTest);

        //Print
        System.out.println(String.format(
        "Precision: %.4f", perfTest.getPrecision()));
        System.out.println(String.format(
        "Recall: %.4f", perfTest.getRecall()));
        System.out.println(String.format(
        "F1: %.4f", perfTest.getF1()));
    }

}
