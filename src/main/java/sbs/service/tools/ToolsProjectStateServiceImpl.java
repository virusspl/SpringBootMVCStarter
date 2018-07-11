package sbs.service.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.tools.ToolsProjectState;
import sbs.repository.GenericRepository;
import sbs.repository.tools.ToolsProjectStateRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class ToolsProjectStateServiceImpl extends GenericServiceAdapter<ToolsProjectState, Integer> implements ToolsProjectStateService{
	
	
	private ToolsProjectStateRepository toolsProjectStateRepository;
	
    @Autowired
	public ToolsProjectStateServiceImpl(@Qualifier("toolsProjectStateRepositoryImpl") GenericRepository<ToolsProjectState, Integer> genericRepository) {
			super(genericRepository);
			this.toolsProjectStateRepository = (ToolsProjectStateRepository) genericRepository;
	}

	@Override
	public ToolsProjectState findByOrder(int stateOrderNo) {
		return toolsProjectStateRepository.findByOrder(stateOrderNo);
	}

}
