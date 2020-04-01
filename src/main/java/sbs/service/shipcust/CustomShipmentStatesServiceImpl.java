package sbs.service.shipcust;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.shipcust.ShipCustState;
import sbs.repository.GenericRepository;
import sbs.repository.shipcust.CustomShipmentStatesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class CustomShipmentStatesServiceImpl extends GenericServiceAdapter<ShipCustState, Integer> implements CustomShipmentStatesService{
	
	@SuppressWarnings("unused")
	private CustomShipmentStatesRepository customShipmentStatesRepository;
	
    @Autowired
	public CustomShipmentStatesServiceImpl(@Qualifier("customShipmentStatesRepositoryImpl") GenericRepository<ShipCustState, Integer> genericRepository) {
			super(genericRepository);
			this.customShipmentStatesRepository = (CustomShipmentStatesRepository) genericRepository;
	}


}
