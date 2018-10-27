package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class Car_FuelType_Levenshtein_Comparator implements Comparator<Car, Attribute> {

    private static final long serialVersionUID = 1L;

    private LevenshteinSimilarity sim = new LevenshteinSimilarity();
    private ComparatorLogger comparisonLog;

    @Override
    public double compare(Car record1, Car record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

        if (record1.getFuelType() != null && record2.getFuelType() != null) {
            // Translate, Diesel remains the same, Benzin needs translation, Other ignored since there
            // are not that many in our offer dataset? //TODO-team
            String fuelType1 = record1.getFuelType().trim().toLowerCase();
            if (fuelType1.equals("benzin")) {
                fuelType1 = "petrol";
            }
            String fuelType2 = record2.getFuelType().trim().toLowerCase();
            if (fuelType2.equals("benzin")) {
                fuelType2 = "petrol";
            }

            //get very harsh Similarity measure  - exact match
            double similarity = sim.calculate(fuelType1, fuelType2);

            if (this.comparisonLog != null) {
                this.comparisonLog.setComparatorName(getClass().getName());

                this.comparisonLog.setRecord1Value(fuelType1);
                this.comparisonLog.setRecord2Value(fuelType2);

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
