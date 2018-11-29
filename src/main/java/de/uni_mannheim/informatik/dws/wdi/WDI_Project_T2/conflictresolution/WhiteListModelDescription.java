package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.conflictresolution;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.util.Utils;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.*;

public class WhiteListModelDescription<String, RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends ConflictResolutionFunction<String, RecordType, SchemaElementType> {

    public FusedValue<String, RecordType, SchemaElementType> resolveConflict(Collection<FusibleValue<String, RecordType, SchemaElementType>> values) {
        List<String> list = new LinkedList<>();

        for (FusibleValue<String, RecordType, SchemaElementType> value : values) {
            if (value.getValue() != null) {
                String description = (String) Utils.cleanModelDescriptionDataFusion((java.lang.String) value.getValue());
                list.add(description);
            }
        }

        if (list.size() == 0) {
            return new FusedValue<>((String) "nA");
        }

        if (list.size() == 1) {
            return new FusedValue<>(list.get(0));
        }

        int length = -1;
        java.lang.String longest = "nA";
        Iterator it = list.iterator();

        while (it.hasNext()) {
            java.lang.String cur = it.next().toString();

            if (cur.length() > length) {
                length = cur.length();
                longest = cur;
            }
        }

        return new FusedValue<>((String) longest);
    }
}
