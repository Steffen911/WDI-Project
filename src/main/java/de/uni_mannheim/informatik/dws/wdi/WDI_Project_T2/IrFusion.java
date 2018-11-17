package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarXMLReader;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import Fusion.FusibleCarFactory;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.datafusion.CorrespondenceSet;
import de.uni_mannheim.informatik.dws.winter.datafusion.DataFusionStrategy;
import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleDataSet;
import de.uni_mannheim.informatik.dws.winter.model.FusibleHashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet;
import de.uni_mannheim.informatik.dws.winter.model.MatchingGoldStandard;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

public class IrFusion {

	public static void main(String[] args) throws Exception{
		
		// Load the Data into FusibleDataSet
		System.out.println("*\n*\tLoading datasets\n*");
	
		FusibleDataSet<Car, Attribute> ds1 = new FusibleHashedDataSet<>();
		new CarXMLReader().loadFromXML(new File("data/car_emissions_dupfree.xml"), "/target/car", ds1);
		ds1.printDataSetDensityReport();
		
		FusibleDataSet<Car, Attribute> ds2 = new FusibleHashedDataSet<>();
		new CarXMLReader().loadFromXML(new File("data/offers_dupfree.xml"), "/target/car", ds2);
		
		FusibleDataSet<Car, Attribute> ds3 = new FusibleHashedDataSet<>();
		new CarXMLReader().loadFromXML(new File("data/station_target.xml"), "/target/car", ds3);
		
		FusibleDataSet<Car, Attribute> ds4 = new FusibleHashedDataSet<>();
		new CarXMLReader().loadFromXML(new File("data/vehicles_dupfree.xml"), "/target/car", ds4);

		
		// Maintain Provenance
		// Scores (e.g. from rating)
		ds1.setScore(10);
		ds2.setScore(4);
		ds3.setScore(3);
		ds4.setScore(1);
		
		// Date (e.g. last update)
		DateTimeFormatter formatter = new DateTimeFormatterBuilder()
		        .appendPattern("yyyy-MM-dd")
		        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
		        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
		        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
		        .toFormatter(Locale.ENGLISH);
		
		ds1.setDate(LocalDateTime.parse("2012-01-01", formatter));
		ds2.setDate(LocalDateTime.parse("2010-01-01", formatter));
		ds3.setDate(LocalDateTime.parse("2008-01-01", formatter));
		ds3.setDate(LocalDateTime.parse("2009-01-01", formatter));
		
		// load correspondences
		System.out.println("*\n*\tLoading correspondences\n*");
		CorrespondenceSet<Car, Attribute> correspondences = new CorrespondenceSet<>();
		correspondences.loadCorrespondences(new File("data/output/"),ds1, ds2);
		
		// write group size distribution
		correspondences.printGroupSizeDistribution();
		
		// load the gold standard
		System.out.println("*\n*\tEvaluating results\n*");
		DataSet<Car, Attribute> gs = new FusibleHashedDataSet<>();
		new CarXMLReader().loadFromXML(new File("goldstandard/test.csv"), "Was muss hier rein ??", gs);
		
		for(Car c : gs.get()) {
			System.out.println(String.format("gs: %s", c.getIdentifier()));
		}
		
		//Fusion strategy definition
		DataFusionStrategy<Car, Attribute> strategy = new DataFusionStrategy<>(new FusibleCarFactory());
		//Write debug result to file
		strategy.activateDebugReport("data/output/debugResultsDatafusion.csv", -1, gs);
		
		// add attribute fusers -- We're adding the fusion strategy
	

		
		
		
		
		
		
		
		
		

	}

}
