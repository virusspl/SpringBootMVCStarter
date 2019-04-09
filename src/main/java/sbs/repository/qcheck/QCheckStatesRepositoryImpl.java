package sbs.repository.qcheck;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qcheck.QCheckState;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qCheckStatesRepositoryImpl")
@Transactional
public class QCheckStatesRepositoryImpl extends GenericRepositoryAdapter<QCheckState, Integer>
		implements QCheckStatesRepository {
	
	@Override
	public QCheckState findByOrder(int order) {
		String hql = "from QCheckState s where s.order = :ord";
		@SuppressWarnings("unchecked")
		List<QCheckState> result = (List<QCheckState>) 
		currentSession()
		.createQuery(hql)
		.setInteger("ord", order)
		.list();
		
		if(!result.isEmpty()){
			return result.get(0);
		}
		else{
			return null;
		}
	}

	
}
