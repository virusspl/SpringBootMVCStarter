package sbs.service.shipments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.shipments.ShipmentLine;
import sbs.repository.GenericRepository;
import sbs.repository.shipments.ShipmentLinesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class ShipmentLinesServiceImpl extends GenericServiceAdapter<ShipmentLine, Integer> implements ShipmentLinesService{
	
	@SuppressWarnings("unused")
	private ShipmentLinesRepository shipmentLinesRepository;
	
    @Autowired
	public ShipmentLinesServiceImpl(@Qualifier("shipmentLinesRepositoryImpl") GenericRepository<ShipmentLine, Integer> genericRepository) {
			super(genericRepository);
			this.shipmentLinesRepository = (ShipmentLinesRepository) genericRepository;
	}


}
