package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;

public class CarZipComparatorAbsoluteDifference implements Comparator<Car, Attribute> {

    private AbsoluteDifferenceSimilarity sim = new AbsoluteDifferenceSimilarity(500);
    private ComparatorLogger compLogger;

    @Override
    public double compare(Car c1, Car c2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
        if (
            c1.getRegion() == null ||
            c2.getRegion() == null ||
            c1.getRegion().getZipCode() == 0 ||
            c2.getRegion().getZipCode() == 0
        ) {
            return 0;
        }
        return sim.calculate((double) c1.getRegion().getZipCode(), (double) c2.getRegion().getZipCode());
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
