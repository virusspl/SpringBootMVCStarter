package sbs.service.shipments;

import java.util.List;

import sbs.model.shipments.ShipmentLine;
import sbs.service.GenericService;

public interface ShipmentLinesService extends GenericService<ShipmentLine, Integer>{

	List<ShipmentLine> findLinesByShipmentId(int shipmentId);

}
