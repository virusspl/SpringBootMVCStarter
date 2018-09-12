package sbs.service.tools;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.tools.ToolsProject;
import sbs.model.users.User;
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
	public List<ToolsProject> findAllPendingToolsProjects() {
		return toolsProjectRepository.findAllPendingToolsProjects();
	}

	@Override
	public List<ToolsProject> findPendingToolsProjectsByUserDescByPriority(User user) {
		return toolsProjectRepository.findPendingToolsProjectsByUserDescByPriority(user);
	}

	@Override
	public boolean isCechOldInUse(String cechOld) {
		ToolsProject project = toolsProjectRepository.findByCechOld(cechOld);
		return project==null ? false : true;
	}
	
	@Override
	public boolean isCechNewInUse(String cechNew) {
		ToolsProject project = toolsProjectRepository.findByCechNew(cechNew);
		return project==null ? false : true;
	}

	@Override
	public List<ToolsProject> findToolsProjectsByStateOrder(int order) {
		return toolsProjectRepository.findToolsProjectsByStateOrder(order);
	}


	

}
