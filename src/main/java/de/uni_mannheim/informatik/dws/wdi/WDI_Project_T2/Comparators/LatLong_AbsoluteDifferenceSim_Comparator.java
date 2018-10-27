package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.Comparators;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.numeric.AbsoluteDifferenceSimilarity;

public class LatLong_AbsoluteDifferenceSim_Comparator implements Comparator<Car, Attribute> {

        private static final long serialVersionUID = 1L;

        private AbsoluteDifferenceSimilarity sim = new AbsoluteDifferenceSimilarity(75); // TODO what to use here?
        private ComparatorLogger comparisonLog;

        @Override
        public double compare(Car record1, Car record2, Correspondence<Attribute, Matchable> schemaCorrespondence) {

            Double lat1 = record1.getRegion().getLatitude();
            Double lat2 = record2.getRegion().getLatitude();
            Double long1 = record1.getRegion().getLongitude();
            Double long2 = record2.getRegion().getLongitude();


            //get very harsh Similarity measure
            double similarity = sim.calculate(lat1, lat2);
            similarity = (similarity + sim.calculate(long1, long2))/2; // TODO how to deal with this



            if(this.comparisonLog != null){
                this.comparisonLog.setComparatorName(getClass().getName());

                this.comparisonLog.setRecord1Value(String.valueOf( lat1 + " ~ " + long1));
                this.comparisonLog.setRecord2Value(String.valueOf( lat2 + " ~ " + long2));

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

