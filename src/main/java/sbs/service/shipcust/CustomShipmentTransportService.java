package sbs.service.shipcust;

import java.util.List;

import sbs.model.shipcust.ShipCustTransport;
import sbs.service.GenericService;

public interface CustomShipmentTransportService extends GenericService<ShipCustTransport, Integer>{

	List<ShipCustTransport> findAllActive();

}
