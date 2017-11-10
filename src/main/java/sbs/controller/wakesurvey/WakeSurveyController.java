package sbs.controller.wakesurvey;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.model.wakesurvey.WakeQuestion;
import sbs.service.wakesurvey.WakeQuestionsService;

@Controller
@RequestMapping("wakesurvey")
public class WakeSurveyController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	WakeQuestionsService wakeQuestionsService;

	@RequestMapping(value = "/dosurvey/{cnt}")
	@Transactional
	public String doSurvey(@PathVariable("cnt") int cnt, Model model) {
		List<WakeQuestion> questions = wakeQuestionsService.findAll();
		Random r = new Random();
		int idx = r.nextInt(questions.size());
		model.addAttribute("question", questions.get(idx));
		model.addAttribute("cnt", cnt+1);
		
		return "wakesurvey/dosurvey";
	}

	@RequestMapping(value = "/create")
	public String showCreateQuestion(Model model) {
		model.addAttribute("wakeQuestionCreateForm", new WakeQuestionCreateForm());
		return "wakesurvey/create";
	}

	@RequestMapping(value = "/create", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String saveQuestion(@Valid WakeQuestionCreateForm wakeQuestionCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) {

		// validate
		if (bindingResult.hasErrors()) {
			return "wakesurvey/create";
		}

		WakeQuestion wk = new WakeQuestion();
		wk.setText(wakeQuestionCreateForm.getText().trim());
		wakeQuestionsService.save(wk);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/wakesurvey/edit";
	}

	@RequestMapping(value = "/edit")
	@Transactional
	public String editQuestions(Model model) {
		model.addAttribute("questions", wakeQuestionsService.findAll());
		return "wakesurvey/edit";
	}

	@RequestMapping(value = "/edit", params = { "remove" }, method = RequestMethod.POST)
	@Transactional
	public String removeQuestion(RedirectAttributes redirectAttrs, HttpServletRequest req, Locale locale) {
		int questionId = Integer.valueOf(req.getParameter("remove"));
		WakeQuestion wk = wakeQuestionsService.findById(questionId);
		wakeQuestionsService.remove(wk);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.removed", null, locale));
		return "redirect:/wakesurvey/edit/";
	}

}
