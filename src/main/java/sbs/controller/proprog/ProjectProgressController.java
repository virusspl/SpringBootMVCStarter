package sbs.controller.proprog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javassist.NotFoundException;
import sbs.model.proprog.Project;
import sbs.service.x3.JdbcOracleX3Service;

/**
 * TO USE THIS CONTROLLER, SBS.MODEL.PROJECTENTITY IS REQUIRED TO BE USED. SWAP ITS NAME TO SBS.MODEL.PROJECT
 * ALSO ERASE 'ENTITYVERSION' IN THIS CLASS
 * @author Michalakk
 *
 */

@Controller
@RequestMapping("proprog")
public class ProjectProgressController {

	
	@Autowired
	MessageSource messageSource;
	@Autowired 
	JdbcOracleX3Service x3Service;
	
	@RequestMapping(value = "/list")
	@Transactional
	public String listAll(Model model) {
		ArrayList<ListItem> projects = new ArrayList<>();
		List<Project> list = x3Service.findAllProjectsProgressDesc();
		for (Project project: list){
			projects.add(new ListItem(project));
		}
		model.addAttribute("projects", projects);
		return "proprog/list";
	}

	@RequestMapping(value = "/view/{id}")
	@Transactional
	public String view(@PathVariable("id") int id, Model model, Locale locale) throws NotFoundException {
		Project project = x3Service.findProjectProgressById(id);
		if(project == null){
			throw new NotFoundException(messageSource.getMessage("proprog.notfound", null, locale));
		}
		model.addAttribute("project", project);
		model.addAttribute("progress", project.getProgressTotal());
		model.addAttribute("color", project.getProgressBootstrapTitle());
		model.addAttribute("checklistForm", new ChecklistForm(id));
		return "proprog/view";
	}
}

/*
USER ROLES:

PROPROG_SALES
private User projectNumberUser;	
private User clientAcceptUser;
private User codificationUser;	
PROPROG_DRAWINGVALIDATION
private User drawingValidationUser;
PROPROG_SALESORDER
private User orderInputUser;
PROPROG_PRODUCTIONPLAN
private User productionPlanUser;
PROPROG_PURCHASE
private User buyPartsUser;
PROPROG_TECHNOLOGY
private User technologyUser;
PROPROG_TOOLDRAWING
private User toolDrawingUser;
PROPROG_TOOLCREATION
private User toolCreationUser
PROPROG_QUALITYCHECK
private User firstItemUser;
*/
