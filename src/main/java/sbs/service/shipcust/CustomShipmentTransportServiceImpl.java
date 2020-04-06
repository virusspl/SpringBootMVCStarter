package sbs.service.shipcust;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.shipcust.ShipCustTransport;
import sbs.repository.GenericRepository;
import sbs.repository.shipcust.CustomShipmentTransportRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class CustomShipmentTransportServiceImpl extends GenericServiceAdapter<ShipCustTransport, Integer> implements CustomShipmentTransportService{
	
	private CustomShipmentTransportRepository customShipmentTransportRepository;
	
    @Autowired
	public CustomShipmentTransportServiceImpl(@Qualifier("customShipmentTransportRepositoryImpl") GenericRepository<ShipCustTransport, Integer> genericRepository) {
			super(genericRepository);
			this.customShipmentTransportRepository = (CustomShipmentTransportRepository) genericRepository;
	}

	@Override
	public List<ShipCustTransport> findAllActive() {
		return customShipmentTransportRepository.findAllActive();
	}


}
