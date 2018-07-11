package sbs.repository.tools;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.tools.ToolsProjectState;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ToolsProjectStateRepositoryImpl extends GenericRepositoryAdapter<ToolsProjectState, Integer>
		implements ToolsProjectStateRepository {

	@Override
	public ToolsProjectState findByOrder(int stateOrderNo) {
		String hql = "from ToolsProjectState s where s.order = :stateOrderNo";
		@SuppressWarnings("unchecked")
		List<ToolsProjectState> result = (List<ToolsProjectState>) currentSession().createQuery(hql).setInteger("stateOrderNo", stateOrderNo).list();
		if (result == null || result.isEmpty()) {
			return null;
		} 
		return result.get(0);
	}

}
