package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;

public class CarBlockingKeyByManufacturerGenerator extends RecordBlockingKeyGenerator<Car, Attribute> {

    private TokenizingJaccardSimilarity sim = new TokenizingJaccardSimilarity();
    private String[] manufacturers = {
            "audi",
            "bmw",
            "fiat",
            "ford",
            "honda",
            "hyundai",
            "jeep",
            "mazda",
            "mercedes_benz",
            "mini",
            "nissan",
            "opel",
            "peugeot",
            "renault",
            "seat",
            "skoda",
            "smart",
            "volkswagen",
            "citroen",
            "alfa_romeo",
            "volvo",
            "mitsubishi",
            "kia",
            "suzuki",
            "lancia",
            "porsche",
            "toyota",
            "chevrolet",
            "dacia",
            "daihatsu",
            "chrysler",
            "jaguar",
            "daewoo",
            "rover",
            "land_rover"
    };

    @Override
    public void generateBlockingKeys(
            Car car,
            Processable<Correspondence<Attribute, Matchable>> correspondences,
            DataIterator<Pair<String, Car>> resultCollector
    ) {
        int key = getIdForManufacturer(car.getManufacturer());
        if (key < 0) {
            // ignore manufacturers that are not whitelisted
            return;
        }
        resultCollector.next(new Pair<>(Integer.toString(key), car));
    }

    private int getIdForManufacturer(String manufacturer) {
        manufacturer = manufacturer.toLowerCase();
        for (int i = 0; i < manufacturers.length; i++) {
            if (manufacturers[i].equals("opel") && manufacturer.equals("vauxhall")) {
                return i;
            }
            double similarity = sim.calculate(manufacturers[i], manufacturer);
            if (similarity > 0.3) {
                return i;
            }
        }
        return -1;
    }
}
