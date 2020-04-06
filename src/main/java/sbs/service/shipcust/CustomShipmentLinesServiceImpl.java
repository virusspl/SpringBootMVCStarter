package sbs.service.shipcust;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.shipcust.CustomShipmentLine;
import sbs.repository.GenericRepository;
import sbs.repository.shipcust.CustomShipmentLinesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class CustomShipmentLinesServiceImpl extends GenericServiceAdapter<CustomShipmentLine, Integer> implements CustomShipmentLinesService{
	
	private CustomShipmentLinesRepository customShipmentLinesRepository;
	
    @Autowired
	public CustomShipmentLinesServiceImpl(@Qualifier("customShipmentLinesRepositoryImpl") GenericRepository<CustomShipmentLine, Integer> genericRepository) {
			super(genericRepository);
			this.customShipmentLinesRepository = (CustomShipmentLinesRepository) genericRepository;
	}

	@Override
	public List<CustomShipmentLine> findAllPendingSpare(int spareTypeProdOrAcq) {
		return customShipmentLinesRepository.findAllPendingSpare(spareTypeProdOrAcq);
	}


}
