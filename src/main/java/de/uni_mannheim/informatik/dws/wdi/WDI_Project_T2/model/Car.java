package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

/**
 * An AbstractRecord Representing a car.
 *
 * Example Record:
 * <car id="c1">
 * 		<manufacturer>volkswagen</manufacturer>
 * 		<model>golf</model>
 * 		<fuelType>diesel</fuelType>
 * 		<transmission>m6</transmission>
 * 		<horse-power>130</horse-power>
 * 		<region>
 * 			<stationId>std_e1wewdsa</stationId>
 * 			<latitude>54.555555</latitude>
 * 			<longitude>54.555555</longitude>
 * 			<zip-code>68159</zip-code>
 * 			<city>Mannheim</city>
 * 		</region>
 * 		<emissions>82</emissions>
 * 		<mileage>150.000</mileage>
 * 		<pollution>
 * 			<pollutant>N02</pollutant>
 * 			<air-quality>108.875</air-quality>
 * 			<aq-unit>count</aq-unit>
 * 		</pollution>
 * 	</car>
 */
public class Car extends AbstractRecord<Attribute> {

    // Mandatory properties of Matchable
    private String identifier;
    private String provenance;

    // Properties of a car
    private String manufacturer;
    private String model;
    private String fuelType;
    private String transmission;
    private int horsePower;
    private Region region;
    private double emission;
    private double mileage;
    private Pollution pollution;

    public static final Attribute MANUFACTURER = new Attribute("Manufacturer");
    public static final Attribute MODEL = new Attribute("Model");
    public static final Attribute FUEL_TYPE = new Attribute("FuelType");
    public static final Attribute TRANSMISSION = new Attribute("Transmission");
    public static final Attribute HORSE_POWER = new Attribute("HorsePower");
    public static final Attribute ZIP_CODE = new Attribute("ZipCode");
    public static final Attribute STATIOND_ID = new Attribute("StationId");
    public static final Attribute LATITUDE = new Attribute("Latitude");
    public static final Attribute LONGITUDE = new Attribute("Longitude");
    public static final Attribute CITY = new Attribute("City");
    public static final Attribute EMISSION = new Attribute("Emission");
    public static final Attribute MILEAGE = new Attribute("Mileage");

    public Car(String identifier, String provenance) {
        this.identifier = identifier;
        this.provenance = provenance;
        this.region = new Region(identifier, provenance);
        this.pollution = new Pollution(identifier, provenance);
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getProvenance() {
        return provenance;
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public double getEmission() {
        return emission;
    }

    public void setEmission(double emission) {
        this.emission = emission;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public Pollution getPollution() {
        return pollution;
    }

    public void setPollution(Pollution pollution) {
        this.pollution = pollution;
    }

    @Override
    public boolean hasValue(Attribute attribute) {
        if (attribute == MANUFACTURER)
            return getManufacturer() != null && !getManufacturer().isEmpty();
        if (attribute == MODEL)
            return getModel() != null && !getModel().isEmpty();
        if (attribute == FUEL_TYPE)
            return getFuelType() != null && !getFuelType().isEmpty();
        if (attribute == TRANSMISSION)
            return getTransmission() != null && !getTransmission().isEmpty();
        if (attribute == EMISSION)
            return getEmission() > 0;
        if (attribute == HORSE_POWER)
            return getHorsePower() > 0;
        if (attribute == MILEAGE)
            return getMileage() > 0;
        if (attribute == ZIP_CODE)
            return getRegion() != null && getRegion().getZipCode() > 0;
        if (attribute == STATIOND_ID)
            return getRegion() != null && getRegion().getStationId() != null;
        if (attribute == LATITUDE)
            return getRegion() != null && getRegion().getLatitude() > 0;
        if (attribute == LONGITUDE)
            return getRegion() != null && getRegion().getLongitude() > 0;
        if (attribute == CITY)
            return getRegion() != null && getRegion().getCity() != null;
        return false;
    }

}
