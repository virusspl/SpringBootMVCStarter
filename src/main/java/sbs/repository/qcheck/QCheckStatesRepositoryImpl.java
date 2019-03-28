package sbs.repository.qcheck;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qcheck.QCheckState;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qCheckStatesRepositoryImpl")
@Transactional
public class QCheckStatesRepositoryImpl extends GenericRepositoryAdapter<QCheckState, Integer>
		implements QCheckStatesRepository {
	

}
