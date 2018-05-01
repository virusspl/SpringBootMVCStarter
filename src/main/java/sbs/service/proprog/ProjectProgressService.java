package sbs.service.proprog;

import java.util.List;

import sbs.model.proprog.ProjectEntity;
import sbs.service.GenericService;

public interface ProjectProgressService extends GenericService<ProjectEntity, Integer>{
	
	List<ProjectEntity> findAllDesc();
	ProjectEntity findByIdEager(int id);
	
}
