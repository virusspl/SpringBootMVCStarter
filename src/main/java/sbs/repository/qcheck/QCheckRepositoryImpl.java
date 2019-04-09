package sbs.repository.qcheck;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qcheck.QCheck;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qCheckRepositoryImpl")
@Transactional
public class QCheckRepositoryImpl extends GenericRepositoryAdapter<QCheck, Integer>
		implements QCheckRepository {
	
	@Override
	public List<QCheck> findAllPending() {
		String hql = "from QCheck c where c.state.order < :ord";
		@SuppressWarnings("unchecked")
		List<QCheck> result = (List<QCheck>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", 40)
		.list();
		
		return result;
	}


	@Override
	public List<QCheck> findAllClosed() {
		String hql = "from QCheck c where c.state.order >= :ord";
		@SuppressWarnings("unchecked")
		List<QCheck> result = (List<QCheck>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", 40)
		.list();
		
		return result;
	}
	

}
