package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.fusers;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.conflictresolution.Median;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class LatitudeFuserMedian extends AttributeValueFuser<Double, Car, Attribute> {

    public LatitudeFuserMedian() {
        super(new Median<>());
    }

    @Override
    public void fuse(RecordGroup<Car, Attribute> group, Car fusedRecord, Processable<Correspondence<Attribute, Matchable>> correspondence, Attribute elem) {
        FusedValue<Double, Car, Attribute> fused = getFusedValue(group, correspondence, elem);
        fusedRecord.setAttributeProvenance(Car.LATITUDE, fused.getOriginalIds());
        fusedRecord.getRegion().setLatitude((fused.getValue() == null) ? 0 : fused.getValue());
    }

    @Override
    public Double getValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
        return c.getRegion().getLatitude();
    }

    @Override
    public boolean hasValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
        return c.hasValue(Car.LATITUDE);
    }

}
