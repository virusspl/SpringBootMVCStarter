package sbs.service.shipments;

import sbs.model.shipments.ShipmentState;
import sbs.service.GenericService;

public interface ShipmentStatesService extends GenericService<ShipmentState, Integer>{

	ShipmentState findByOrder(int order);

}
