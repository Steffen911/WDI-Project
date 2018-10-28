package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util.CompareUtil;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.MaximumOfTokenContainment;

public class CarModelComparatorMaximumTokenContainment implements Comparator<Car, Attribute> {

    private MaximumOfTokenContainment sim = new MaximumOfTokenContainment();
    private ComparatorLogger compLogger;

    @Override
    public double compare(Car c1, Car c2, Correspondence<Attribute, Matchable> schemaCorrespondence) {
        return CompareUtil.compareCarModel(c1, c2, getClass().getName(), sim, this.compLogger);
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

