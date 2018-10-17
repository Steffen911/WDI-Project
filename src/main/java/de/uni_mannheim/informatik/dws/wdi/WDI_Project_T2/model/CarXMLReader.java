package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** A XMLMatchableReader for Cars. */
public class CarXMLReader extends XMLMatchableReader<Car, Attribute> {

    /** Create a new car from the given xml node. */
    @Override
    public Car createModelFromElement(Node node, String provenance) {
        Element elem = (Element) node;
        String id = elem.getAttribute("id");

        Car car = new Car(id, provenance);
        car.setModel(getValueFromChildElement(node, "model"));
        car.setManufacturer(getValueFromChildElement(node, "manufacturer"));
        car.setFuelType(getValueFromChildElement(node, "fuelType"));
        car.setTransmission(getValueFromChildElement(node, "transmission"));

        int horsePower;
        try {
            horsePower = Integer.valueOf(getValueFromChildElement(node, "horse-power"));
        } catch (Exception e) {
            horsePower = 0;
        }
        car.setHorsePower(horsePower);

        double emissions;
        try {
            emissions = Double.valueOf(getValueFromChildElement(node, "emissions"));
        } catch (Exception e) {
            emissions = 0.0;
        }
        car.setEmission(emissions);

        double mileage;
        try {
            mileage = Double.valueOf(getValueFromChildElement(node, "mileage"));
        } catch (Exception e) {
            mileage = 0.0;
        }
        car.setMileage(mileage);

        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeName().equals("pollution")) {
                car.setPollution(getPollution(child, id, provenance));
            }
            if (child.getNodeName().equals("region")) {
                car.setRegion(getRegion(child, id, provenance));
            }
        }

        return car;
    }

    /** getPollution takes a pollution xml node and extracts the specific values. */
    private Pollution getPollution(Node node, String id, String provenance) {
        Pollution pollution = new Pollution(id, provenance);
        pollution.setPollutant(getValueFromChildElement(node, "pollutant"));
        pollution.setAqUnit(getValueFromChildElement(node, "aq-unit"));

        double aq;
        try {
            aq = Double.valueOf(getValueFromChildElement(node, "air-quality"));
        } catch (Exception e) {
            aq = 0.0;
        }
        pollution.setAirQuality(aq);
        return pollution;
    }

    /** getRegion takes a region xml node and extracts the specific values. */
    private Region getRegion(Node node, String id, String provenance) {
        Region region = new Region(id, provenance);
        region.setStationId(getValueFromChildElement(node, "stationId"));
        region.setCity(getValueFromChildElement(node, "city"));

        // Latitude and Longitude make only sense together. So either both compile or none
        double latitude;
        double longitude;
        try {
            latitude = Double.valueOf(getValueFromChildElement(node, "latitude"));
            longitude = Double.valueOf(getValueFromChildElement(node, "longitude"));
        } catch (Exception e) {
            latitude = 0;
            longitude = 0;
        }
        region.setLatitude(latitude);
        region.setLongitude(longitude);

        int zipCode;
        try {
            zipCode = Integer.valueOf(getValueFromChildElement(node, "zip-code"));
        } catch (Exception e) {
            zipCode = 0;
        }
        region.setZipCode(zipCode);

        return region;
    }

}
