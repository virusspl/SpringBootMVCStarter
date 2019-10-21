package sbs.service.redmine;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sbs.model.redmine.RedmineProject;
import sbs.model.redmine.RedmineUser;
import sbs.repository.redmine.RedmineRepository;

@Service
public class RedmineServiceImpl implements RedmineService {
	@Autowired
	RedmineRepository redmineRepository;

	@Override
	public Map<Integer, RedmineProject> getPlProjects() {
		return redmineRepository.getPlProjects();
	}

	@Override
	public Map<Integer, RedmineUser> getPlUsers() {
		return redmineRepository.getPlUsers();
	}

}
