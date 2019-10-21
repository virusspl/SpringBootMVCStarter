package sbs.repository.redmine;

import java.util.Map;

import sbs.model.redmine.RedmineProject;
import sbs.model.redmine.RedmineUser;


public interface RedmineRepository  {

    public Map<Integer, RedmineProject> getPlProjects();
    public Map<Integer, RedmineUser> getPlUsers();
    
}
