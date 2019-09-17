package sbs.service.downtimes;

import java.util.List;

import sbs.model.downtimes.DowntimeCause;
import sbs.service.GenericService;

public interface DowntimeCausesService extends GenericService<DowntimeCause, Integer>{

	public static final String CAUSE_FAULT = "fault";
	public static final String CAUSE_MATERIAL = "material";
	public static final String CAUSE_QUALITY = "quality";
	public static final String CAUSE_SAFETY = "safety";
	public static final String CAUSE_OTHER = "other";
	
	public boolean isIndependent(DowntimeCause cause);
	public List<DowntimeCause> findByType(String typeInternalTitle, boolean activeOnly);

	
}
