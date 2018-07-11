package sbs.repository.tools;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sbs.model.tools.ToolsProject;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ToolsProjectRepositoryImpl extends GenericRepositoryAdapter<ToolsProject, Integer>
		implements ToolsProjectRepository {


	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ToolsProject> findArchivedToolsProjects() {
		Criteria crit = currentSession().createCriteria(ToolsProject.class, "project");
		crit.createAlias("project.state", "state");
		crit.add(Restrictions.ge("state.order", 50));
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ToolsProject> findAllPendingToolsProjects() {
		Criteria crit = currentSession().createCriteria(ToolsProject.class, "project");
		crit.createAlias("project.state", "state");
		crit.add(Restrictions.lt("state.order", 50));
		return crit.list();
	}



}
