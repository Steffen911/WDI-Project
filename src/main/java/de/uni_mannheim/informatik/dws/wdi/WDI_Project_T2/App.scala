package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2

import java.io.File

import org.apache.spark.sql.SparkSession
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.{Car, CarXMLReader}
import de.uni_mannheim.informatik.dws.winter.model.HashedDataSet
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity
import de.uni_mannheim.informatik.dws.winter.utils.WinterLogManager
import org.apache.logging.log4j.Logger
import org.apache.spark.mllib.evaluation.BinaryClassificationMetrics

object App {

  val logger: Logger = WinterLogManager.activateLogger("default")

  case class CarRow(id: String, manufacturer: String, model: String, fuelType: String,
                    transmission: String, horsePower: Double, emissions: Double)

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder
      .config("spark.master", "local[*]")
      .config("spark.executor.memory", "8g")
      .config("spark.driver.memory", "8g")
      .appName("IR_App")
      .getOrCreate()

    spark.sparkContext.setLogLevel("ERROR")

    import spark.implicits._

    // Load the datasets
    logger.info("Loading datasets...")
    val classloader = Thread.currentThread.getContextClassLoader
    val carEmissions = new HashedDataSet[Car, Attribute]
    logger.info("Loading car_emissions_target...")
    new CarXMLReader().loadFromXML(new File(classloader.getResource("data/car_emissions_target.xml").getFile), "/target/car", carEmissions)

    val offers = new HashedDataSet[Car, Attribute]
    logger.info("Loading offer_target 1 and 2...")
    new CarXMLReader().loadFromXML(new File(classloader.getResource("data/offer_target_1.xml").getFile), "/target/car", offers)
    new CarXMLReader().loadFromXML(new File(classloader.getResource("data/offer_target_2.xml").getFile), "/target/car", offers)

    val offerDf = offers.get().toArray(new Array[Car](0)).toList.map(convertCar)
      .toDF.as[CarRow]
      .sample(true, 0.3)
      .repartition(900)
    val carEmissionsDf = carEmissions.get().toArray(new Array[Car](0)).toList.map(convertCar)
      .toDF.as[CarRow]
      .repartition(900)

    logger.info(s"Offers has length ${offerDf.count()} and carEmissions has length ${carEmissionsDf.count()}")

    // Join dataframes on manufacturer - corresponds to blocking on manufacturer
    val joined = offerDf.as("l")
      .joinWith(carEmissionsDf.as("r"), $"l.manufacturer" === $"r.manufacturer")
      .map {
        case (left, right) => (left, right, distance(left, right))
      }
      .toDF("left", "right", "distance")
      .as[(CarRow, CarRow, Double)].cache
    logger.info(s"Successfully created joined dataset with length ${joined.count}")

    // Use Levenshtein similarity on manufacturer for linear matcher
    val filtered = joined.filter("distance > 0.9")
    logger.info(s"Before: ${joined.count} and after: ${filtered.count}")

    // Load the goldstandard and create predictions
    val gsTest = spark.read
      .format("csv")
      .option("timestampFormat", "yyyy/MM/dd")
      .load(classloader.getResource("goldstandard/test.csv").getFile)
      .map(row => (row.getString(0), row.getString(1), row.getString(2).toUpperCase == "TRUE"))
      .toDF("id1", "id2", "match")
      .as[(String, String, Boolean)]

    val prediction = joined.map(row => (row._1.id, row._2.id, row._3 > 0.9))
      .toDF("id1", "id2", "match")
      .as[(String, String, Boolean)]

    // Find all matches with the gold standard
    val predsAndLabels = prediction.as("l")
      .joinWith(gsTest.as("r"), $"l.id1" === $"r.id1" && $"l.id2" === $"r.id2")
      .map(matches => (b2d(matches._1._3), b2d(matches._2._3))).rdd

    logger.info(s"Found ${predsAndLabels.count()} matches with goldstandard")

    // Calculate prediction metrics
    val metrics = new BinaryClassificationMetrics(predsAndLabels)

    // Precision by threshold
    val precision = metrics.precisionByThreshold
    logger.info(s"Precision: ${precision.first()._2}")

    // Recall by threshold
    val recall = metrics.recallByThreshold
    logger.info(s"Recall: ${recall.first()._2}")

    // F-measure
    val f1Score = metrics.fMeasureByThreshold
    logger.info(s"F-score: ${f1Score.first()._2}")

  }

  def b2d(bool: Boolean): Double = if (bool) 1.0 else 0.0

  def convertCar(car: Car) = CarRow(
    car.getIdentifier,
    if (car.getManufacturer != null) car.getManufacturer.toLowerCase else "",
    car.getModel,
    car.getFuelType,
    car.getTransmission,
    car.getHorsePower,
    car.getEmission
  )

  def distance(c1: CarRow, c2: CarRow): Double = {
    val sim: LevenshteinSimilarity = new LevenshteinSimilarity
    var model = sim.calculate(c1.model, c2.model)
    var fuelType = sim.calculate(c1.fuelType, c2.fuelType)
    var transmission = sim.calculate(c1.transmission, c2.transmission)

    model = if (model > 0.5) model else 0
    fuelType = if (fuelType > 0.7) fuelType else 0
    transmission = if (transmission > 0.5) transmission else 0

    0.5 * model + 0.5 * fuelType + 0.5 * transmission
  }

}