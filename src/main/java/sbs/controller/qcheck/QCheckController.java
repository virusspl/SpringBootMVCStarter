package sbs.controller.qcheck;

import java.sql.Timestamp;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import javax.transaction.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javassist.NotFoundException;
import sbs.model.qcheck.QCheck;
import sbs.model.qcheck.QCheckState;
import sbs.model.x3.X3Product;
import sbs.service.qcheck.QCheckService;
import sbs.service.qcheck.QCheckStatesService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("/qcheck")
public class QCheckController {

	@Autowired
	UserService userService;
	@Autowired
	QCheckService checksService;
	@Autowired
	QCheckStatesService statesService;
	@Autowired
	MessageSource messageSource;
	@Autowired 
	JdbcOracleX3Service jdbcOracleX3Service;
	
	/*
	+ @RequestMapping("/list")
	+ @RequestMapping("/archive")
	@RequestMapping("/create")
	@RequestMapping("/edit")
	@RequestMapping("/show")
	*/
	
	
	@RequestMapping("/list")
	public String current(Model model) {
		model.addAttribute("list",checksService.findAllPending());
		return "qcheck/list";
	}
	
	@RequestMapping("/archive")
	public String closed(Model model) {
		model.addAttribute("list",checksService.findAllClosed());
		return "qcheck/list";
	}
	
	@RequestMapping(value = "/create")
	@Transactional
	public String create(Model model) {
		model.addAttribute("QCheckCreateForm", new QCheckCreateForm());
		return "qcheck/create";
	}
	
	@RequestMapping(value = "/create", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String save(@Valid QCheckCreateForm qCheckCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {
		
		// validate
		if (bindingResult.hasErrors()) {
			return "qcheck/create";
		}
		
		X3Product product = jdbcOracleX3Service.findProductByCode("ATW", qCheckCreateForm.getProductCode().trim());
		if(product == null){
			bindingResult.rejectValue("productCode", "error.no.such.product", "ERROR");
			return "qcheck/create";
		}

		QCheckState stateNew = statesService.findByOrder(10);
		if(stateNew == null){
			throw new NotFoundException("state \"new\" not found");
		}
		
		QCheck check = new QCheck();
		check.setProductCode(product.getCode());
		check.setProductDescription(product.getDescription());
		check.setContents(qCheckCreateForm.getContents().trim());
		check.setCreator(userService.getAuthenticatedUser());
		check.setCreationDate(new Timestamp(new java.util.Date().getTime()));
		check.setState(stateNew);
		checksService.save(check);
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/qcheck/show/"+check.getId();
	}
	
	@RequestMapping(value = "/show/{id}")
	@Transactional
	public String answer(@PathVariable("id") int id,  ResponseForm responseForm, Model model, Locale locale) throws NotFoundException {
		QCheck check = checksService.findById(id);
		if(check == null){
			throw new NotFoundException(messageSource.getMessage("qcheck.notfound", null, locale));
		}
		model.addAttribute("check", check);
		System.out.println(check);
		// TODO show view, answer, mails
		return "qcheck/show";
	}
	
	
	/*
	 
	
	

	
	@RequestMapping(value = "/answer/{id}", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String commitAnswer(@Valid ResponseForm responseForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {
		
		BuyOrder order = buyOrdersService.findById(responseForm.getId());
		// validate
		if (bindingResult.hasErrors()) {
			if(order == null){
				throw new NotFoundException(messageSource.getMessage("buyorders.notfound", null, locale));
			}
			model.addAttribute("order", order);
			return "buyorders/answer";
		}
		
		order.setResponder(userService.getAuthenticatedUser());
		order.setResponseDate(new Timestamp(new java.util.Date().getTime()));
		order.setResponderComment(responseForm.getResponderComment().trim());
		buyOrdersService.saveOrUpdate(order);
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		
		return "redirect:/buyorders/list/";
	}
	
*/
	
	
}
