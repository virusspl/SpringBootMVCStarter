package sbs.service.qcheck;

import java.util.List;

import sbs.model.qcheck.QCheck;
import sbs.service.GenericService;

public interface QCheckService extends GenericService<QCheck, Integer>{

	List<QCheck> findAllPending();
	List<QCheck> findAllClosed();
	
}
