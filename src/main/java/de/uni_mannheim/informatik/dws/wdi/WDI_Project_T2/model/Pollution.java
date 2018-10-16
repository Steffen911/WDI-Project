package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.io.Serializable;

/**
 * An AbstractRecord Representing a pollutant.
 *
 * Example Record:
 * <pollution>
 *     <pollutant>N02</pollutant>
 *     <air-quality>108.875</air-quality>
 *     <aq-unit>count</aq-unit>
 * </pollution>
 */
public class Pollution extends AbstractRecord<Attribute> implements Serializable {

    private String pollutant;
    private double airQuality;
    private String aqUnit;

    public Pollution(String identifier, String provenance) {
        super(identifier, provenance);
    }

    public static final Attribute POLLUTANT = new Attribute("pollutant");
    public static final Attribute AIR_QUALITY = new Attribute("air-quality");
    public static final Attribute AQ_UNIT = new Attribute("aq-unit");

    public String getPollutant() {
        return pollutant;
    }

    public void setPollutant(String pollutant) {
        this.pollutant = pollutant;
    }

    public double getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(double airQuality) {
        this.airQuality = airQuality;
    }

    public String getAqUnit() {
        return aqUnit;
    }

    public void setAqUnit(String aqUnit) {
        this.aqUnit = aqUnit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Pollution other = (Pollution) obj;
        return (!(pollutant == null &&
                other.pollutant != null &&
                !pollutant.equals(other.pollutant))
        );
    }

    @Override
    public boolean hasValue(Attribute attribute) {
        if (attribute == POLLUTANT)
            return pollutant != null;
        if (attribute == AQ_UNIT)
            return aqUnit != null;
        // TODO: Check if numerical attributes have to be checked
        return false;
    }
}
