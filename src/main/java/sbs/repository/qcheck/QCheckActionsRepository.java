package sbs.repository.qcheck;

import java.util.List;

import sbs.model.qcheck.QCheckAction;
import sbs.repository.GenericRepository;

public interface QCheckActionsRepository extends GenericRepository<QCheckAction,Integer> {

	List<QCheckAction> findByCheckId(int id);

}

