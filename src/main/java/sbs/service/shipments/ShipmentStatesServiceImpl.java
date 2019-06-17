package sbs.service.shipments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.shipments.ShipmentState;
import sbs.repository.GenericRepository;
import sbs.repository.shipments.ShipmentStatesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class ShipmentStatesServiceImpl extends GenericServiceAdapter<ShipmentState, Integer> implements ShipmentStatesService{
	
	private ShipmentStatesRepository shipmentStatesRepository;
	
    @Autowired
	public ShipmentStatesServiceImpl(@Qualifier("shipmentStatesRepositoryImpl") GenericRepository<ShipmentState, Integer> genericRepository) {
			super(genericRepository);
			this.shipmentStatesRepository = (ShipmentStatesRepository) genericRepository;
	}

	@Override
	public ShipmentState findByOrder(int order) {
		return shipmentStatesRepository.findByOrder(order);
	}


}
