package sbs.repository.downtimes;

import sbs.model.downtimes.DowntimeCause;
import sbs.repository.GenericRepository;

public interface DowntimeCausesRepository extends GenericRepository<DowntimeCause,Integer> {

	boolean isIndependent(DowntimeCause cause);

	
}

