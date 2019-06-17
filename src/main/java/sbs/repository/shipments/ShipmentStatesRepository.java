package sbs.repository.shipments;

import sbs.model.shipments.ShipmentState;
import sbs.repository.GenericRepository;

public interface ShipmentStatesRepository extends GenericRepository<ShipmentState,Integer> {

	ShipmentState findByOrder(int order);

	
}

