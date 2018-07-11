package sbs.service.tools;

import sbs.model.tools.ToolsProjectState;
import sbs.service.GenericService;

public interface ToolsProjectStateService extends GenericService<ToolsProjectState, Integer>{

	public ToolsProjectState findByOrder(int stateOrderNo);

}
