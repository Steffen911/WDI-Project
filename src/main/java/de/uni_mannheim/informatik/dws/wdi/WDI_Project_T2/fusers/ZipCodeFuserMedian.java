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

public class ZipCodeFuserMedian extends AttributeValueFuser<Double, Car, Attribute> {

    public ZipCodeFuserMedian() {
        super(new Median<>());
    }

    @Override
    public void fuse(RecordGroup<Car, Attribute> group, Car fusedRecord, Processable<Correspondence<Attribute, Matchable>> correspondence, Attribute elem) {
        FusedValue<Double, Car, Attribute> fused = getFusedValue(group, correspondence, elem);
        int zip = (fused.getValue() != null) ? fused.getValue().intValue() : 0;
        fusedRecord.getRegion().setZipCode(zip);
    }

    @Override
    public Double getValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
        return (double) c.getRegion().getZipCode();
    }

    @Override
    public boolean hasValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
        return c.hasValue(c.ZIP_CODE);
    }

}
