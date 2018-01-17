package sbs.service.proprog;

import java.util.List;

import sbs.model.proprog.Project;
import sbs.service.GenericService;

public interface ProjectProgressService extends GenericService<Project, Integer>{
	
	List<Project> findAllDesc();
	Project findByIdEager(int id);
	
}
