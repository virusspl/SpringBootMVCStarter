package sbs.service.downtimes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.downtimes.Downtime;
import sbs.repository.GenericRepository;
import sbs.repository.downtimes.DowntimesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class DowntimesServiceImpl extends GenericServiceAdapter<Downtime, Integer> implements DowntimesService{
	
	@SuppressWarnings("unused")
	private DowntimesRepository downtimesRepository;
	
    @Autowired
	public DowntimesServiceImpl(@Qualifier("downtimesRepositoryImpl") GenericRepository<Downtime, Integer> genericRepository) {
			super(genericRepository);
			this.downtimesRepository = (DowntimesRepository) genericRepository;
	}

}
