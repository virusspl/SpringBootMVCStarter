package sbs.repository.payterm;

import java.util.Map;

import sbs.model.payterm.PaymentTermsParameter;
import sbs.repository.GenericRepository;

public interface PaymentTermsParametersRepository extends GenericRepository<PaymentTermsParameter,Integer> {

	PaymentTermsParameter findByCode(String code);

	Map<String, PaymentTermsParameter> getAllParametersMap();

}