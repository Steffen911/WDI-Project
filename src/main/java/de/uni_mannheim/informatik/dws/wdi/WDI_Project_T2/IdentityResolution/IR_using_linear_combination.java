package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.IdentityResolution;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking.CarBlockingKeyByManufacturerGenerator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking.CarBlocking_Manufacturer;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.Comparators.CarManufacturerComparator_LowerCase;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLReader;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEngine;
import de.uni_mannheim.informatik.dws.winter.matching.MatchingEvaluator;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.*;
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
    private Processable<Correspondence<Car, Attribute>> correspondences;
    private MatchingGoldStandard gsTest;
    private Performance perfTest;

    /**
     * Blockers to choose from:
     *
     */
    private NoBlocker<Car, Attribute> blocker;
    private StandardRecordBlocker<Car, Attribute> blockerSTD;
    private SortedNeighbourhoodBlocker blockerSN;
    private ValueBasedBlocker<Car, Attribute, Car> blockerValue;




    private void loadData() throws Exception {
        System.out.println("Working Directory = " +
                System.getProperty("user.dir"));
        // loading data
        System.out.println("*\n*\tLoading datasets\n*");
        dataset1 = new HashedDataSet<>();
        new CarXMLReader().loadFromXML(new File(
                "src/main/resources/data/offer_target_1.xml"),"/target/car", dataset1);

        // Take the first x samples from the offers dataset
        HashedDataSet<Car, Attribute> offers = new HashedDataSet<>();
        Car[] carOffers =dataset1.get().toArray(new Car[]{});
        for (int i = 0; i < 2500; i++) {
            offers.add(carOffers[i]);
        }


        dataset2 = new HashedDataSet<>();
        new CarXMLReader().loadFromXML(new File("src/main/resources/data/car_emissions_target.xml"), "/target/car", dataset2);

    }

    private void loadTrainingSet() throws Exception {
        gsTraining = new MatchingGoldStandard();
        gsTraining.loadFromCSVFile(new File("src/main/resources/goldStandard/train.csv"));
    }

    private void createMatchingRule(){
        
        matchingRule = new LinearCombinationMatchingRule<>(0.7);
        matchingRule.activateDebugReport("src/main/resources/output/debugResultsMatchingRule.csv", -1, gsTraining);
    }
    private void addComparators() throws Exception {
        matchingRule.addComparator(new CarManufacturerComparator_LowerCase(), 0.01);

    }

    private void createBlocker()   {
        //~~~~~USE SAME BLOCKER IN HERE AND ADD THE CORRECT ONE TO THE ME!!
        blockerSTD = new StandardRecordBlocker<>(new CarBlockingKeyByManufacturerGenerator());
        blockerSTD.setMeasureBlockSizes(true);
        //Write debug results to file:
        blockerSTD.collectBlockSizeData("src/main/resources/output/debugResultsBlocking.csv", 100);

    }

    private void initMachineEngine(){
        engine = new MatchingEngine<>();
    }

    private void executeMatching(){
        // Execute the matching
        System.out.println("*\n*\tRunning identity resolution\n*");

        correspondences = engine.runIdentityResolution(
                dataset1, dataset2, null, matchingRule,blockerSTD);
    }

    private void writeCorrespondences() throws Exception {
        // write the correspondences to the output file
        new CSVCorrespondenceFormatter().writeCSV(new File("src/main/resources/output/offers_emissions_correspondences.csv"), correspondences);
    }

    private void loadGoldStandard() throws Exception {
        // load the gold standard (test set)
        System.out.println("*\n*\tLoading gold standard\n*");
        gsTest = new MatchingGoldStandard();
        gsTest.loadFromCSVFile(new File("src/main/resources/goldStandard/test.csv"));
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
        System.out.println("Offers <-> Emissions");
        System.out.println(String.format(
                "Precision: %.4f",perfTest.getPrecision()));
        System.out.println(String.format(
                "Recall: %.4f",	perfTest.getRecall()));
        System.out.println(String.format(
                "F1: %.4f",perfTest.getF1()));
    }
	
    public static void main( String[] args ) throws Exception {
	    IR_using_linear_combination IR = new IR_using_linear_combination();

	    // loading data
		IR.loadData();

		// load the training set
		IR.loadTrainingSet();

		// create a matching rule
		IR.createMatchingRule();
		
		// add comparators
		IR.addComparators();

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
