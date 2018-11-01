package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.comparator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util.Utils;
import de.uni_mannheim.informatik.dws.winter.matching.rules.Comparator;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.matching.rules.ComparatorLogger;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.similarity.string.TokenizingJaccardSimilarity;


public class Car_Model_TokenizingJaccard_Comparator implements Comparator<Car, Attribute> {

    private static final long serialVersionUID = 1L;
    private TokenizingJaccardSimilarity sim =  new TokenizingJaccardSimilarity();
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

        String cleaned = Utils.preprocessModel(modelDescription);
        return cleaned;

    }
    private void preprocessModelStringTEST(String modelDescription) {
        String t1 = "BMW_316i___e36_Limousine___Bastlerfahrzeug__Export 3er";
        String t2 ="Ford_C___Max_Titanium_1_0_L_EcoBoost c_max";
        String t3 = "Golf_3_1.6 golf";
        String t4 ="Volkswagen_Passat_Variant_2.0_TDI_Comfortline passat";
        String t5 ="VW_Golf_4_5_tuerig_zu_verkaufen_mit_Anhängerkupplung golf";
        String t6 ="Ford_Fiesta_1.0_Start_Stop_Titanium_TOP_Ausstattung_+ fiesta";
        String t7 = "BMW_330_D__M_packet_2_TÜV_noch_15_Monate 3er";
        String t8 = "Fast_Jang_Timer_sucht_Übernehmer_!! andere";

        String cleaned = t8.toLowerCase();
        cleaned = Utils.preprocessModel(cleaned);
    }

    @Override
    public ComparatorLogger getComparisonLog() {
        return this.comparisonLog;
    }

    @Override
    public void setComparisonLog(ComparatorLogger comparatorLog) {
        this.comparisonLog = comparatorLog;
    }

    public static void main (String args[]){
        Car_Model_TokenizingJaccard_Comparator test = new Car_Model_TokenizingJaccard_Comparator();
        test.preprocessModelStringTEST(null);
    }
}
