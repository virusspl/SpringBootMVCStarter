package sbs.service.qcheck;

import sbs.model.qcheck.QCheckState;
import sbs.service.GenericService;

public interface QCheckStatesService extends GenericService<QCheckState, Integer>{

	QCheckState findByOrder(int order);

	
}
