package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class CarStationComparatorLevenshtein implements Comparator<Car, Attribute> {

    private LevenshteinSimilarity sim = new LevenshteinSimilarity();
    private ComparatorLogger compLogger;

    @Override
    public double compare(Car c1, Car c2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
        if (
            c1.getRegion() == null ||
            c2.getRegion() == null ||
            c1.getRegion().getStationId() == null ||
            c2.getRegion().getStationId() == null
        ) {
            return 0;
        }
        return sim.calculate(c1.getRegion().getStationId(), c2.getRegion().getStationId());
    }

    @Override
    public ComparatorLogger getComparisonLog() {
        return this.compLogger;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.compLogger = comparatorLog;
    }

}
