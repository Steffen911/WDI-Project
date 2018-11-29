package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.evaluation;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.datafusion.EvaluationRule;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class StationIdEvaluationRule extends EvaluationRule<Car, Attribute> {

    private SimilarityMeasure<String> sim = new LevenshteinSimilarity();

    @Override
    public boolean isEqual(Car c1, Car c2, Attribute elem) {
        return sim.calculate(c1.getRegion().getStationId(), c2.getRegion().getStationId()) > 0.7;
    }

    @Override
    public boolean isEqual(Car c1, Car c2, Correspondence<Attribute, Matchable> correspondence) {
        return isEqual(c1, c2, (Attribute) null);
    }

}
