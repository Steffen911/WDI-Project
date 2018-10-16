package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model;

import de.uni_mannheim.informatik.dws.winter.model.Matchable;

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
public class Car implements Matchable {

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

    public Car(String identifier, String provenance) {
        this.identifier = identifier;
        this.provenance = provenance;
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
}
