package sbs.service.tools;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.tools.ToolsProject;
import sbs.repository.GenericRepository;
import sbs.repository.tools.ToolsProjectRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class ToolsProjectServiceImpl extends GenericServiceAdapter<ToolsProject, Integer> implements ToolsProjectService{
	
	
	private ToolsProjectRepository toolsProjectRepository;
	
    @Autowired
	public ToolsProjectServiceImpl(@Qualifier("toolsProjectRepositoryImpl") GenericRepository<ToolsProject, Integer> genericRepository) {
			super(genericRepository);
			this.toolsProjectRepository = (ToolsProjectRepository) genericRepository;
	}

	@Override
	public List<ToolsProject> findClosedToolsProjects() {
		return toolsProjectRepository.findClosedToolsProjects();
	}

	@Override
	public List<ToolsProject> findAllPendingToolsProjects() {
		return toolsProjectRepository.findAllPendingToolsProjects();
	}


	

}
