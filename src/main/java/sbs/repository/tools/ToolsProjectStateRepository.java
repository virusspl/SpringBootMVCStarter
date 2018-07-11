package sbs.repository.tools;

import sbs.model.tools.ToolsProjectState;
import sbs.repository.GenericRepository;

public interface ToolsProjectStateRepository extends GenericRepository<ToolsProjectState,Integer> {

	public ToolsProjectState findByOrder(int stateOrderNo);
	
}

