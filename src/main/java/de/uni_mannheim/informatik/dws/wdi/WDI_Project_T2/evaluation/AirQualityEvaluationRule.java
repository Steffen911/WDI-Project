package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.evaluation;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;

public class AirQualityEvaluationRule extends EvaluationRule<Car, Attribute> {

    private AbsoluteDifferenceSimilarity sim = new AbsoluteDifferenceSimilarity(5);

    @Override
    public boolean isEqual(Car c1, Car c2, Attribute elem) {
        if (
            c1.getPollution() == null ||
            c2.getPollution() == null ||
            c1.getPollution().getAirQuality() == 0 ||
            c2.getPollution().getAirQuality() == 0
        ) {
            return false;
        }
        return sim.calculate(c1.getPollution().getAirQuality(), c2.getPollution().getAirQuality()) > 0.8;
    }

    @Override
    public boolean isEqual(Car c1, Car c2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(c1, c2, (Attribute) null);
    }
}
