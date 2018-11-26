package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model;

import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FusibleCarFactory implements FusibleFactory<Car, Attribute> {

    @Override
    public Car createInstanceForFusion(RecordGroup<Car, Attribute> cluster) {
        List<String> ids = new LinkedList<>();
        for (Car c : cluster.getRecords()) {
            ids.add(c.getIdentifier());
        }
        Collections.sort(ids);
        String merged = StringUtils.join(ids, "_");
        return new Car(merged, "fused");
    }

}
