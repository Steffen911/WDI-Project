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
        car.appendChild(createTextElement("manufacturer", c.getManufacturer(), doc));
        car.appendChild(createTextElement("model", c.getModel(), doc));
        car.appendChild(createTextElement("fuelType", c.getFuelType(), doc));
        car.appendChild(createTextElement("transmission", c.getTransmission(), doc));
        car.appendChild(createTextElement("horse-power", "" + c.getHorsePower(), doc));

        Element region = doc.createElement("region");
        region.appendChild(createTextElement("zip-code", "" + c.getRegion().getZipCode(), doc));
        region.appendChild(createTextElement("stationId", c.getRegion().getStationId(), doc));
        region.appendChild(createTextElement("latitude", "" + c.getRegion().getLatitude(), doc));
        region.appendChild(createTextElement("longitude", "" + c.getRegion().getLongitude(), doc));
        region.appendChild(createTextElement("city", c.getRegion().getCity(), doc));
        car.appendChild(region);

        car.appendChild(createTextElement("mileage", "" + c.getMileage(), doc));

        Element pollution = doc.createElement("pollution");
        pollution.appendChild(createTextElement("pollutant", c.getPollution().getPollutant(), doc));
        pollution.appendChild(createTextElement("air-quality", "" + c.getPollution().getAirQuality(), doc));
        pollution.appendChild(createTextElement("aq-unit", c.getPollution().getAqUnit(), doc));
        car.appendChild(pollution);

        return car;
    }

}
