package sbs.service.downtimes;

import sbs.model.downtimes.DowntimeCause;
import sbs.service.GenericService;

public interface DowntimeCausesService extends GenericService<DowntimeCause, Integer>{

	boolean isIndependent(DowntimeCause cause);

	
}
