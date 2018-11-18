package Fusion;

import java.util.List;


import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.winter.datafusion.AttributeValueFuser;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.ConflictResolutionFunction;
import de.uni_mannheim.informatik.dws.winter.datafusion.conflictresolution.string.LongestString;
import de.uni_mannheim.informatik.dws.winter.model.Correspondence;
import de.uni_mannheim.informatik.dws.winter.model.FusedValue;
import de.uni_mannheim.informatik.dws.winter.model.Matchable;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import de.uni_mannheim.informatik.dws.winter.processing.Processable;

public class Fusers extends AttributeValueFuser<String, Car, Attribute>{
	
	public Fusers() {
		super(new LongestString<Car, Attribute>());
	
	}	
	
	@Override
	public boolean hasValue(Car record, Correspondence<Attribute, Matchable> correspondence) {
		return record.hasValue(Car.MODEL);
	}
	
	
	@Override
	public String getValue(Car record, Correspondence<Attribute, Matchable> correspondence) {
		return record.getModel();
	}

	@Override
	public void fuse(RecordGroup<Car, Attribute> group, Car fusedRecord, Processable<Correspondence<Attribute, Matchable>> schemaCorrespondences, Attribute schemaElement) {
		FusedValue<String, Car, Attribute> fused = getFusedValue(group, schemaCorrespondences, schemaElement);
		fusedRecord.setModel(fused.getValue());
		fusedRecord.setAttributeProvenance(Car.MODEL,
				fused.getOriginalIds());
	}





}
