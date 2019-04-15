package sbs.service.qcheck;

import java.util.List;

import sbs.model.qcheck.QCheckAction;
import sbs.service.GenericService;

public interface QCheckActionsService extends GenericService<QCheckAction, Integer>{

	List<QCheckAction> findByCheckId(int id);

}
