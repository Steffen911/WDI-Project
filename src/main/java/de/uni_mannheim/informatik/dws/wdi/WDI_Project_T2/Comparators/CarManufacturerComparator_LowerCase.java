package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.Comparators;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
//Where to find similarity measures
//import de.uni_mannheim.informatik.dws.winter.similarity.;
import de.uni_mannheim.informatik.dws.winter.similarity.string.GeneralisedStringJaccard;

public class CarManufacturerComparator_LowerCase implements Comparator<Car, Attribute> {
    private static final long serialVersionUID = 1L;

    private GeneralisedStringJaccard sim;
    private ComparatorLogger comparisonLog;

    @Override
    public double compare(Car record1,
                          Car record2,
                          Correspondence<Attribute, Matchable> schemaCorrespondence) {

        if (!record1.getManufacturer().toLowerCase().equals("sonstige_autos")
                && !record2.getManufacturer().toLowerCase().equals("sonstige_autos")){
            double similarity = sim.calculate(record1.getManufacturer().toLowerCase(),
                     record2.getManufacturer().toLowerCase());

        if(this.comparisonLog != null){
            this.comparisonLog.setComparatorName(getClass().getName());

            this.comparisonLog.setRecord1Value(record1.getManufacturer());
            this.comparisonLog.setRecord2Value(record2.getManufacturer());

            this.comparisonLog.setSimilarity(Double.toString(similarity));
        }
        return similarity;
        }
        return 0;
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
