package sbs.repository.qcheck;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qcheck.QCheck;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qCheckRepositoryImpl")
@Transactional
public class QCheckRepositoryImpl extends GenericRepositoryAdapter<QCheck, Integer>
		implements QCheckRepository {
	

}
