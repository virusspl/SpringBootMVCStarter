package sbs.repository.shipcust;

import sbs.model.shipcust.ShipCustState;
import sbs.repository.GenericRepository;

public interface CustomShipmentStatesRepository extends GenericRepository<ShipCustState,Integer> {

	ShipCustState findByOrder(int order);
	ShipCustState findByInternalTitle(String typeInternalTitle);


}

