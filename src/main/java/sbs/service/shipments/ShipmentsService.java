package sbs.service.shipments;

import java.util.List;

import sbs.model.shipments.Shipment;
import sbs.service.GenericService;

public interface ShipmentsService extends GenericService<Shipment, Integer>{

	List<Shipment> findPending(String company);

	List<Shipment> findShipped(String company);

}
