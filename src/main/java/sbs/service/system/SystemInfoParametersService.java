package sbs.service.system;

import sbs.model.system.SystemInfoParameter;
import sbs.service.GenericService;

public interface SystemInfoParametersService extends GenericService<SystemInfoParameter, Integer> {

	public void storeSystemInfoParameter(String code, String description, String value);
	public SystemInfoParameter findByCode(String code);
	
}
