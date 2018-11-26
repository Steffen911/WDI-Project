package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class CarBlockingKeyByZipGenerator extends RecordBlockingKeyGenerator<Car, Attribute> {

    @Override
    public void generateBlockingKeys(
            Car car,
            Processable<Correspondence<Attribute, Matchable>> correspondences,
            DataIterator<Pair<String, Car>> resultCollector
    ) {
        if (car.getRegion() == null || car.getRegion().getZipCode() == 0) {
            return;
        }
        int zip = car.getRegion().getZipCode();
        String firstDigit = String.valueOf(String.valueOf(zip).charAt(0));
        resultCollector.next(new Pair<>(firstDigit, car));
    }

}
