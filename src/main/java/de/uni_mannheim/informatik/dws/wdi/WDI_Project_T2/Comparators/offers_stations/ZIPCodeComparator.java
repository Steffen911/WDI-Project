package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.Comparators.offers_stations;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;

public class ZIPCodeComparator implements Comparator<Car, Attribute> {
        private static final long serialVersionUID = 1L;

        // The numbers have to be the same
        private AbsoluteDifferenceSimilarity sim = new AbsoluteDifferenceSimilarity(100); // TODO or rather 0?
        private ComparatorLogger comparisonLog;

        @Override
        public double compare(Car record1,
                              Car record2,
                              Correspondence<Attribute, Matchable> schemaCorrespondence) {

            if (record1.getRegion().getZipCode() != 0 && record2.getRegion().getZipCode() != 0){
                double similarity = sim.calculate(
                        (double) record1.getRegion().getZipCode(),
                        (double) record2.getRegion().getZipCode());

                if(this.comparisonLog != null){
                    this.comparisonLog.setComparatorName(getClass().getName());

                    this.comparisonLog.setRecord1Value(String.valueOf(record1.getRegion().getZipCode()));
                    this.comparisonLog.setRecord2Value(String.valueOf(record2.getRegion().getZipCode()));

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

