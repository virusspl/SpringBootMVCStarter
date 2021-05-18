package sbs.controller.payterm;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sbs.model.payterm.PaymentTermsParameter;
import sbs.model.x3.X3PaymentTerm;
import sbs.model.x3.X3SalesInvoice;
import sbs.service.payterm.PaymentTermsParametersService;
import sbs.service.x3.JdbcOracleX3Service;


@Component
public class PaymentTermsBean {
	@Autowired
	JdbcOracleX3Service x3Service;
	@Autowired
	PaymentTermsParametersService termsService;
	
	public Map<String, PaymentTermsParameter> getAllParametersMap() {
		return termsService.getAllParametersMap();
	}
	

	public List<X3SalesInvoice> getInvoicesList() {
		
		// days
		int days = 0;
		try{
			days = Integer.parseInt(this.getAllParametersMap().get("pay.param.days.before").getValue());
		}
		catch (NumberFormatException ex) {
		}
		
		List<X3SalesInvoice> result = new ArrayList<>();
		java.util.Date startDate;
		java.util.Date endDate;
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 5);
		endDate = cal.getTime();
		cal.add(Calendar.MONTH,-18);
		startDate = cal.getTime();
		
		// get terms list
		Map<String, X3PaymentTerm> payTerms = x3Service.getAllPaymentTerms("ATW");

		// get invoices in period
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		
		List<X3SalesInvoice> invoices = x3Service.getSalesInvoicesInPeriod(startDate, endDate, "ATW");
		Set<String> clients = this.getClientsSet();
		long timeDiff;
		
		X3PaymentTerm term; 
		for(X3SalesInvoice inv: invoices) {
			if(!clients.contains(inv.getClient())) {
				continue;
			}
			// get payment term for invoice
			term = payTerms.containsKey(inv.getPaymentTerms()) ? payTerms.get(inv.getPaymentTerms()) : null;
			if(term == null) {
				// unknown term
				continue;
			}
			// timeleft (expected - today) 
			timeDiff = term.getPaymentDate(inv.getPaymentStartDueDate()).getTime() - today.getTimeInMillis();
			
			if(timeDiff == days*24*60*60*1000) {
				result.add(inv);
			}
			
		}
		return result;
	}


	public void saveParameters(String clientsList, String mailingList, int days) {
		
		Map<String, PaymentTermsParameter> params = this.getAllParametersMap();
		PaymentTermsParameter param;
		// clients
		param = params.getOrDefault("pay.param.client.list", null);
		param.setValue(clientsList);
		termsService.update(param);

		// mailing list
		param = params.getOrDefault("pay.param.mailing.list", null);
		param.setValue(mailingList);
		termsService.update(param);		
		
		// days
		param = params.getOrDefault("pay.param.days.before", null);
		param.setValue(days+"");
		termsService.update(param);
		
	}


	public List<String> getMailingList() {
		List<String> list = new ArrayList<>();
		PaymentTermsParameter param = termsService.findByCode("pay.param.mailing.list");
		for(String mail: param.getValue().split(";")) {
			if(mail.trim().length()==0) {
				continue;
			}
			list.add(mail.trim().toLowerCase());
		}
		return list;
	}
	
	public Set<String> getClientsSet(){
		Set<String> set = new HashSet<>();
		PaymentTermsParameter param = termsService.findByCode("pay.param.client.list");
		for(String cli: param.getValue().split(";")) {
			if(cli.trim().length()==0) {
				continue;
			}
			set.add(cli.trim().toUpperCase());
		}
		return set;		
	}
	
	public int getDaysParameter() {
		PaymentTermsParameter param = termsService.findByCode("pay.param.days.before");
		return Integer.parseInt(param.getValue());
	}
	
	
	
	
	


}
