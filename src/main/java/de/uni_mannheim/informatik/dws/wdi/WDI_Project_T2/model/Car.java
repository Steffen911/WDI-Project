package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
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
	
	
    public Car(String identifier, String provenance) {
        super(identifier, provenance);
        cars = new LinkedList<>();
    }

    // Mandatory properties of Matchable
  
   

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
    private List<Car> cars;
    




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
    
	private Map<Attribute, Collection<String>> provenance = new HashMap<>();
	private Collection<String> recordProvenance;

	public void setRecordProvenance(Collection<String> provenance) {
		recordProvenance = provenance;
	}

	public Collection<String> getRecordProvenance() {
		return recordProvenance;
	}
	
	public void setAttributeProvenance(Attribute attribute,
			Collection<String> provenance) {
		this.provenance.put(attribute, provenance);
		
	}
	
	public Collection<String> getAttributeProvenance(String attribute) {
		return provenance.get(attribute);
	}
	
	public String getMergedAttributeProvenance(Attribute attribute) {
		Collection<String> prov = provenance.get(attribute);
		if (prov != null) {
			return StringUtils.join(prov, "+");
		} else {
			return "";
		}
	}
	
	public static final Attribute MANUFACTURER = new Attribute("MANUFACTURER");
	public static final Attribute MODEL = new Attribute("MODEL");
	public static final Attribute FUELTYPE = new Attribute("FUELTYPE");
	public static final Attribute TRANSMISSION = new Attribute("TRANSMISSION");
	

	@Override
	public boolean hasValue(Attribute attribute) {
		if(attribute==MANUFACTURER)
			return getManufacturer() != null && !getManufacturer().isEmpty();
		else if(attribute==MODEL)
			return getModel() != null && !getModel().isEmpty();
		else if(attribute==FUELTYPE)
			return getFuelType() != null;
		else if(attribute==TRANSMISSION)
			return getTransmission() != null && !(getTransmission().isEmpty());
		else
			return false;
	}
	

	


}
