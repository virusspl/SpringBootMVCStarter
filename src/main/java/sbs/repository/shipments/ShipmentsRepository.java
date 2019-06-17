package sbs.repository.shipments;

import java.util.List;

import sbs.model.shipments.Shipment;
import sbs.repository.GenericRepository;

public interface ShipmentsRepository extends GenericRepository<Shipment,Integer> {

	List<Shipment> findPending(String company);

	List<Shipment> findShipped(String company);

	
}

