package sbs.service.downtimes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.downtimes.DowntimeDetailsFailure;
import sbs.repository.GenericRepository;
import sbs.repository.downtimes.DowntimeDetailsFailureRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class DowntimeDetailsFailureServiceImpl extends GenericServiceAdapter<DowntimeDetailsFailure, Integer> implements DowntimeDetailsFailureService{
	
	@SuppressWarnings("unused")
	private DowntimeDetailsFailureRepository downtimeDetailsFailureRepository;
	
    @Autowired
	public DowntimeDetailsFailureServiceImpl(@Qualifier("downtimeDetailsFailureRepositoryImpl") GenericRepository<DowntimeDetailsFailure, Integer> genericRepository) {
			super(genericRepository);
			this.downtimeDetailsFailureRepository = (DowntimeDetailsFailureRepository) genericRepository;
	}

}
