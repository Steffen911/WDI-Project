package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import java.util.Arrays;
import java.util.List;

public class CarBlocking_Manufacturer extends RecordBlockingKeyGenerator<Car, Attribute> {
    private static final long serialVersionUID = 1L;
    private List<String> list = Arrays.asList("volkswagen", "audi", "jeep", "volkswagen", "skoda", "bmw", "peugeot", "ford", "mazda", "nissan",
            "renault", "mercedes_benz", "opel", "seat", "citroen", "honda", "fiat", "mini", "smart","hyundai", "alfa_romeo", //           subaru,
            "volvo", "mitsubishi", "kia", "suzuki", "lancia", "porsche", "toyota", "chevrolet", "dacia", "daihatsu", "chrysler", "jaguar",
            "daewoo", "rover", "land_rover" );

    @Override
    public void generateBlockingKeys(
            Car carInstance, Processable<Correspondence<Attribute,
            Matchable>> correspondences, DataIterator<Pair<String, Car>> resultCollector) {


        String nameClean =carInstance.getManufacturer().toLowerCase().replace("_", " "). replace("-", " ").replaceAll("\\s{2,}", " ").trim();


            resultCollector.next(
                    new Pair<>(
                            nameClean
                            ,
                            carInstance
                    )
            );




    }

}
