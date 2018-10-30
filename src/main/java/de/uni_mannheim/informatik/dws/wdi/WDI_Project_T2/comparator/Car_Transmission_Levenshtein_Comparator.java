package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;

public class Car_Transmission_Levenshtein_Comparator implements Comparator<Car, Attribute> {
    private static final long serialVersionUID = 1L;

    private LevenshteinSimilarity sim = new LevenshteinSimilarity();

    private ComparatorLogger comparisonLog;


    @Override
    public double compare(Car record1,
                          Car record2,
                          Correspondence<Attribute, Matchable> schemaCorrespondence) {
        if (record1.getTransmission() != null && record2.getTransmission() != null) {
            String transmission1 = record1.getTransmission().toLowerCase();
            String transmission2 = record2.getTransmission().toLowerCase();

            transmission1 = preprocessTransmissionString(transmission1);
            transmission2 = preprocessTransmissionString(transmission2);
            // does have to be THE same, harsh similarity measure needed
            double similarity = sim.calculate(transmission1, transmission2);


            if (this.comparisonLog != null) {
                this.comparisonLog.setComparatorName(getClass().getName());
                this.comparisonLog.setRecord1Value(transmission1);
                this.comparisonLog.setRecord2Value(transmission2);
                this.comparisonLog.setSimilarity(Double.toString(similarity));
            }
            return similarity;
        }
        return 0;
    }

    private String preprocessTransmissionString(String transmission) {
        String cleaned = transmission;
        if(cleaned.equals("manuell")){
            cleaned = "manual";
        } else if (cleaned.equals("automatik")){
            cleaned = "automatic";
        }
        return cleaned;
    }

}
