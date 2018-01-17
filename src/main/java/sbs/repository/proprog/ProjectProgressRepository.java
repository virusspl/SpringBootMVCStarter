package sbs.repository.proprog;

import java.util.List;

import sbs.model.proprog.Project;
import sbs.repository.GenericRepository;

public interface ProjectProgressRepository extends GenericRepository<Project,Integer> {

	public List<Project> findAllDesc();
	public Project findByIdEager(int id);

}

