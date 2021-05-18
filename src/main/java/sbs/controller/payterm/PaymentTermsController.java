package sbs.controller.payterm;

import java.util.Locale;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import sbs.model.payterm.PaymentTermsParameter;
import sbs.model.x3.X3Client;
import sbs.scheduling.PaymentTermsScheduler;
import sbs.service.x3.JdbcOracleX3Service;

@Controller
@RequestMapping("payterm")
public class PaymentTermsController {

	@Autowired
	PaymentTermsBean termBean;
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	MessageSource messageSource;
	@Autowired
	PaymentTermsScheduler termsScheduler;

	public PaymentTermsController() {

	}

	@RequestMapping("/main")
	public String view(Model model, Locale locale) {
		String tmpDays;
		int days = -1;
		PaymentTermsForm paymentTermsForm = new PaymentTermsForm();
		Map<String, PaymentTermsParameter> params = termBean.getAllParametersMap();
		paymentTermsForm.setClientsList(
				params.containsKey("pay.param.client.list") ? params.get("pay.param.client.list").getValue() : "");
		paymentTermsForm.setMailingList(
				params.containsKey("pay.param.mailing.list") ? params.get("pay.param.mailing.list").getValue() : "");
		tmpDays = params.containsKey("pay.param.days.before") ? params.get("pay.param.days.before").getValue() : "";
		try {
			days = Integer.parseInt(tmpDays);
		} catch (NumberFormatException ex) {
			days = -1;
		} finally {
			paymentTermsForm.setDays(days);
		}

		model.addAttribute("paymentTermsForm", paymentTermsForm);
		return "payterm/main";
	}
	
	@RequestMapping("/notify")
	public String sendNotify(RedirectAttributes redirectAttrs, Locale locale) {
		termsScheduler.schedulePaymentNotifications();
		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("email.sent", null, locale));
		return "redirect:/payterm/main";
	}

	@RequestMapping(value = "/saveparams", params = { "save" }, method = RequestMethod.POST)
	public String findChains(@Valid PaymentTermsForm paymentTermsForm, BindingResult bindingResult,
			RedirectAttributes redirectAttrs, Model model, Locale locale) {

		if (bindingResult.hasErrors()) {
			return "payterm/main";
		}

		// mails format
		String[] mails = paymentTermsForm.getMailingList().split(";");
		String mailList = "";
		for (String mail : mails) {
			if(mail.trim().length()==0) {
				continue;
			}
			mailList += mail.trim().toLowerCase() + "; ";
		}
		// set only correct values to field
		paymentTermsForm.setMailingList(mailList);
		
		// clients additional check
		Map<String, X3Client> clientsDictionary = x3Service.findAllClientsMap("ATW");
		String[] clients = paymentTermsForm.getClientsList().split(";");
		String clientsErrorList = "";
		String clientsCorrectList = "";

		for (String cli : clients) {
			if(cli.trim().length()==0) {
				continue;
			}
			if (!clientsDictionary.containsKey(cli.trim().toUpperCase())) {
				clientsErrorList += cli.trim().toUpperCase() + "; ";
			} else {
				clientsCorrectList += cli.trim().toUpperCase() + "; ";
			}
		}

		// set only correct values to field
		paymentTermsForm.setClientsList(clientsCorrectList);
		
		if (clientsErrorList.length() > 0) {
			bindingResult.rejectValue("clientsList", "error.no.such.client", "ERROR");
			// show failed values as error
			model.addAttribute("error", clientsErrorList);
			return "payterm/main";
		}

		// save parameters
		termBean.saveParameters(clientsCorrectList, paymentTermsForm.getMailingList(), paymentTermsForm.getDays());

		redirectAttrs.addFlashAttribute("msg", messageSource.getMessage("action.saved", null, locale));
		return "redirect:/payterm/main";
	}

}
