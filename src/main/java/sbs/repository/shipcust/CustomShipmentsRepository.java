package sbs.repository.shipcust;

import java.util.List;

import sbs.model.shipcust.CustomShipment;
import sbs.repository.GenericRepository;

public interface CustomShipmentsRepository extends GenericRepository<CustomShipment,Integer> {

	List<CustomShipment> findAllClosed();
	List<CustomShipment> findAllPending();


}

