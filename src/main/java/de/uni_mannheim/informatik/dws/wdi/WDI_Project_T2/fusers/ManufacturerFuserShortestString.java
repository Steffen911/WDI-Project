package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.fusers;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.ShortestString;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class ManufacturerFuserShortestString extends AttributeValueFuser<String, Car, Attribute>{
	
	public ManufacturerFuserShortestString(ConflictResolutionFunction<String, Car, Attribute> conflictResolution) {
		super(new ShortestString());
		
	}

	@Override
	public String getValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
		return c.getManufacturer();
	}

	@Override
	public void fuse(RecordGroup<Car, Attribute> group, Car fusedRecord,Processable<Correspondence<Attribute, Matchable>> correspondence, Attribute elem) {
		 FusedValue<String, Car, Attribute> fused = getFusedValue(group, correspondence, elem);
	        fusedRecord.setManufacturer(fused.getValue());		
	}

	@Override
	public boolean hasValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
		
		return c.hasValue(c.MANUFACTURER);
	}
	

}
