package sbs.service.downtimes;

import sbs.model.downtimes.DowntimeType;
import sbs.service.GenericService;

public interface DowntimeTypesService extends GenericService<DowntimeType, Integer>{

	public static final String TYPE_FAULT = "fault";
	public static final String TYPE_MATERIAL = "material";
	public static final String TYPE_QUALITY = "quality";
	public static final String TYPE_SAFETY = "safety";
	public static final String TYPE_OTHER = "other";
	
	DowntimeType findByOrder(int order);
	DowntimeType findByInternalTitle(String typeInternalTitle);

	
}
