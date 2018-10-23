package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparators;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.LevenshteinSimilarity;


    public class CarManufModelComparatorLevenshtein implements Comparator<Car, Attribute> {

        private static final long serialVersionUID = 1L;
        private LevenshteinSimilarity sim = new LevenshteinSimilarity();

        private ComparatorLogger comparisonLog;

        @Override
        public double compare(
                Car record1,
                Car record2,
                Correspondence<Attribute, Matchable> schemaCorrespondences) {

            String s1 = record1.getManufacturer() + " " + record1.getModel();
            String s2 = record2.getManufacturer() + " " + record2.getModel();

            double similarity = sim.calculate(s1, s2);

            if(this.comparisonLog != null){
                this.comparisonLog.setComparatorName(getClass().getName());

                this.comparisonLog.setRecord1Value(s1);
                this.comparisonLog.setRecord2Value(s2);

                this.comparisonLog.setSimilarity(Double.toString(similarity));
            }
            return similarity;
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
