package sbs.repository.proprog;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import sbs.model.buyorders.BuyOrder;
import sbs.model.proprog.Project;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ProjectProgressRepositoryImpl extends GenericRepositoryAdapter<Project, Integer>
		implements ProjectProgressRepository {

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Project> findAllDesc() {
		Criteria crit = currentSession().createCriteria(Project.class);
		crit.addOrder(Order.desc("id"));
		return crit.list();
	}


}
