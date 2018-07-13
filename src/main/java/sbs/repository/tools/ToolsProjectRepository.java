package sbs.repository.tools;

import java.util.List;

import sbs.model.tools.ToolsProject;
import sbs.repository.GenericRepository;

public interface ToolsProjectRepository extends GenericRepository<ToolsProject,Integer> {

	List<ToolsProject> findClosedToolsProjects();
	List<ToolsProject> findAllPendingToolsProjects();
	
}

