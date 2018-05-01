package sbs.repository.proprog;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Repository;

import sbs.model.proprog.Project;
import sbs.model.proprog.ProjectEntity;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class ProjectProgressRepositoryImpl extends GenericRepositoryAdapter<ProjectEntity, Integer>
		implements ProjectProgressRepository {

	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectEntity> findAllDesc() {
		Criteria crit = currentSession().createCriteria(Project.class);
		crit.addOrder(Order.desc("id"));
		return crit.list();
	}
	
	@Override
	@Transactional
	public ProjectEntity findByIdEager(int id){
		ProjectEntity project = currentSession().get(ProjectEntity.class, id);
		if (project != null){
			Hibernate.initialize(project.getBuyPartsUser());
			Hibernate.initialize(project.getClientAcceptUser());
			Hibernate.initialize(project.getCodificationUser());
			Hibernate.initialize(project.getDrawingValidationUser());
			Hibernate.initialize(project.getFirstItemUser());
			Hibernate.initialize(project.getOrderInputUser());
			Hibernate.initialize(project.getProductionPlanUser());
			Hibernate.initialize(project.getProjectNumberUser());
			Hibernate.initialize(project.getTechnologyUser());
			Hibernate.initialize(project.getToolCreationUser());
			Hibernate.initialize(project.getToolDrawingUser());
		}
		return project;
	}
	
	


}
