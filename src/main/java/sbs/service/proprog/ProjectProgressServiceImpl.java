package sbs.service.proprog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.proprog.ProjectEntity;
import sbs.repository.GenericRepository;
import sbs.repository.proprog.ProjectProgressRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class ProjectProgressServiceImpl extends GenericServiceAdapter<ProjectEntity, Integer> implements ProjectProgressService{
	
	private ProjectProgressRepository projectProgressRepository;
	
    @Autowired
	public ProjectProgressServiceImpl(@Qualifier("projectProgressRepositoryImpl") GenericRepository<ProjectEntity, Integer> genericRepository) {
			super(genericRepository);
			this.projectProgressRepository = (ProjectProgressRepository) genericRepository;
	}

	@Override
	public List<ProjectEntity> findAllDesc() {
		return projectProgressRepository.findAllDesc();
	}

	@Override
	public ProjectEntity findByIdEager(int id) {
		return projectProgressRepository.findByIdEager(id);
	}	
	

}
