package sbs.repository.qcheck;

import java.util.List;

import sbs.model.qcheck.QCheck;
import sbs.repository.GenericRepository;

public interface QCheckRepository extends GenericRepository<QCheck,Integer> {

	List<QCheck> findAllPending();
	List<QCheck> findAllClosed();

	
}

