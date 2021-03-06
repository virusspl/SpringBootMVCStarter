package sbs.repository.tools;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import sbs.model.tools.ToolsProject;
import sbs.model.users.User;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ToolsProjectRepositoryImpl extends GenericRepositoryAdapter<ToolsProject, Integer>
		implements ToolsProjectRepository {

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ToolsProject> findToolsProjectsByStateOrder(int order) {
		Criteria crit = currentSession().createCriteria(ToolsProject.class, "project");
		crit.createAlias("project.state", "state");
		crit.add(Restrictions.eq("state.order", order));
		return crit.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ToolsProject> findAllPendingToolsProjects() {
		Criteria crit = currentSession().createCriteria(ToolsProject.class, "project");
		crit.createAlias("project.state", "state");
		crit.add(Restrictions.lt("state.order", 50));
		crit.add(Restrictions.ne("state.order", 20));
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ToolsProject> findPendingToolsProjectsByUserDescByPriority(User user) {
		Criteria crit = currentSession().createCriteria(ToolsProject.class, "project");
		crit.add(Restrictions.eq("assignedUser", user));
		crit.createAlias("project.state", "state");
		crit.add(Restrictions.lt("state.order", 50));
		crit.addOrder(Order.desc("priority"));
		return crit.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ToolsProject findByCechOld(String cechOld) {
		Criteria crit = currentSession().createCriteria(ToolsProject.class);
		crit.add(Restrictions.eq("cechOld", cechOld.toUpperCase()));
		List<ToolsProject> list = crit.list();
		if(list.isEmpty()){
			return null;
		}
		else {
			return (ToolsProject)crit.list().get(0);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public ToolsProject findByCechNew(String cechNew) {
		Criteria crit = currentSession().createCriteria(ToolsProject.class);
		crit.add(Restrictions.eq("cechNew", cechNew.toUpperCase()));
		List<ToolsProject> list = crit.list();
		if(list.isEmpty()){
			return null;
		}
		else {
			return (ToolsProject)crit.list().get(0);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ToolsProject> findAllToolsProjects() {
		Criteria crit = currentSession().createCriteria(ToolsProject.class, "project");
		crit.createAlias("project.state", "state");
		crit.add(Restrictions.lt("state.order", 90));
		return crit.list();
	}



}
