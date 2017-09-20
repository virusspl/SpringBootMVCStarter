package sbs.service.hrua;

import java.util.List;

import sbs.model.hruafiles.HrUaInfo;
import sbs.service.GenericService;

public interface HrUaService extends GenericService<HrUaInfo, Integer>{

	
	List<HrUaInfo> findCurrent();
	List<HrUaInfo> findArchived();
	
	
}
