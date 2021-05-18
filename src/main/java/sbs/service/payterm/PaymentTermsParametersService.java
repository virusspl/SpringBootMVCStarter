package sbs.service.payterm;

import java.util.Map;

import sbs.model.payterm.PaymentTermsParameter;
import sbs.service.GenericService;

public interface PaymentTermsParametersService  extends GenericService<PaymentTermsParameter, Integer> {

	public PaymentTermsParameter findByCode(String parameterCode);
	public Map<String, PaymentTermsParameter> getAllParametersMap();
	
	

}
