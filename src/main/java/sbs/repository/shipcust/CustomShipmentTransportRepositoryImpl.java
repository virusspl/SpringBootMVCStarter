package sbs.repository.shipcust;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.shipcust.ShipCustTransport;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CustomShipmentTransportRepositoryImpl extends GenericRepositoryAdapter<ShipCustTransport, Integer>
		implements CustomShipmentTransportRepository {


}
