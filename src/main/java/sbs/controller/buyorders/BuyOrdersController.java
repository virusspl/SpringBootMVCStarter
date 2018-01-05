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
		
		// validate
		if (bindingResult.hasErrors()) {
			return "buyorders/create";
		}
		
		buyOrderCreateForm.setProduct(buyOrderCreateForm.getProduct().toUpperCase());
		itemDesc = jdbcOracleX3Service.findItemDescription("ATW", buyOrderCreateForm.getProduct());
		
		if(itemDesc == null){
			bindingResult.rejectValue("product", "error.no.such.product", "ERROR");
			return "buyorders/create";
		}
		
		BuyOrder buyOrder = new BuyOrder();
		buyOrder.setCreationDate(new Timestamp(new java.util.Date().getTime()));
		buyOrder.setCreator(userService.getAuthenticatedUser());
		buyOrder.setItemCode(buyOrderCreateForm.getProduct().toUpperCase());
		buyOrder.setItemDescription(itemDesc);
		buyOrder.setQuantity(buyOrderCreateForm.getQuantity());
		buyOrder.setCreatorComment(buyOrderCreateForm.getComment());
		
		buyOrdersService.saveOrUpdate(buyOrder);
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		
		return "redirect:/buyorders/list/";
	}
	
	@RequestMapping(value = "/view/{id}")
	@Transactional
	public String edit(@PathVariable("id") int id,  Model model) throws NotFoundException {
		/*HrUaInfo object = hrUaService.findById(id); 
		if (object == null) {
			throw new NotFoundException("User not found");
		}
		model.addAttribute(HrUaCreateForm.hrUaCreateFormFromHrUaInfo(object));
		*/
		return "hrua/user";
	}
	

	
	
}
