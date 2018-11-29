package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model;

import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * <car id="offer_66">
 * 		<manufacturer>bmw</manufacturer>
 * 		<model>BMW_325_i_Cabrio_wenig_Kilometer 3er</model>
 * 		<fuelType>benzin</fuelType>
 * 		<transmission>manuell</transmission>
 * 		<horse-power>218</horse-power>
 * 		<region>
 * 			<zip-code>1129</zip-code>
 * 		    <stationId>STA.DE_DEBW223</stationId>
 * 		    <latitude>48.68996</latitude>
 *          <longitude>9.16726</longitude>
 *          <city>Leinfelden-Echterdingen</city>
 * 		</region>
 * 		<mileage>50000</mileage>
 * 	    <pollution>
 * 	        <pollutant>Nitrogen dioxide (air)</pollutant>
 * 	        <air-quality>46.503863</air-quality>
 *          <aq-unit>ug.m-3</aq-unit>
 * 	    </pollution>
 * 	</car>
 */
public class CarXMLFormatter extends XMLFormatter<Car> {

    @Override
    public Element createRootElement(Document doc) {
        return doc.createElement("target");
    }

    @Override
    public Element createElementFromRecord(Car c, Document doc) {
        Element car = doc.createElement("car");
        car.appendChild(createTextElementWithProvenance("manufacturer", c.getManufacturer(), c.getMergedAttributeProvenance(Car.MANUFACTURER), doc));
        car.appendChild(createTextElementWithProvenance("model", c.getModel(), c.getMergedAttributeProvenance(Car.MODEL), doc));
        car.appendChild(createTextElementWithProvenance("fuelType", c.getFuelType(), c.getMergedAttributeProvenance(Car.FUEL_TYPE), doc));
        car.appendChild(createTextElementWithProvenance("transmission", c.getTransmission(), c.getMergedAttributeProvenance(Car.TRANSMISSION), doc));
        car.appendChild(createTextElementWithProvenance("horse-power", "" + c.getHorsePower(), c.getMergedAttributeProvenance(Car.HORSE_POWER), doc));

        Element region = doc.createElement("region");
        region.appendChild(createTextElementWithProvenance("zip-code", "" + c.getRegion().getZipCode(), c.getMergedAttributeProvenance(Car.ZIP_CODE), doc));
        region.appendChild(createTextElementWithProvenance("stationId", c.getRegion().getStationId(), c.getMergedAttributeProvenance(Car.STATIOND_ID), doc));
        region.appendChild(createTextElementWithProvenance("latitude", "" + c.getRegion().getLatitude(), c.getMergedAttributeProvenance(Car.LATITUDE), doc));
        region.appendChild(createTextElementWithProvenance("longitude", "" + c.getRegion().getLongitude(), c.getMergedAttributeProvenance(Car.LONGITUDE), doc));
        region.appendChild(createTextElementWithProvenance("city", c.getRegion().getCity(), c.getMergedAttributeProvenance(Car.CITY), doc));
        car.appendChild(region);

        car.appendChild(createTextElement("mileage", "" + c.getMileage(), doc));

        Element pollution = doc.createElement("pollution");
        pollution.appendChild(createTextElementWithProvenance("pollutant", c.getPollution().getPollutant(), c.getMergedAttributeProvenance(Car.POLLUTANT), doc));
        pollution.appendChild(createTextElementWithProvenance("air-quality", "" + c.getPollution().getAirQuality(), c.getMergedAttributeProvenance(Car.AIR_QUALITY), doc));
        pollution.appendChild(createTextElementWithProvenance("aq-unit", c.getPollution().getAqUnit(), c.getMergedAttributeProvenance(Car.AQ_UNIT), doc));
        car.appendChild(pollution);

        return car;
    }

    private Element createTextElementWithProvenance(String name, String value, String provenance, Document doc) {
        Element elem = createTextElement(name, value, doc);
        elem.setAttribute("provenance", provenance);
        return elem;
    }

}
