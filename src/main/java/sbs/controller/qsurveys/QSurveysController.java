package sbs.controller.qsurveys;

import java.util.Locale;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import sbs.controller.inventory.InventoryColumnCreateForm;
import sbs.model.inventory.Inventory;
import sbs.model.inventory.InventoryColumn;
import sbs.model.inventory.InventoryDataType;
import sbs.model.qsurveys.QSurveyQuestion;
import sbs.model.qsurveys.QSurveyQuestionType;
import sbs.model.qsurveys.QSurveyTemplate;
import sbs.service.optima.JdbcAdrOptimaService;
import sbs.service.qsurveys.QSurveyQuestionTypesService;
import sbs.service.qsurveys.QSurveyQuestionsService;
import sbs.service.qsurveys.QSurveyTemplatesService;
import sbs.service.qsurveys.QSurveysService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("qsurveys")
public class QSurveysController {
	
	@Autowired
	QSurveySessionInfo sessionInfo;
	@Autowired
	QSurveysService surveysService;
	@Autowired
	QSurveyTemplatesService templatesService;
	@Autowired
	QSurveyQuestionTypesService qSurveyQuestionTypesService;
	@Autowired
	QSurveyQuestionsService qSurveyQuestionsService;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	JdbcAdrOptimaService hrService;
	@Autowired
	MessageSource messageSource;
	
	@ModelAttribute("sessionInfo")
	public QSurveySessionInfo sessionInfo() {
		return sessionInfo;
	}
	
	@RequestMapping(value = "/dispatch")
	public String dispatch(){
		return "qsurveys/dispatch";
	}
	
	@RequestMapping(value = "/templates")
	public String templates(Model model) {
		model.addAttribute("templates", templatesService.findAll());
		return "qsurveys/templates";
	}
	
	@RequestMapping(value = "/templates/create")
	public String createTemplate(Model model) {
		model.addAttribute("formTemplateCreate", new FormTemplateCreate());
		return "qsurveys/createtemplate";
	}

	@RequestMapping(value = "/templates/create", method = RequestMethod.POST)
	@Transactional
	public String createTemplate(@Valid FormTemplateCreate formTemplateCreate, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {
		
		// validate
		if (bindingResult.hasErrors()) {
			return "qsurveys/createtemplate";
		}
		
		// create project object
		QSurveyTemplate template = new QSurveyTemplate();

		template.setActive(false);
		
		// form fields
		template.setTitle(formTemplateCreate.getTitle());
		template.setDescription(formTemplateCreate.getDescription());
		template.setComment(formTemplateCreate.getComment());

		// save
		templatesService.save(template);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/questions/" + template.getId();

	}
	
	@RequestMapping("/templates/edit/{id}")
	public String editTemplateView(@PathVariable("id") int id, Model model) throws NotFoundException {
		QSurveyTemplate template = templatesService.findById(id);
		if (template == null) {
			throw new NotFoundException("Template not found");
		}
		FormTemplateEdit formTemplateEdit = new FormTemplateEdit();
		formTemplateEdit.setId(template.getId());
		formTemplateEdit.setActive(template.getActive());
		formTemplateEdit.setTitle(template.getTitle());
		formTemplateEdit.setDescription(template.getDescription());
		formTemplateEdit.setComment(template.getComment());

		model.addAttribute("formTemplateEdit", formTemplateEdit);

		return "qsurveys/edittemplate";
	}
	
	@RequestMapping(value = "/templates/edit", method = RequestMethod.POST)
	@Transactional
	public String editTemplate(@Valid FormTemplateEdit formTemplateEdit, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {
		
		
		QSurveyTemplate template = templatesService.findById(formTemplateEdit.getId());
		if (template == null) {
			throw new NotFoundException("Template not found");
		}

		// validate
		if (bindingResult.hasErrors()) {
			return "qsurveys/edittemplate";
		}
	
		// form fields
		template.setTitle(formTemplateEdit.getTitle());
		template.setDescription(formTemplateEdit.getDescription());
		template.setComment(formTemplateEdit.getComment());
		template.setActive(formTemplateEdit.isActive());

		// save
		templatesService.save(template);

		// redirect
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/templates/edit/" + template.getId();

	}
	
	@RequestMapping("/questions/{id}")
	@Transactional
	public String editQuestionsView(@PathVariable("id") int id, Model model) throws NotFoundException {
		QSurveyTemplate template = templatesService.findById(id);
		if (template == null) {
			throw new NotFoundException("Template not found");
		}

		model.addAttribute("template", template);
		model.addAttribute("questions", qSurveyQuestionsService.findListByTemplateIdAsc(template.getId()));

		return "qsurveys/questions";
	}
	
	@RequestMapping("/questions/add/{id}")
	@Transactional
	public String addQuestionsView(@PathVariable("id") int id, Model model) throws NotFoundException {
		QSurveyTemplate template = templatesService.findById(id);
		if (template == null) {
			throw new NotFoundException("Template not found");
		}
		FormQuestionCreate formQuestionCreate = new FormQuestionCreate();
		formQuestionCreate.setTemplateId(template.getId());

		model.addAttribute("formQuestionCreate", formQuestionCreate);
		sessionInfo.setTypes(qSurveyQuestionTypesService.findAll());

		return "qsurveys/createquestion";
	}

	@RequestMapping(value = "/questions/add", method = RequestMethod.POST)
	@Transactional
	public String addQuestion(@Valid FormQuestionCreate formQuestionCreate, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		QSurveyTemplate template = templatesService.findById(formQuestionCreate.getTemplateId());
		if (template == null) {
			throw new NotFoundException("Template not found");
		}
		
		// validate
		if (bindingResult.hasErrors()) {
			return "qsurveys/createquestion";
		}
		
		QSurveyQuestion question = new QSurveyQuestion();
		question.setOrder(formQuestionCreate.getOrder());
		question.setShortText(formQuestionCreate.getShortText());
		question.setLongText(formQuestionCreate.getLongText());
		
		QSurveyQuestionType type = qSurveyQuestionTypesService.findById(formQuestionCreate.getTypeId());
		if (type == null) {
			throw new NotFoundException("Unknown question type");
		}
		
		question.setType(type);
		question.setTemplate(template);
		
		qSurveyQuestionsService.save(question);
		template.getQuestions().add(question);
		
		redirectAttrs.addFlashAttribute("msg",
				messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qsurveys/questions/" + formQuestionCreate.getTemplateId();
	}
	
	@RequestMapping("/questions/delete/{templateId}/{questionId}")
	@Transactional
	public String deleteQuestion(@PathVariable("templateId") int templateId, @PathVariable("questionId") int questionId,
			RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException {
		QSurveyQuestion question = qSurveyQuestionsService.findById(questionId);
		if (question != null) {
			qSurveyQuestionsService.remove(question);
			redirectAttrs.addFlashAttribute("msg",
					messageSource.getMessage("action.removed", null, locale) + ": " + question.getShortText());
			return "redirect:/qsurveys/questions/" + templateId;
		} else {
			throw new NotFoundException("Question not found");
		}
	}
	
	
	@RequestMapping(value = "/create")
	public String createSurvey(Model model) {
		sessionInfo.setTemplates(templatesService.findAllActiveAscByTitle());
		sessionInfo.setWorkers(hrService.findAllCurrentlyEmployed());
		model.addAttribute("formSurveyCreate", new FormSurveyCreate());
		return "qsurveys/create";
	}
	

}
