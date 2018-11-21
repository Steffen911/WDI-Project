package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.fusers;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.numeric.Average;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class HorsePowerFuserAverage extends AttributeValueFuser<Double, Car, Attribute>{

	public HorsePowerFuserAverage(ConflictResolutionFunction<Double, Car, Attribute> conflictResolution) {
		super(new Average() );
	}
	
	@Override
	public Double getValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
		return (double) c.getHorsePower();
	}
	
	@Override
	public void fuse(RecordGroup<Car, Attribute> group, Car fusedRecord,Processable<Correspondence<Attribute, Matchable>> correspondence, Attribute elem) {
		 FusedValue<Double, Car, Attribute> fused = getFusedValue(group, correspondence, elem);
	        fusedRecord.setHorsePower( fused.getValue().intValue());		
	}
	
	@Override
	public boolean hasValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
		
		return c.hasValue(c.HORSE_POWER);
	}
	
	
	

}
