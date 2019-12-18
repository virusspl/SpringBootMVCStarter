package sbs.repository.downtimes;

import java.util.List;

import sbs.model.downtimes.Downtime;
import sbs.repository.GenericRepository;

public interface DowntimesRepository extends GenericRepository<Downtime,Integer> {

	List<Downtime> findAllPending();

	List<Downtime> findWithoutResponseForUser(Long userId);

	
}

