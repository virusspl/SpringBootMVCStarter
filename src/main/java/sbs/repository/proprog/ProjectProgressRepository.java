package sbs.repository.proprog;

import java.util.List;

import sbs.model.proprog.ProjectEntity;
import sbs.repository.GenericRepository;

public interface ProjectProgressRepository extends GenericRepository<ProjectEntity,Integer> {

	public List<ProjectEntity> findAllDesc();
	public ProjectEntity findByIdEager(int id);

}

