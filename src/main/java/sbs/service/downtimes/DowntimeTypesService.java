package sbs.service.downtimes;

import sbs.model.downtimes.DowntimeType;
import sbs.service.GenericService;

public interface DowntimeTypesService extends GenericService<DowntimeType, Integer>{

	DowntimeType findByOrder(int order);

	
}
