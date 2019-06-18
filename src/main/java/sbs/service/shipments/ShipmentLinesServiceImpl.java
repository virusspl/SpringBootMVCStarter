package sbs.service.shipments;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.shipments.ShipmentLine;
import sbs.repository.GenericRepository;
import sbs.repository.shipments.ShipmentLinesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class ShipmentLinesServiceImpl extends GenericServiceAdapter<ShipmentLine, Integer> implements ShipmentLinesService{
	
	private ShipmentLinesRepository shipmentLinesRepository;
	
    @Autowired
	public ShipmentLinesServiceImpl(@Qualifier("shipmentLinesRepositoryImpl") GenericRepository<ShipmentLine, Integer> genericRepository) {
			super(genericRepository);
			this.shipmentLinesRepository = (ShipmentLinesRepository) genericRepository;
	}

	@Override
	public List<ShipmentLine> findLinesByShipmentId(int shipmentId) {
		return shipmentLinesRepository.findLinesByShipmentId(shipmentId);
	}


}
