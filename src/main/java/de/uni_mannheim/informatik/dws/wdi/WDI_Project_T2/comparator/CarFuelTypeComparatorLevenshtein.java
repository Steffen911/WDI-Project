package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class CarFuelTypeComparatorLevenshtein implements Comparator<Car, Attribute> {

    private LevenshteinSimilarity sim = new LevenshteinSimilarity();
    private ComparatorLogger compLogger;

    @Override
    public double compare(
            Car c1,
            Car c2,
            Correspondence<Attribute, Matchable> schemaCorrespondence
    ) {

        String m1 = c1.getFuelType();
        String m2 = c2.getFuelType();

        if (this.compLogger != null) {
            this.compLogger.setComparatorName(getClass().getName());
            this.compLogger.setRecord1Value(m1);
            this.compLogger.setRecord2Value(m2);
        }

        // preprocessing
        m1 = (m1 != null) ? m1.trim().toLowerCase() : "";
        m2 = (m2 != null) ? m2.trim().toLowerCase() : "";

        // diesel remains the same, benzin needs translation
        m1 = (m1.equals("benzin")) ? "petrol" : m1;
        m2 = (m2.equals("benzin")) ? "petrol" : m2;

        // calculate similarity
        double similarity = sim.calculate(m1, m2);

        if(this.compLogger != null){
            this.compLogger.setRecord1PreprocessedValue(m1);
            this.compLogger.setRecord2PreprocessedValue(m2);
            this.compLogger.setSimilarity(Double.toString(similarity));
        }

        return similarity;

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
