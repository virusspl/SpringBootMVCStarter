package sbs.service.downtimes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.downtimes.DowntimeResponseType;
import sbs.repository.GenericRepository;
import sbs.repository.downtimes.DowntimeResponseTypesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class DowntimeResponseTypesServiceImpl extends GenericServiceAdapter<DowntimeResponseType, Integer> implements DowntimeResponseTypesService{
	
	private DowntimeResponseTypesRepository downtimeResponseTypesRepository;
	
    @Autowired
	public DowntimeResponseTypesServiceImpl(@Qualifier("downtimeResponseTypesRepositoryImpl") GenericRepository<DowntimeResponseType, Integer> genericRepository) {
			super(genericRepository);
			this.downtimeResponseTypesRepository = (DowntimeResponseTypesRepository) genericRepository;
	}

	@Override
	public DowntimeResponseType findByOrder(int order) {
		return downtimeResponseTypesRepository.findByOrder(order);
	}

	@Override
	public DowntimeResponseType findByInternalTitle(String typeInternalTitle) {
		return downtimeResponseTypesRepository.findByInternalTitle(typeInternalTitle);
	}

}
