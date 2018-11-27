package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.conflictresolution;

import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Fusible;
import de.uni_mannheim.informatik.dws.winter.model.FusibleValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Median {@link ConflictResolutionFunction}: Returns the median of all values
 *
 * @param <RecordType> the type that represents a record
 */
public class Median<RecordType extends Matchable & Fusible<SchemaElementType>, SchemaElementType extends Matchable> extends
ConflictResolutionFunction<Double, RecordType, SchemaElementType> {

    @Override
    public FusedValue<Double, RecordType, SchemaElementType> resolveConflict(
    Collection<FusibleValue<Double, RecordType, SchemaElementType>> values) {

        List<Double> list = new LinkedList<>();

        for (FusibleValue<Double, RecordType, SchemaElementType> value : values) {
            list.add(value.getValue());
        }

        Collections.sort(list);

        boolean isEven = list.size() % 2 == 0;
        if (list.size() == 0) {
            return new FusedValue<>((Double) null);
        } else if (list.size() == 1) {
            return new FusedValue<>(list.get(0));
        } else if (isEven) {
            double middle = ((double) list.size() + 1.0) / 2.0;
            double median1 = list.get((int) Math.floor(middle) - 1);
            double median2 = list.get((int) Math.ceil(middle) - 1);

            return new FusedValue<>((median1 + median2) / 2.0);
        } else {
            int middle = list.size() / 2;

            return new FusedValue<>(list.get(middle - 1));
        }
    }
}

