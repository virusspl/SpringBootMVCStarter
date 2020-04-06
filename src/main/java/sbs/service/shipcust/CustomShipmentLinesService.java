package sbs.service.shipcust;

import java.util.List;

import sbs.model.shipcust.CustomShipmentLine;
import sbs.service.GenericService;

public interface CustomShipmentLinesService extends GenericService<CustomShipmentLine, Integer>{

	public final static int SPARE_PROD = 1;
	public final static int SPARE_ACQ = 2;
	
	List<CustomShipmentLine> findAllPendingSpare(int spareTypeProdOrAcq);

}
