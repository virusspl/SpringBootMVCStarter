package sbs.service.cebs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.cebs.CebsEvent;
import sbs.repository.GenericRepository;
import sbs.repository.cebs.CebsEventsRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class CebsEventsServiceImpl extends GenericServiceAdapter<CebsEvent, Integer> implements CebsEventsService {
	
	
	private CebsEventsRepository cebsEventsRepository;
	
    @Autowired
	public CebsEventsServiceImpl(@Qualifier("cebsEventsRepositoryImpl") GenericRepository<CebsEvent, Integer> genericRepository) {
			super(genericRepository);
			this.cebsEventsRepository = (CebsEventsRepository) genericRepository;
	}

	@Override
	public CebsEvent findActiveEvent() {
		return cebsEventsRepository.findActiveEvent();
	}


}
