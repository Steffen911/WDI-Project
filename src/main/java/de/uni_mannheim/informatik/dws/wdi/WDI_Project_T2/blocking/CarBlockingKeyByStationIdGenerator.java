package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.blocking;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class CarBlockingKeyByStationIdGenerator extends RecordBlockingKeyGenerator<Car, Attribute> {

    @Override
    public void generateBlockingKeys(
            Car car,
            Processable<Correspondence<Attribute, Matchable>> correspondences,
            DataIterator<Pair<String, Car>> resultCollector
    ) {
        if (car.getRegion() == null || car.getRegion().getStationId() == null) {
            return;
        }
        // TODO: Check if this is necessary
        if (car.getPollution() != null &&
            car.getPollution().getPollutant() != null &&
            !car.getPollution().getPollutant().equals("Nitrogen dioxide (air)")
        ) {
            return;
        }
        resultCollector.next(new Pair<>(car.getRegion().getStationId(), car));
    }

}
