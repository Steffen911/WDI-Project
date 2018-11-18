package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


import de.uni_mannheim.informatik.dws.winter.model.io.XMLFormatter;

public class CarXMLFormatter extends XMLFormatter<Car>{

	@Override
	public Element createRootElement(Document doc) {
		// TODO Auto-generated method stub
		return doc.createElement("car");
	}

	@Override
	public Element createElementFromRecord(Car record, Document doc) {
		Element car = doc.createElement("car");
		car.appendChild(createTextElement("id", record.getIdentifier(), doc));
		
		car.appendChild(createTextElementWithProvenance("manufacturer",
				record.getManufacturer(),
				record.getMergedAttributeProvenance(Car.MANUFACTURER), doc));
		car.appendChild(createTextElementWithProvenance("model",
				record.getModel(),
				record.getMergedAttributeProvenance(Car.MODEL), doc));
		car.appendChild(createTextElementWithProvenance("transmission", 
				record.getTransmission().toString(), 
				record.getMergedAttributeProvenance(Car.TRANSMISSION), doc));
		car.appendChild(createTextElementWithProvenance("fueltype", 
				record.getTransmission().toString(), 
				record.getMergedAttributeProvenance(Car.FUELTYPE), doc));
		
				

	

		return car;
	}

	protected Element createTextElementWithProvenance(String name,
			String value, String provenance, Document doc) {
		Element elem = createTextElement(name, value, doc);
		elem.setAttribute("provenance", provenance);
		return elem;
	}
	
	
	
}
