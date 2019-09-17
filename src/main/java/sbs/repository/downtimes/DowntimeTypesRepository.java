package sbs.repository.downtimes;

import sbs.model.downtimes.DowntimeType;
import sbs.repository.GenericRepository;

public interface DowntimeTypesRepository extends GenericRepository<DowntimeType,Integer> {

	DowntimeType findByOrder(int order);
	DowntimeType findByInternalTitle(String typeInternalTitle);

	
}

