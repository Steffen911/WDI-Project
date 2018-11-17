package Fusion;



import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.Car;
import de.uni_mannheim.informatik.dws.wdi.WDI_Project_T2.model.CarFusion;
import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import org.apache.commons.lang3.StringUtils;


import de.uni_mannheim.informatik.dws.winter.model.FusibleFactory;
import de.uni_mannheim.informatik.dws.winter.model.RecordGroup;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;


public class FusibleCarFactory implements FusibleFactory<Car, Attribute> {

	@Override
	public Car createInstanceForFusion(RecordGroup<Car, Attribute> cluster) {
		
		List<String> ids = new LinkedList<>();

		for (Car m : cluster.getRecords()) {
			ids.add(m.getIdentifier());
		}

		Collections.sort(ids);

		String mergedId = StringUtils.join(ids, '+');

		return new Car(mergedId, "fused");
	}

}
