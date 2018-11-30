package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.evaluation.*;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.fusers.*;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLFormatter;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLReader;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.FusibleCarFactory;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEngine;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionEvaluator;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class DF_App {

    public static final Logger logger = WinterLogManager.activateLogger("default");

    public static void main(String[] args) throws Exception {

        // Load the datasets
        logger.info("Loading datasets...");
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        FusibleHashedDataSet<Car, Attribute> carEmissions = DF_App.loadData("car_emissions_dupfree");
        FusibleHashedDataSet<Car, Attribute> offerInt = DF_App.loadData("offers_dupfree");
        FusibleHashedDataSet<Car, Attribute> stations = DF_App.loadData("station_target");
        FusibleHashedDataSet<Car, Attribute> regionEmissions = DF_App.loadData("region_emissions_target");
        FusibleHashedDataSet<Car, Attribute> vehicles = DF_App.loadData("vehicles_dupfree");

        logger.info("Successfully loaded data sets");

        // Add provenance scores
        carEmissions.setScore(3.0);
        offerInt.setScore(1.0);
        stations.setScore(3.0);
        regionEmissions.setScore(3.0);
        vehicles.setScore(3.0);

        // Load correspondences
        logger.info("Loading correspondences...");
        CorrespondenceSet<Car, Attribute> correspondences = new CorrespondenceSet<>();
        correspondences.loadCorrespondences(new File("data/output/offers-caremissions.csv"), offerInt, carEmissions);
        correspondences.loadCorrespondences(new File("data/output/offers-vehicles.csv"), offerInt, vehicles);
        correspondences.loadCorrespondences(new File("data/output/vehicles-caremissions.csv"), vehicles, carEmissions);
        correspondences.loadCorrespondences(new File("data/output/offers-stations.csv"), offerInt, stations);
        correspondences.loadCorrespondences(new File("data/output/stations-regionEmissions.csv"), stations, regionEmissions);

        correspondences.printGroupSizeDistribution();

        DataSet<Car, Attribute> gs = new FusibleHashedDataSet<>();
        new CarXMLReader().loadFromXML(new File(classloader.getResource("goldstandard/fusion.xml").getFile()), "/target/car", gs);

        logger.info("Definining the fusion strategy...");
        DataFusionStrategy<Car, Attribute> strategy = new DataFusionStrategy<>(new FusibleCarFactory());
        strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);

        logger.info("Adding the attribute fusers...");
        strategy.addAttributeFuser(Car.MANUFACTURER, new ManufacturerFuserLongestString(), new ManufacturerEvaluationRule());
        strategy.addAttributeFuser(Car.MODEL, new ModelFuserWhiteList(), new ModelEvaluationRule());
        strategy.addAttributeFuser(Car.FUEL_TYPE, new FuelTypeFuserFavorSources(), new FuelTypeEvaluationRule());
        strategy.addAttributeFuser(Car.TRANSMISSION, new TransmissionFuserVotingString(), new TransmissionEvaluationRule());
        strategy.addAttributeFuser(Car.HORSE_POWER, new HorsePowerFuserAvg(), new HorsePowerEvaluationRule());
        strategy.addAttributeFuser(Car.ZIP_CODE, new ZipCodeFuserMedian(), new ZipCodeEvaluationRule());
        strategy.addAttributeFuser(Car.STATIOND_ID, new StationIdFuserLongestString(), new StationIdEvaluationRule());
        strategy.addAttributeFuser(Car.LATITUDE, new LatitudeFuserMedian(), new LatitudeEvaluationRule());
        strategy.addAttributeFuser(Car.LONGITUDE, new LongitudeFuserMedian(), new LongitudeEvaluationRule());
        strategy.addAttributeFuser(Car.CITY, new CityFuserLongestString(), new CityEvaluationRule());
        strategy.addAttributeFuser(Car.EMISSION, new EmissionFuserAvg(), new EmissionEvaluationRule());
        strategy.addAttributeFuser(Car.POLLUTANT, new PollutantFuserLongestString(), new PollutantEvaluationRule());
        strategy.addAttributeFuser(Car.AIR_QUALITY, new AirQualityFuserMedian(), new AirQualityEvaluationRule());
        strategy.addAttributeFuser(Car.AQ_UNIT, new AqUnitFuserLongestString(), new AqUnitEvaluationRule());
        strategy.addAttributeFuser(Car.MILEAGE, new MileageFuserMedian(), new MileageEvaluationRule());

        logger.info("Starting the fusion...");
        DataFusionEngine<Car, Attribute> engine = new DataFusionEngine<>(strategy);
        engine.printClusterConsistencyReport(correspondences, null);
        engine.writeRecordGroupsByConsistency(new File("data/output/recordGroupConsistencies.csv"), correspondences, null);

        logger.info("Running the data fusion...");
        FusibleDataSet<Car, Attribute> fusedDataset = engine.run(correspondences, null);
        fusedDataset.printDataSetDensityReport();

        new CarXMLFormatter().writeXML(new File("data/output/fused.xml"), fusedDataset);
        logger.info("Successfully wrote fused.xml output");

        logger.info("Evaluating the fusion:");
        DataFusionEvaluator<Car, Attribute> evaluator = new DataFusionEvaluator<>(strategy);
        double accuracy = evaluator.evaluate(fusedDataset, gs, null);
        logger.info(String.format("Got an accuracy score of %.2f", accuracy));

    }

    private static FusibleHashedDataSet<Car, Attribute> loadData(String fileName) throws Exception {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        FusibleHashedDataSet<Car, Attribute> ds = new FusibleHashedDataSet<>();
        logger.info("Loading " + fileName + "...");
        new CarXMLReader().loadFromXML(new File(classloader.getResource("data/" + fileName + ".xml").getFile()), "/target/car", ds);
        return ds;
    }

}
