package sbs.service.shipcust;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.shipcust.CustomShipment;
import sbs.repository.GenericRepository;
import sbs.repository.shipcust.CustomShipmentsRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class CustomShipmentsServiceImpl extends GenericServiceAdapter<CustomShipment, Integer> implements CustomShipmentsService{
	
	@SuppressWarnings("unused")
	private CustomShipmentsRepository customShipmentsRepository;
	
    @Autowired
	public CustomShipmentsServiceImpl(@Qualifier("customShipmentsRepositoryImpl") GenericRepository<CustomShipment, Integer> genericRepository) {
			super(genericRepository);
			this.customShipmentsRepository = (CustomShipmentsRepository) genericRepository;
	}


}
