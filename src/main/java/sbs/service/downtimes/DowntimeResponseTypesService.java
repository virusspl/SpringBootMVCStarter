package sbs.service.downtimes;

import sbs.model.downtimes.DowntimeResponseType;
import sbs.service.GenericService;

public interface DowntimeResponseTypesService extends GenericService<DowntimeResponseType, Integer>{
	
	DowntimeResponseType findByOrder(int order);
	DowntimeResponseType findByInternalTitle(String typeInternalTitle);

	
}
