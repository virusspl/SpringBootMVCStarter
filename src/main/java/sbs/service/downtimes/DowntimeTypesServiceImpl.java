package sbs.service.downtimes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.downtimes.DowntimeType;
import sbs.repository.GenericRepository;
import sbs.repository.downtimes.DowntimeTypesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class DowntimeTypesServiceImpl extends GenericServiceAdapter<DowntimeType, Integer> implements DowntimeTypesService{
	
	private DowntimeTypesRepository downtimeTypesRepository;
	
    @Autowired
	public DowntimeTypesServiceImpl(@Qualifier("downtimeTypesRepositoryImpl") GenericRepository<DowntimeType, Integer> genericRepository) {
			super(genericRepository);
			this.downtimeTypesRepository = (DowntimeTypesRepository) genericRepository;
	}

	@Override
	public DowntimeType findByOrder(int order) {
		return downtimeTypesRepository.findByOrder(order);
	}

}
