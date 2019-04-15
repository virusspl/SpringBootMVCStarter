package sbs.repository.qcheck;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.qcheck.QCheckAction;
import sbs.repository.GenericRepositoryAdapter;

@Repository("qCheckActionsRepositoryImpl")
@Transactional
public class QCheckActionsRepositoryImpl extends GenericRepositoryAdapter<QCheckAction, Integer>
		implements QCheckActionsRepository {

	@Override
	public List<QCheckAction> findByCheckId(int id) {
		String hql = "from QCheckAction a WHERE a.check.id = :id ORDER BY a.id ASC";
		@SuppressWarnings("unchecked")
		List<QCheckAction> result = (List<QCheckAction>) currentSession().createQuery(hql).setInteger("id", id).list();
		return result;
	}
	

}
