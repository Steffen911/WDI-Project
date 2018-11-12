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
import de.uni_mannheim.informatik.dws.winter.matching.rules.LinearCombinationMatchingRule;
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
        
        
		// create a matching rule
		String options[] = new String[] { "-S" };
		String modelType = "SimpleLogistic"; //logistic regression
		WekaMatchingRule<Car, Attribute> matchingRule = new WekaMatchingRule<>(0.7, modelType, options);
		matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", 1000);
        
		// add comparators
		matchingRule.addComparator(new CarFuelTypeComparatorLevenshtein());
		matchingRule.addComparator(new CarModelComparatorLevenshtein());
		matchingRule.addComparator(new CarModelComparatorMaximumTokenContainment());
		matchingRule.addComparator(new CarModelComparatorTokenizingJaccard());
		matchingRule.addComparator(new CarModelComparatorLevenshtein());
		matchingRule.addComparator(new CarTransmissionComparatorLevenshtein());
        
		//loading training_set
		MatchingGoldStandard gsTraining = new MatchingGoldStandard();
		gsTraining.loadFromCSVFile(new File(classloader.getResource("goldstandard/test.csv").getFile()));
		// train the matching rule's model
		RuleLearner<Car, Attribute> learner = new RuleLearner<>();
		learner.learnMatchingRule(carEmissions, vehicles, null, matchingRule, gsTraining);
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
		carEmissions, vehicles, null, matchingRule,
				blocker);
		
		// Write the correspondences to the output file
		new CSVCorrespondenceFormatter().writeCSV(new File("data/output/carEmissions_vehicle.csv"), correspondences);
		
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
				"Precision: %.4f",perfTest.getPrecision()));
		System.out.println(String.format(
				"Recall: %.4f",	perfTest.getRecall()));
		System.out.println(String.format(
				"F1: %.4f",perfTest.getF1()));
				        
    }

}
