package sbs.service.downtimes;

import java.util.List;

import sbs.model.downtimes.Downtime;
import sbs.service.GenericService;

public interface DowntimesService extends GenericService<Downtime, Integer>{

	List<Downtime> findAllPending();

	List<Downtime> findWithoutResponseForUser(Long userId);

	
}
