package sbs.repository.shipments;

import java.util.List;

import sbs.model.shipments.ShipmentLine;
import sbs.repository.GenericRepository;

public interface ShipmentLinesRepository extends GenericRepository<ShipmentLine,Integer> {

	List<ShipmentLine> findLinesByShipmentId(int shipmentId);

	
}

