package sbs.repository.shipcust;

import sbs.model.shipcust.ShipCustLineState;
import sbs.repository.GenericRepository;

public interface CustomShipmentLineStatesRepository extends GenericRepository<ShipCustLineState,Integer> {

	ShipCustLineState findByOrder(int order);
	ShipCustLineState findByInternalTitle(String typeInternalTitle);


}

