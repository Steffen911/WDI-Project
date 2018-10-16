package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;

import java.io.Serializable;

/**
 * An AbstractRecord Representing a region.
 *
 * Example Record:
 * <region>
 *     <stationId>std_e1wewdsa</stationId>
 *     <latitude>54.555555</latitude>
 *     <longitude>54.555555</longitude>
 *     <zip-code>68159</zip-code>
 *     <city>Mannheim</city>
 * </region>
 */
public class Region extends AbstractRecord<Attribute> implements Serializable {

    private String stationId;
    private double latitude;
    private double longitude;
    private int zipCode;
    private String city;

    public Region(String identifier, String provenance) {
        super(identifier, provenance);
    }

    public static final Attribute STATION_ID = new Attribute("stationId");
    public static final Attribute LATITUDE = new Attribute("latitude");
    public static final Attribute LONGITUDE = new Attribute("longitude");
    public static final Attribute ZIP_CODE = new Attribute("zip-code");
    public static final Attribute CITY = new Attribute("city");

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
        Region other = (Region) obj;
        return (!(stationId == null &&
                other.stationId != null &&
                !stationId.equals(other.stationId))
        );
    }

    @Override
    public boolean hasValue(Attribute attribute) {
        if (attribute == STATION_ID)
            return stationId != null;
        if (attribute == CITY)
            return city != null;
        // TODO: Check if numerical attributes have to be checked
        return false;
    }
}
