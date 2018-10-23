package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.Blocking;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.blockers.generators.RecordBlockingKeyGenerator;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.Pair;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.DataIterator;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class CarBlocking_XY extends RecordBlockingKeyGenerator<Car, Attribute> {
    private static final long serialVersionUID = 1L;


    @Override
    public void generateBlockingKeys(Car car, Processable<Correspondence<Attribute, Matchable>> processable, DataIterator<Pair<String, Car>> dataIterator) {
        //TODO

    }
}
