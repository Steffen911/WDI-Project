package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.fusers;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Intersection;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class ModelFuserSplitIntersection extends AttributeValueFuser<List<String>, Car, Attribute> {

    public ModelFuserSplitIntersection() {
        super(new Intersection());
        this.setCollectDebugResults(false);
    }

    @Override
    public List<String> getValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
        return Arrays.asList(c.getModel().split("[_\\s]"));
    }

    @Override
    public void fuse(RecordGroup<Car, Attribute> group, Car fusedRecord, Processable<Correspondence<Attribute, Matchable>> correspondence, Attribute elem) {
        FusedValue<List<String>, Car, Attribute> fused = getFusedValue(group, correspondence, elem);
        fusedRecord.setAttributeProvenance(Car.MODEL, fused.getOriginalIds());
        fusedRecord.setModel(StringUtils.join(fused.getValue(), " "));
    }

    @Override
    public boolean hasValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
        return c.hasValue(Car.MODEL);
    }

}
