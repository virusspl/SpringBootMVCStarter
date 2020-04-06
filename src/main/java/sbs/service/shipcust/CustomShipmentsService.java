package sbs.service.shipcust;

import java.util.List;

import sbs.model.shipcust.CustomShipment;
import sbs.service.GenericService;

public interface CustomShipmentsService extends GenericService<CustomShipment, Integer>{

	List<CustomShipment> findAllClosed();
	List<CustomShipment> findAllPending();

}
