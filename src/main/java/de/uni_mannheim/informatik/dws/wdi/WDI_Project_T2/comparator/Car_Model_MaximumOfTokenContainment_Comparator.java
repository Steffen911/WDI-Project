package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.Utils;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.MaximumOfTokenContainment;

public class Car_Model_MaximumOfTokenContainment_Comparator  implements Comparator<Car, Attribute> {
    private static final long serialVersionUID = 1L;
    private MaximumOfTokenContainment sim = new MaximumOfTokenContainment();
    private ComparatorLogger comparisonLog;

    @Override
    public double compare(Car record1,
                          Car record2,
                          Correspondence<Attribute, Matchable> schemaCorrespondence) {
        if (record1.getModel() != null && record2.getModel() != null) {
            String model1 = record1.getModel().toLowerCase();
            String model2 = record2.getModel().toLowerCase();

            model1= preprocessModelString(model1);
            model2= preprocessModelString(model2);
            double similarity = sim.calculate(model1, model2);


            if (this.comparisonLog != null) {
                this.comparisonLog.setComparatorName(getClass().getName());

                this.comparisonLog.setRecord1Value(model1);
                this.comparisonLog.setRecord2Value(model2);

                this.comparisonLog.setSimilarity(Double.toString(similarity));
            }
            return similarity;
        }
        return 0;
    }

    private String preprocessModelString(String modelDescription) {
        String cleaned = Utils.removeUnderscores(modelDescription);
        cleaned = Utils.replaceUmlaute(cleaned);
        cleaned = Utils.removeNonDescriptionWords(cleaned);
        cleaned = Utils.removeYearDates(cleaned);

        //remove all duplicate words and more than one space
        cleaned = Utils.removeDuplicateWords(cleaned);
        return cleaned;
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
