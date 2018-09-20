package sbs.repository.tools;

import java.util.List;

import sbs.model.tools.ToolsProject;
import sbs.model.users.User;
import sbs.repository.GenericRepository;

public interface ToolsProjectRepository extends GenericRepository<ToolsProject,Integer> {

	List<ToolsProject> findAllPendingToolsProjects();
	List<ToolsProject> findToolsProjectsByStateOrder(int order);
	List<ToolsProject> findPendingToolsProjectsByUserDescByPriority(User user);
	ToolsProject findByCechOld(String cechOld);
	ToolsProject findByCechNew(String cechNew);
	List<ToolsProject> findAllToolsProjects();
	
	
}

