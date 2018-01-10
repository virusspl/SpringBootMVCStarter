package sbs.controller.buyorders;

import java.sql.Timestamp;
import java.util.Locale;

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

import javassist.NotFoundException;
import sbs.model.buyorders.BuyOrder;
import sbs.model.x3.X3Client;
import sbs.service.buyorders.BuyOrdersService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("/buyorders")
public class BuyOrdersController {

	@Autowired
	BuyOrdersService buyOrdersService;
	@Autowired
	UserService userService;
	@Autowired
	MessageSource messageSource;
	@Autowired 
	JdbcOracleX3Service jdbcOracleX3Service;
	
	
	@RequestMapping("/list")
	public String search(Model model) {
		model.addAttribute("orders",buyOrdersService.findAllDesc());
		return "buyorders/list";
	}
	
	@RequestMapping(value = "/create")
	@Transactional
	public String create(BuyOrderCreateForm buyOrderCreateForm, Model model) {
		return "buyorders/create";
	}
	
	@RequestMapping(value = "/create", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String save(@Valid BuyOrderCreateForm buyOrderCreateForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		String itemDesc;
		X3Client client;
		String clientCode;
		
		// validate
		if (bindingResult.hasErrors()) {
			return "buyorders/create";
		}

		// product ODBC
		buyOrderCreateForm.setProduct(buyOrderCreateForm.getProduct().toUpperCase());
		itemDesc = jdbcOracleX3Service.findItemDescription("ATW", buyOrderCreateForm.getProduct());
		if(itemDesc == null){
			bindingResult.rejectValue("product", "error.no.such.product", "ERROR");
			return "buyorders/create";
		}
		
		// order ODBC
		buyOrderCreateForm.setOrder(buyOrderCreateForm.getOrder().toUpperCase());
		clientCode = jdbcOracleX3Service.findFinalClientByOrder("ATW", buyOrderCreateForm.getOrder());
		
		if(clientCode == null){
			bindingResult.rejectValue("order", "error.no.such.order", "ERROR");
			return "buyorders/create";
		}

		// client ODBC - always exists if there is code in order - no validation		
		client = jdbcOracleX3Service.findClientByCode("ATW", clientCode);

		
		BuyOrder buyOrder = new BuyOrder();
		buyOrder.setCreationDate(new Timestamp(new java.util.Date().getTime()));
		buyOrder.setCreator(userService.getAuthenticatedUser());
		buyOrder.setItemCode(buyOrderCreateForm.getProduct().toUpperCase());
		buyOrder.setItemDescription(itemDesc);
		buyOrder.setQuantity(buyOrderCreateForm.getQuantity());
		buyOrder.setCreatorComment(buyOrderCreateForm.getComment());
		buyOrder.setOrderNumber(buyOrderCreateForm.getOrder());
		buyOrder.setClientCode(clientCode);
		buyOrder.setClientName(client.getName());
		buyOrdersService.saveOrUpdate(buyOrder);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		
		return "redirect:/buyorders/list/";
	}
	
	@RequestMapping(value = "/answer/{id}")
	@Transactional
	public String answer(@PathVariable("id") int id,  ResponseForm responseForm, Model model, Locale locale) throws NotFoundException {
		BuyOrder order = buyOrdersService.findById(id);
		if(order == null){
			throw new NotFoundException(messageSource.getMessage("buyorders.notfound", null, locale));
		}
		model.addAttribute("order", order);
		model.addAttribute("responseForm", responseForm);
		return "buyorders/answer";
	}
	
	@RequestMapping(value = "/answer/{id}", params = { "save" }, method = RequestMethod.POST)
	@Transactional
	public String commitAnswer(@Valid ResponseForm responseForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {
		
		BuyOrder order = buyOrdersService.findById(responseForm.getId());
		System.out.println(responseForm.getId() + responseForm.getResponderComment());
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
	

	
	
}
