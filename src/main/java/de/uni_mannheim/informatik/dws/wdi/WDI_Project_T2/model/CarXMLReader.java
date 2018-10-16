package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model;

import de.uni_mannheim.informatik.dws.winter.model.DataSet;
import org.w3c.dom.Node;

import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.model.io.XMLMatchableReader;

/** A XMLMatchableReader for Cars. */
public class CarXMLReader extends XMLMatchableReader<Car, Attribute> {

    @Override
    protected void initialiseDataset(DataSet<Car, Attribute> dataset) {
        super.initialiseDataset(dataset);
    }

    @Override
    public Car createModelFromElement(Node node, String provenance) {
        String id = getValueFromChildElement(node, "id");
        Car car = new Car(id, provenance);
        car.setModel(getValueFromChildElement(node, "model"));
        car.setManufacturer(getValueFromChildElement(node, "manufacturer"));

        // TODO: Read additional properties into object

        return car;
    }

}
