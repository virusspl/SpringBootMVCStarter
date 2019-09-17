package sbs.repository.downtimes;

import java.util.List;

import sbs.model.downtimes.DowntimeCause;
import sbs.repository.GenericRepository;

public interface DowntimeCausesRepository extends GenericRepository<DowntimeCause,Integer> {

	boolean isIndependent(DowntimeCause cause);
	List<DowntimeCause> findByType(String typeInternalTitle, boolean activeOnly);

	
}

