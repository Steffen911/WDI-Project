package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.IdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.Comparators.CarModelComparator_LowerCase;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
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

public class IR_using_linear_combination {
	/*
	 * Logging Options:
	 * 		default: 	level INFO	- console
	 * 		trace:		level TRACE     - console
	 * 		infoFile:	level INFO	- console/file
	 * 		traceFile:	level TRACE	- console/file
	 *  
	 * To set the log level to trace and write the log to winter.log and console, 
	 * activate the "traceFile" logger as follows:
	 *     private static final Logger logger = WinterLogManager.activateLogger("traceFile");
	 *
	 */

	private static final Logger logger = WinterLogManager.activateLogger("default");
    private HashedDataSet<Car, Attribute> dataset1;
    private HashedDataSet<Car, Attribute> dataset2;
    private MatchingGoldStandard gsTraining;
    private LinearCombinationMatchingRule<Car, Attribute> matchingRule;
    private MatchingEngine<Car, Attribute> engine;
    //private CarBlocking_XY<Car, Attribute> blocker;
    private Processable<Correspondence<Car, Attribute>> correspondences;
    private MatchingGoldStandard gsTest;
    private Performance perfTest;


    private void loadData() throws Exception {
        // loading data
        System.out.println("*\n*\tLoading datasets\n*");
        dataset1 = new HashedDataSet<>();
        new CarXMLReader().loadFromXML(new File("data/offer_target_1.xml"),"/Cars/Car", dataset1);
        dataset2 = new HashedDataSet<>();
        new CarXMLReader().loadFromXML(new File("data/car_emissions_target.xml"), "/Cars/Car", dataset2);
    }

    private void loadTrainingSet() throws Exception {
        gsTraining = new MatchingGoldStandard();
        gsTraining.loadFromCSVFile(new File("goldStandard/gold.csv"));
    }

    private void createMatchingRule(){
        
         matchingRule = new LinearCombinationMatchingRule<>(0.7);
        matchingRule.activateDebugReport("data/output/debugResultsMatchingRule.csv", -1, gsTraining);
    }
    private void addComparatorstoMR() throws Exception {
        matchingRule.addComparator(new CarModelComparator_LowerCase(), 1);

    }

    private void createBlocker(){
       // CarBlocking_XY<Car, Attribute> blocker = new CarBlocking_XY();

        //Write debug results to file:
        //blocker.collectBlockSizeData("data/output/debugResultsBlocking.csv", 100);

    }

    private void initMachineEngine(){
        engine = new MatchingEngine<>();
    }

    private void executeMatching(){
        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");
        correspondences = engine.runIdentityResolution(
                dataset1, dataset2, null, matchingRule, null);
    }

    private void writeCorrespondences() throws Exception {
        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("data/output/academy_awards_2_actors_correspondences.csv"), correspondences);
    }

    private void loadGoldStandard() throws Exception {
        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File("goldStandard/gold.csv"));
    }
    private void GlobalMatching(){
        // Create a top-1 global matching
        //correspondences = engine.getTopKInstanceCorrespondences(correspondences, 1, 0.0);

        // Alternative: Create a maximum-weight, bipartite matching
//		MaximumBipartiteMatchingAlgorithm<Car,Attribute> maxWeight = new MaximumBipartiteMatchingAlgorithm<>(correspondences);
//		maxWeight.run();
//		correspondences = maxWeight.getResult();
    }

    private void resultEVA(){
        MatchingEvaluator<Car, Attribute> evaluator = new MatchingEvaluator<Car, Attribute>();
        perfTest = evaluator.evaluateMatching(correspondences,
                gsTest);
    }
    private void printEVA() {
        // print the evaluation result
        System.out.println("Academy Awards <-> Actors");
        System.out.println(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f",perfTest.getF1()));
    }
	
    private static void main( String[] args ) throws Exception {
	    IR_using_linear_combination IR = new IR_using_linear_combination();

	    // loading data
		IR.loadData();

		// load the training set
		IR.loadTrainingSet();

		// create a matching rule
		IR.createMatchingRule();
		
		// add comparators
		IR.addComparatorstoMR();

		// create a blocker (blocking strategy)
        IR.createBlocker();

		// Initialize Matching Engine
		IR.initMachineEngine();

        // Execute the matching
		IR.executeMatching();

		//Global matching?
        //IR.GlobalMatching();

        // write the correspondences to the output file
        IR.writeCorrespondences();

		// load the gold standard (test set)
		IR.loadGoldStandard();
		
		System.out.println("*\n*\tEvaluating result\n*");
		// evaluate your result
		IR.resultEVA();
		IR.printEVA();


    }
}
