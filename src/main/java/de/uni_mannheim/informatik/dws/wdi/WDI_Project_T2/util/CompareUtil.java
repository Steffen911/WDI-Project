package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.similarity.SimilarityMeasure;

public class CompareUtil {

    public static double compareCarModel(Car c1,
                                         Car c2,
                                         String compName,
                                         SimilarityMeasure<String> sim,
                                         ComparatorLogger compLogger) {

        String m1 = c1.getModel();
        String m2 = c2.getModel();

        if (compLogger != null) {
            compLogger.setComparatorName(compName);
            compLogger.setRecord1Value(m1);
            compLogger.setRecord2Value(m2);
        }

        // preprocessing
        m1 = (m1 != null) ? m1.toLowerCase() : "";
        m2 = (m2 != null) ? m2.toLowerCase() : "";

        m1 = Utils.preprocessModel(m1);
        m2 = Utils.preprocessModel(m2);

        // calculate similarity
        double similarity = sim.calculate(m1, m2);

        if(compLogger != null){
            compLogger.setRecord1PreprocessedValue(m1);
            compLogger.setRecord2PreprocessedValue(m2);
            compLogger.setSimilarity(Double.toString(similarity));
        }

        return similarity;
    }

}
