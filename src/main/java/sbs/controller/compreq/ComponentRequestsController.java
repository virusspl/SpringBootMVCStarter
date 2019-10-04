package sbs.controller.compreq;

import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;
import javax.validation.Valid;

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
import sbs.helpers.DateHelper;
import sbs.model.compreq.ComponentRequest;
import sbs.model.compreq.ComponentRequestLine;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3SalesOrder;
import sbs.model.x3.X3Workstation;
import sbs.service.compreq.ComponentRequestLinesService;
import sbs.service.compreq.ComponentRequestsService;
import sbs.service.downtimes.DowntimesService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;


@Controller
@RequestMapping("compreq")
public class ComponentRequestsController {
	
	@Autowired
	MessageSource messageSource;
	@Autowired 
	UserService userService;
	@Autowired 
	JdbcOracleX3Service x3Service;
	@Autowired
	ComponentRequestsService requestsService;
	@Autowired
	ComponentRequestLinesService linesService;
	@Autowired
	DowntimesService downtimesService;
	@Autowired
	DateHelper dateHelper;
	
	@ModelAttribute("machines")
	public List<X3Workstation> getMachines() {
		// cached
		return x3Service.getWorkstations("ATW");
	}
	
    public ComponentRequestsController() {
    	
    }
    
    @RequestMapping("/list")
    public String dispatch(Model model){
    	model.addAttribute("requests", requestsService.findAllPending());
    	return "compreq/list";
    }
    
    @RequestMapping("/manage/create")
    public String showCreate(Model model){
    	model.addAttribute("formRequestCreate", new FormRequestCreate());
    	return "compreq/create";
    }
    
    @RequestMapping(value = "/manage/create", method = RequestMethod.POST)
	@Transactional
	public String close(@Valid FormRequestCreate formRequestCreate, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

    	formRequestCreate.setComment(formRequestCreate.getComment().trim());
    	formRequestCreate.setOrderNumber(formRequestCreate.getOrderNumber().trim().toUpperCase());
    	formRequestCreate.setProductCode(formRequestCreate.getProductCode().trim().toUpperCase());
    	
    	
		if(bindingResult.hasErrors()){
			return "compreq/create";
		}
		
		X3Product product = x3Service.findProductByCode("ATW", formRequestCreate.getProductCode());
		if (product == null) {
			bindingResult.rejectValue("productCode", "error.no.such.product", "ERROR");
		}
		
		X3SalesOrder order = x3Service.findSalesOrderByNumber("ATW", formRequestCreate.getOrderNumber());
		if (order == null) {
			bindingResult.rejectValue("orderNumber", "error.no.such.order", "ERROR");
		}
		
		if(bindingResult.hasErrors()){
			return "compreq/create";
		}
		
		X3Workstation workstation = x3Service.findWorkstationByCode("ATW", formRequestCreate.getWorkstationCode());
		if(workstation == null){
			throw new NotFoundException("Workstation error: " + formRequestCreate.getWorkstationCode());
		}
		
		ComponentRequest req = new ComponentRequest();
		// background data
		req.setPending(true);
		req.setStartDate(new Timestamp(new java.util.Date().getTime()));
		req.setCreator(userService.getAuthenticatedUser());
		// workstation
		req.setWorkstationCode(workstation.getCode());
		req.setWorkstationName(workstation.getName());
		// product
		req.setProductCode(product.getCode());
		req.setProductDescription(product.getDescription());
		// order
		req.setOrderNumber(order.getSalesNumber());
		req.setClientCode(order.getClientCode());
		req.setClientName(order.getClientName());
		// comment
		req.setComment(formRequestCreate.getComment());

		requestsService.save(req);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/compreq/show/" + req.getId();
		
	}
    
	@RequestMapping(value = "/show/{id}")
	@Transactional
	public String showRequest(@PathVariable("id") int id, Model model) throws NotFoundException {

		ComponentRequest request = requestsService.findById(id);
		if (request == null) {
			throw new NotFoundException("Request not found: #" + id);
		}

		model.addAttribute("rq", request);
		Hibernate.initialize(request.getRequestLines());
		model.addAttribute("lines", request.getRequestLines());

		return "compreq/show";
	}
	
	@RequestMapping(value = "/manage/addline/{id}")
	@Transactional
	public String showAddLine(@PathVariable("id") int id, Model model) throws NotFoundException {
		
		ComponentRequest request = requestsService.findById(id);
		if (request == null) {
			throw new NotFoundException("Request not found: #" + id);
		}
		
		model.addAttribute("request", request);
		model.addAttribute("formCompReqLineCreate", new FormCompReqLineCreate(request.getId()));
		
		return "compreq/addline";
	}
	
    @RequestMapping(value = "/manage/addline", method = RequestMethod.POST)
	@Transactional
	public String addLine(@Valid FormCompReqLineCreate formCompReqLineCreate, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

    	formCompReqLineCreate.setComponentCode(formCompReqLineCreate.getComponentCode().trim().toUpperCase());
    	
    	ComponentRequest request = requestsService.findById(formCompReqLineCreate.getRequestId());
		if (request == null) {
			throw new NotFoundException("Request not found: #" + formCompReqLineCreate.getRequestId());
		}
		
		X3Product product = x3Service.findProductByCode("ATW", formCompReqLineCreate.getComponentCode());
		if (product == null) {
			bindingResult.rejectValue("componentCode", "error.no.such.product", "ERROR");
		}
		
		if(bindingResult.hasErrors()){
			return "compreq/addline";
		}
		
		ComponentRequestLine line = new ComponentRequestLine();
		
		line.setQuantity(formCompReqLineCreate.getQuantity());
		line.setComponentCode(product.getCode());
		line.setComponentDescription(product.getDescription());
		line.setComponentCategory(product.getCategory());
		
		line.setRequest(request);
		linesService.save(line);
		request.getRequestLines().add(line);
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/compreq/show/" + request.getId();
    }
    
	@RequestMapping(value = "/manage/remove/{id}")
	@Transactional
	public String removeLine(@PathVariable("id") int id, RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException {
		
		ComponentRequestLine line = linesService.findById(id);
		if (line == null) {
			throw new NotFoundException("Request line not found: #" + id);
		}
		
		ComponentRequest request = line.getRequest();
		String code = line.getComponentCode();
		request.getRequestLines().remove(line);
		linesService.remove(line);
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.removed", null, locale) + " " + code);
		return "redirect:/compreq/show/" + request.getId();
	}
	
	@RequestMapping(value = "/manage/close/{id}")
	@Transactional
	public String closeRequest(@PathVariable("id") int id, RedirectAttributes redirectAttrs, Locale locale) throws NotFoundException {
		
		ComponentRequest request = requestsService.findById(id);
		if (request == null) {
			throw new NotFoundException("Request not found: #" + id);
		}

		request.setPending(false);
		request.setEndDate(new Timestamp(new java.util.Date().getTime()));
		requestsService.update(request);
		
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.finished", null, locale) + " " + request.getProductCode());
		return "redirect:/compreq/list";
	}
    
	@RequestMapping(value = "/print/{id}")
	@Transactional
	public String showPrint(@PathVariable("id") int id, Model model, Locale locale) throws NotFoundException {
	
		ComponentRequest request = requestsService.findById(id);
		if (request == null) {
			throw new NotFoundException("Request not found: #" + id);
		}

		model.addAttribute("rq", request);
		Hibernate.initialize(request.getRequestLines());
		model.addAttribute("lines", request.getRequestLines());
		model.addAttribute("title", request.getProductCode());
		model.addAttribute("printDate", dateHelper.formatDdMmYyyyHhMm(new java.util.Date()));
		model.addAttribute("startDate", dateHelper.formatDdMmYyyyHhMm(request.getStartDate()));
		
		return "compreq/print";
	}

    
}
