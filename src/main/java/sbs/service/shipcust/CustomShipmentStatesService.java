package sbs.service.shipcust;

import sbs.model.shipcust.ShipCustState;
import sbs.service.GenericService;

public interface CustomShipmentStatesService extends GenericService<ShipCustState, Integer>{

	ShipCustState findByOrder(int order);
	ShipCustState findByInternalTitle(String typeInternalTitle);

}
