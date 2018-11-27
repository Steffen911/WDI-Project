package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.conflictresolution;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util.Utils;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.*;

public class CascadingCRF<String, RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends ConflictResolutionFunction<String, RecordType, SchemaElementType> {
    public CascadingCRF() {
    }


    public FusedValue<String, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<String, RecordType, SchemaElementType>> values) {
        Map<String, Integer> frequencies = new HashMap();

        List<String> list = new LinkedList<>();

        for (FusibleValue<String, RecordType, SchemaElementType> value : values) {
            String description= (String) Utils.cleanModelDescriptionDataFusion((java.lang.String) value.getValue());
            list.add(description);
        }
        if (list.size() ==1){
            return new FusedValue<>(list.get(1));
        }

        int length =-1;
        String longest="nA";
        Iterator it = list.iterator();
        while(it.hasNext()){
            String cur = (String) it.next().toString();

            if(cur.length() >length){
                length = cur.length();
                longest = cur;
            }
        }


        return new FusedValue<>(longest);

    }
}
