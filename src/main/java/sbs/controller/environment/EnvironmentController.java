package sbs.controller.environment;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.config.error.NotFoundException;
import sbs.helpers.DateHelper;
import sbs.helpers.TextHelper;
import sbs.model.x3.X3EnvironmentInfo;
import sbs.model.x3.X3SalesOrderLine;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("environment")
public class EnvironmentController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	TextHelper textHelper;
	@Autowired
	DateHelper dateHelper;

	@RequestMapping("/dispatch")
	public String view(Model model, Locale locale) {
		EnvironmentForm environmentForm = new EnvironmentForm();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, -7);
		environmentForm.setStartDate(new Timestamp(cal.getTimeInMillis()));
		cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		
		environmentForm.setEndDate(new Timestamp(cal.getTimeInMillis()));
		model.addAttribute("environmentForm", environmentForm);
		return "environment/dispatch";
	}

	@RequestMapping(value = "/makelist", params = { "type" }, method = RequestMethod.POST)
	public String viewList(@Valid EnvironmentForm environmentForm, @RequestParam String type, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttrs, Locale locale) {
		
		if (bindingResult.hasErrors()) {
			return "environment/dispatch";
		}
		
		List<X3EnvironmentInfo> list = x3Service.getEnvironmentInfoInPeriod(environmentForm.getStartDate(), environmentForm.getEndDate(), type, environmentForm.getCompany());

		redirectAttrs.addFlashAttribute("list", list);
		redirectAttrs.addFlashAttribute("type", type);
		redirectAttrs.addFlashAttribute("company", environmentForm.getCompany());
		redirectAttrs.addFlashAttribute("startDate", dateHelper.formatDdMmYyyy(environmentForm.getStartDate()));
		redirectAttrs.addFlashAttribute("endDate", dateHelper.formatDdMmYyyy(environmentForm.getEndDate()));
		
		return "redirect:/environment/view";		

	}
	
	@RequestMapping("/view")
	public String showView() {
		return "environment/view";
		
	}

}
