package sbs.repository.downtimes;

import sbs.model.downtimes.DowntimeResponseType;
import sbs.repository.GenericRepository;

public interface DowntimeResponseTypesRepository extends GenericRepository<DowntimeResponseType,Integer> {

	DowntimeResponseType findByOrder(int order);
	DowntimeResponseType findByInternalTitle(String typeInternalTitle);

	
}

