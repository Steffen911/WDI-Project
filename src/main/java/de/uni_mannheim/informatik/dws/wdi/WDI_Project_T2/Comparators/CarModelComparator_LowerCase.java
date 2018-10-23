package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.Comparators;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
//Where to find similarity measures
import de.uni_mannheim.informatik.dws.winter.similarity.*;

public class CarModelComparator_LowerCase implements Comparator<Car, Attribute> {
    private static final long serialVersionUID = 1L;


    private ComparatorLogger comparisonLog;

    @Override
    public double compare(Car car,
                          Car recordType1,
                          Correspondence<Attribute, Matchable> correspondence) {
        return 0; //TODO
    }
    @Override
    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }
}
