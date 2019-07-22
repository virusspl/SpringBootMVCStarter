package sbs.service.downtimes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.downtimes.DowntimeDetailsMaterial;
import sbs.repository.GenericRepository;
import sbs.repository.downtimes.DowntimeDetailsMaterialRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class DowntimeDetailsMaterialServiceImpl extends GenericServiceAdapter<DowntimeDetailsMaterial, Integer> implements DowntimeDetailsMaterialService{
	
	@SuppressWarnings("unused")
	private DowntimeDetailsMaterialRepository downtimeDetailsMaterialRepository;
	
    @Autowired
	public DowntimeDetailsMaterialServiceImpl(@Qualifier("downtimeDetailsMaterialRepositoryImpl") GenericRepository<DowntimeDetailsMaterial, Integer> genericRepository) {
			super(genericRepository);
			this.downtimeDetailsMaterialRepository = (DowntimeDetailsMaterialRepository) genericRepository;
	}

}
