package sbs.service.shipcust;

import sbs.model.shipcust.ShipCustLineState;
import sbs.service.GenericService;

public interface CustomShipmentLineStatesService extends GenericService<ShipCustLineState, Integer>{

	ShipCustLineState findByOrder(int order);
	ShipCustLineState findByInternalTitle(String typeInternalTitle);

}
