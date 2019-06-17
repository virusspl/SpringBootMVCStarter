package sbs.service.shipments;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.shipments.Shipment;
import sbs.repository.GenericRepository;
import sbs.repository.shipments.ShipmentsRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class ShipmentsServiceImpl extends GenericServiceAdapter<Shipment, Integer> implements ShipmentsService{
	
	private ShipmentsRepository shipmentsRepository;
	
    @Autowired
	public ShipmentsServiceImpl(@Qualifier("shipmentsRepositoryImpl") GenericRepository<Shipment, Integer> genericRepository) {
			super(genericRepository);
			this.shipmentsRepository = (ShipmentsRepository) genericRepository;
	}

	@Override
	public List<Shipment> findPending(String company) {
		return shipmentsRepository.findPending(company);
	}

	@Override
	public List<Shipment> findShipped(String company) {
		return shipmentsRepository.findShipped(company);
	}


}
