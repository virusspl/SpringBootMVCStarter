package sbs.repository.bhptickets;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.bhptickets.BhpTicketState;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class BhpTicketStateRepositoryImpl extends GenericRepositoryAdapter<BhpTicketState, Integer>
		implements BhpTicketStateRepository {

	@Override
	public BhpTicketState findByOrder(int stateOrderNo) {
		String hql = "from BhpTicketState s where s.order = :stateOrderNo";
		@SuppressWarnings("unchecked")
		List<BhpTicketState> result = (List<BhpTicketState>) currentSession().createQuery(hql).setInteger("stateOrderNo", stateOrderNo).list();
		if (result == null || result.isEmpty()) {
			return null;
		} 
		return result.get(0);
	}

}
