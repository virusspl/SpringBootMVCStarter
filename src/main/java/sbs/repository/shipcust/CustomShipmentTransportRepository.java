package sbs.repository.shipcust;

import java.util.List;

import sbs.model.shipcust.ShipCustTransport;
import sbs.repository.GenericRepository;

public interface CustomShipmentTransportRepository extends GenericRepository<ShipCustTransport,Integer> {

	List<ShipCustTransport> findAllActive();

}

