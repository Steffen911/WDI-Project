package de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.fusers;

import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.list.Intersection;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class ModelFuserIntersection extends AttributeValueFuser<String, Car, Attribute>{
	
	public ModelFuserIntersection(ConflictResolutionFunction<String, Car, Attribute> conflictResolution) {
		super(new Intersection());
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
		return c.getModel();
	}

	@Override
	public void fuse(RecordGroup<Car, Attribute> group, Car fusedRecord,Processable<Correspondence<Attribute, Matchable>> correspondence, Attribute elem) {
		 FusedValue<String, Car, Attribute> fused = getFusedValue(group, correspondence, elem);
	        fusedRecord.setModel(fused.getValue());		
	}

	@Override
	public boolean hasValue(Car c, Correspondence<Attribute, Matchable> correspondence) {
		
		return c.hasValue(c.MODEL);
	}

}
