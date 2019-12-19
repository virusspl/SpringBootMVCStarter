package sbs.controller.downtimes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javassist.NotFoundException;
import sbs.controller.auth.HrUserInfoSessionHolder;
import sbs.model.downtimes.Downtime;
import sbs.model.downtimes.DowntimeCause;
import sbs.model.downtimes.DowntimeDetailsFailure;
import sbs.model.downtimes.DowntimeDetailsMaterial;
import sbs.model.downtimes.DowntimeResponseType;
import sbs.model.downtimes.DowntimeType;
import sbs.model.users.User;
import sbs.model.x3.X3Product;
import sbs.model.x3.X3UtrFault;
import sbs.model.x3.X3Workstation;
import sbs.service.downtimes.DowntimeCausesService;
import sbs.service.downtimes.DowntimeDetailsFailureService;
import sbs.service.downtimes.DowntimeDetailsMaterialService;
import sbs.service.downtimes.DowntimeResponseTypesService;
import sbs.service.downtimes.DowntimeTypesService;
import sbs.service.downtimes.DowntimesService;
import sbs.service.mail.MailService;
import sbs.service.users.UserService;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("downtimes")
public class DowntimesController {

	@Autowired
	MessageSource messageSource;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	DowntimeCausesService causesService;
	@Autowired
	DowntimeDetailsMaterialService materialDetailsService;
	@Autowired
	DowntimeDetailsFailureService failureDetailsService;
	@Autowired
	DowntimesService downtimesService;
	@Autowired
	DowntimeTypesService typesService;
	@Autowired
	HrUserInfoSessionHolder userHolder;
	@Autowired
	UserService userService;
	@Autowired
	DowntimeResponseTypesService responseTypeService;
	@Autowired
	MailService mailService;
	@Autowired
	TemplateEngine templateEngine;
	

	private List<DowntimeCause> currentCauses;
	private DowntimeType currentType;

	@ModelAttribute("userHolder")
	public HrUserInfoSessionHolder getUserHolder() {
		return userHolder;
	}

	@ModelAttribute("causes")
	public List<DowntimeCause> getCauses() {
		return currentCauses;
	}

	@ModelAttribute("currentType")
	public DowntimeType getCurrentType() {
		return currentType;
	}

	@ModelAttribute("machines")
	public List<X3Workstation> getMachines() {
		return x3Service.getWorkstations("ATW");
	}

	@RequestMapping(value = "/dispatch")
	public String dispatch(Model model) {
		User current = userService.getAuthenticatedUser();
		if (current != null) {
			List<Downtime> waiting = downtimesService.findWithoutResponseForUser(current.getId());
			if (waiting.size() > 0) {
				model.addAttribute("waiting", waiting);
			}
		}
		model.addAttribute("downtimes", downtimesService.findAllPending());
		return "downtimes/dispatch";
	}

	@RequestMapping(value = "/create/{type}")
	public String showCreate(@PathVariable("type") String type, Model model, Locale locale) throws NotFoundException {

		switch (type) {
		case DowntimeCausesService.CAUSE_FAULT:
		case DowntimeCausesService.CAUSE_MATERIAL:
		case DowntimeCausesService.CAUSE_QUALITY:
		case DowntimeCausesService.CAUSE_SAFETY:
		case DowntimeCausesService.CAUSE_OTHER:
			this.currentCauses = causesService.findByType(type, true);
			this.currentType = typesService.findByInternalTitle(type);
			break;
		default:
			this.currentCauses = null;
			this.currentType = null;
			throw new NotFoundException(
					messageSource.getMessage("downtimes.error.type.unknown", null, locale) + ": '" + type + "'");
		}

		model.addAttribute("causes", currentCauses);
		model.addAttribute("formDowntimeCreate", new FormDowntimeCreate(type));
		model.addAttribute("currentType", currentType);

		return "downtimes/create";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Transactional
	public String addCause(@Valid FormDowntimeCreate formDowntimeCreate, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException, UnknownHostException, MessagingException {

		if (!userHolder.isSet()) {
			redirectAttrs.addFlashAttribute("error",
					messageSource.getMessage("error.user.not.authenticated", null, locale));
			return "redirect:/industry/dispatch";
		}

		Downtime downtime = null;
		DowntimeDetailsFailure failure = null;
		DowntimeDetailsMaterial material = null;
		

		// validate special fields
		if (formDowntimeCreate.getTypeInternalTitle().equals(DowntimeCausesService.CAUSE_FAULT)) {
			// FAULT, take X3 number
			formDowntimeCreate.setX3FailureNumber(formDowntimeCreate.getX3FailureNumber().trim().toUpperCase());
			if (formDowntimeCreate.getX3FailureNumber() != null
					&& formDowntimeCreate.getX3FailureNumber().length() > 0) {
				X3UtrFault fault = x3Service.findUtrFault(formDowntimeCreate.getX3FailureNumber().trim());
				if (fault == null) {
					bindingResult.rejectValue("x3FailureNumber", "downtimes.error.failure.notfound", "ERROR");
				} else {
					failure = new DowntimeDetailsFailure(fault);
				}
			}

		} else if (formDowntimeCreate.getTypeInternalTitle().equals(DowntimeCausesService.CAUSE_MATERIAL)) {

			// MATERIAL, check product in X3
			formDowntimeCreate.setProductCode(formDowntimeCreate.getProductCode().trim().toUpperCase());
			X3Product product = x3Service.findProductByCode("ATW", formDowntimeCreate.getProductCode());
			if (product == null) {
				bindingResult.rejectValue("productCode", "error.no.such.product", "ERROR");
			} else {
				material = new DowntimeDetailsMaterial();
				material.setProductCode(product.getCode());
				material.setProductDescription(product.getDescription());
				material.setProductCategory(product.getCategory());
			}
		}

		if (bindingResult.hasErrors()) {
			return "downtimes/create";
		}

		// check basic structure fields
		DowntimeType type = typesService.findByInternalTitle(formDowntimeCreate.getTypeInternalTitle());
		if (type == null) {
			throw new NotFoundException("Downtime type not found: " + formDowntimeCreate.getTypeInternalTitle());
		}

		X3Workstation workstation = x3Service.findWorkstationByCode("ATW", formDowntimeCreate.getMachineCode());
		if (workstation == null) {
			throw new NotFoundException("Workstation not found: " + formDowntimeCreate.getMachineCode());
		}

		DowntimeCause cause = causesService.findById(formDowntimeCreate.getCauseId());
		if (cause == null) {
			throw new NotFoundException("Downtime cause not found: " + formDowntimeCreate.getMachineCode());
		}

		DowntimeResponseType responseType = responseTypeService.findByInternalTitle("empty");
		if (responseType == null) {
			throw new NotFoundException("Response type not found: " + "empty");
		}		
		
		// SAVE
		downtime = new Downtime();
		downtime.setType(type);
		downtime.setCause(cause);
		downtime.setResponseType(responseType);
		downtime.setOpened(true);
		downtime.setMachineCode(workstation.getCode());
		downtime.setMachineName(workstation.getName());
		downtime.setComment(formDowntimeCreate.getComment().trim());
		downtime.setStartDate(new Timestamp(new java.util.Date().getTime()));
		downtime.setInitRcpNumber(userHolder.getInfo().getRcpNumber());
		downtime.setInitFirstName(userHolder.getInfo().getFirstName());
		downtime.setInitLastName(userHolder.getInfo().getLastName());
		downtime.setInitPosition(userHolder.getInfo().getPosition());
		downtime.setInitDepartment(userHolder.getInfo().getDepartment());
		

		downtimesService.save(downtime);

		// save child rows
		if (material != null) {
			material.setDowntime(downtime);
			materialDetailsService.save(material);
		} else if (failure != null) {
			failure.setDowntime(downtime);
			failureDetailsService.save(failure);
		}
		
		// send e-mail notification
		List<String> recipients = new ArrayList<>();
		recipients.add(downtime.getCause().getResponsibleUser().getEmail());
		
		String title = messageSource.getMessage(downtime.getType().getCode(), null, locale) + " - " + downtime.getMachineCode() + " [Downtimes]" 
				
				;
		this.sendMail(title, downtime, recipients);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/downtimes/dispatch";
	}
	
	private void sendMail(String title, Downtime downtime, List<String> recipients) throws UnknownHostException, MessagingException {

		ArrayList<String> mailTo = new ArrayList<>();
		for (String addr : recipients) {
			mailTo.add(addr);
		}

		Context context = new Context();
		context.setVariable("host", InetAddress.getLocalHost().getHostAddress());
		context.setVariable("title", title);
		context.setVariable("dt", downtime);
		String body = templateEngine.process("downtimes/mailtemplate", context);
		if(mailTo.size()>0){
			mailService.sendEmail("webapp@atwsystem.pl", mailTo.toArray(new String[0]), new String[0], title, body);
		}
	}

	@RequestMapping(value = "/show/{id}")
	public String showDowntime(@PathVariable("id") int id, Model model) throws NotFoundException {

		Downtime downtime = downtimesService.findById(id);
		if (downtime == null) {
			throw new NotFoundException("Downtime not found: #" + id);
		}

		model.addAttribute("dt", downtime);
		model.addAttribute("formDowntimeClose", new FormDowntimeClose(downtime.getId()));

		if (!downtime.getFailureDetails().isEmpty()) {
			model.addAttribute("ddf", downtime.getFailureDetails().toArray()[0]);
		}

		if (!downtime.getMaterialDetails().isEmpty()) {
			model.addAttribute("ddm", downtime.getMaterialDetails().toArray()[0]);
		}

		User user = userService.getAuthenticatedUser();
		if (user != null && user.getId() == downtime.getCause().getResponsibleUser().getId()) {
			model.addAttribute("responseAllowed", true);
		}

		return "downtimes/show";
	}

	@RequestMapping(value = "/close", method = RequestMethod.POST)
	@Transactional
	public String close(@Valid FormDowntimeClose formDowntimeClose, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model) throws NotFoundException {

		if (!userHolder.isSet()) {
			redirectAttrs.addFlashAttribute("error",
					messageSource.getMessage("error.user.not.authenticated", null, locale));
			return "redirect:/industry/dispatch";
		}

		Downtime downtime = downtimesService.findById(formDowntimeClose.getDowntimeId());
		if (downtime == null) {
			throw new NotFoundException("Downtime not found: #" + formDowntimeClose.getDowntimeId());
		}

		if (bindingResult.hasErrors()) {
			User user = userService.getAuthenticatedUser();
			if (user != null && user.getId() == downtime.getCause().getResponsibleUser().getId()) {
				model.addAttribute("responseAllowed", true);
			}
			model.addAttribute("dt", downtime);
			if (!downtime.getFailureDetails().isEmpty()) {
				model.addAttribute("ddf", downtime.getFailureDetails().toArray()[0]);
			}
			if (!downtime.getMaterialDetails().isEmpty()) {
				model.addAttribute("ddm", downtime.getMaterialDetails().toArray()[0]);
			}
			return "downtimes/show";
		}

		downtime.setOpened(false);
		downtime.setEndComment(formDowntimeClose.getEndComment().trim());
		downtime.setEndDate(new Timestamp(new java.util.Date().getTime()));
		downtime.setEndRcpNumber(userHolder.getInfo().getRcpNumber());
		downtime.setEndFirstName(userHolder.getInfo().getFirstName());
		downtime.setEndLastName(userHolder.getInfo().getLastName());
		downtime.setEndPosition(userHolder.getInfo().getPosition());
		downtime.setEndDepartment(userHolder.getInfo().getDepartment());

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/downtimes/show/" + downtime.getId();
	}

	@RequestMapping(value = "/response/{id}")
	public String takeAction(@PathVariable("id") int id, Model model) throws NotFoundException {

		User current = userService.getAuthenticatedUser();
		if (current == null) {
			throw new NotFoundException("User not authenticated");
		}

		Downtime downtime = downtimesService.findById(id);
		if (downtime == null) {
			throw new NotFoundException("Downtime not found: " + id);
		}

		if (current.getId() == downtime.getCause().getResponsibleUser().getId()
				&& downtime.getResponseType().getOrder() <= 10) {
			model.addAttribute("modAllowed", true);
		} else {
			model.addAttribute("modAllowed", false);
		}

		model.addAttribute("formDowntimeClose", new FormDowntimeClose(downtime.getId()));

		return "downtimes/actions";
	}

	@RequestMapping(value = "/response", params = { "action" }, method = RequestMethod.POST)
	@Transactional
	public String response(@Valid FormDowntimeClose formDowntimeClose, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Locale locale, Model model, @RequestParam String action)
			throws NotFoundException {

		if (bindingResult.hasErrors()) {
			model.addAttribute("modAllowed", true);
			return "downtimes/actions";
		}
		
		DowntimeResponseType response;
		
		switch(action) {
			case "acc":
				response = responseTypeService.findByInternalTitle("accepted");
			break;
			case "fwd":
				response = responseTypeService.findByInternalTitle("forwarded");
				break;
			case "rjct":
				response = responseTypeService.findByInternalTitle("rejected");
				break;
			default:
				throw new NotFoundException("Unknown action: " + action);
		}
		
		if(response == null) {
			throw new NotFoundException("Response type not found: " + action);
		}
		
		Downtime dt = downtimesService.findById(formDowntimeClose.downtimeId);
		if(dt == null) {
			throw new NotFoundException("Downtime not found: " + formDowntimeClose.downtimeId);
		}
		
		dt.setResponseType(response);
		dt.setResponseComment(formDowntimeClose.getEndComment().trim());
		dt.setResponseDate(new Timestamp(new java.util.Date().getTime()));
		downtimesService.save(dt);

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/downtimes/show/" + formDowntimeClose.downtimeId;
	}

}
