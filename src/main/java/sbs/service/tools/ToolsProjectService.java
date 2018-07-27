package sbs.service.tools;

import java.util.List;

import sbs.model.tools.ToolsProject;
import sbs.model.users.User;
import sbs.service.GenericService;

public interface ToolsProjectService extends GenericService<ToolsProject, Integer>{

	public List<ToolsProject> findClosedToolsProjects();
	public List<ToolsProject> findAllPendingToolsProjects();
	public List<ToolsProject> findPendingToolsProjectsByUserDescByPriority(User user);
}
