package sbs.service.capacity;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.capacity.CapacityItem;
import sbs.repository.GenericRepository;
import sbs.repository.capacity.CapacityRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class CapacityServiceImpl extends GenericServiceAdapter<CapacityItem, Integer> implements CapacityService {
	
	
	private CapacityRepository capacityRepository;
	
    @Autowired
	public CapacityServiceImpl(@Qualifier("capacityRepositoryImpl") GenericRepository<CapacityItem, Integer> genericRepository) {
			super(genericRepository);
			this.capacityRepository = (CapacityRepository) genericRepository;
	}

	@Override
	public void deleteItemsOnDate(Date date) {
		capacityRepository.deleteItemsOnDate(date);
	}

	@Override
	public Timestamp getDateZero() {
		return capacityRepository.getDateZero();
	}


}
