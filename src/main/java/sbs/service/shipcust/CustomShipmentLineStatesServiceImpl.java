package sbs.service.shipcust;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.shipcust.ShipCustLineState;
import sbs.repository.GenericRepository;
import sbs.repository.shipcust.CustomShipmentLineStatesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class CustomShipmentLineStatesServiceImpl extends GenericServiceAdapter<ShipCustLineState, Integer> implements CustomShipmentLineStatesService{
	
	private CustomShipmentLineStatesRepository customShipmentLineStatesRepository;
	
    @Autowired
	public CustomShipmentLineStatesServiceImpl(@Qualifier("customShipmentLineStatesRepositoryImpl") GenericRepository<ShipCustLineState, Integer> genericRepository) {
			super(genericRepository);
			this.customShipmentLineStatesRepository = (CustomShipmentLineStatesRepository) genericRepository;
	}

	@Override
	public ShipCustLineState findByOrder(int order) {
		return customShipmentLineStatesRepository.findByOrder(order);
	}

	@Override
	public ShipCustLineState findByInternalTitle(String typeInternalTitle) {
		return customShipmentLineStatesRepository.findByInternalTitle(typeInternalTitle);
	}


}
