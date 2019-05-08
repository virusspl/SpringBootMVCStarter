package sbs.repository.system;

import sbs.model.system.SystemInfoParameter;
import sbs.repository.GenericRepository;

public interface SystemInfoParametersRepository extends GenericRepository<SystemInfoParameter,Integer> {

	SystemInfoParameter findByCode(String code);

}

