package sbs.service.payterm;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.payterm.PaymentTermsParameter;
import sbs.repository.GenericRepository;
import sbs.repository.payterm.PaymentTermsParametersRepository;
import sbs.service.GenericServiceAdapter;


@Service
public class PaymentTermsParametersServiceImpl extends GenericServiceAdapter<PaymentTermsParameter, Integer> implements PaymentTermsParametersService{
	
	private PaymentTermsParametersRepository paymentTermsParametersRepository;
	
	@Autowired
	public PaymentTermsParametersServiceImpl(@Qualifier("paymentTermsParametersRepositoryImpl") GenericRepository<PaymentTermsParameter, Integer> genericRepository) {
			super(genericRepository);
			this.paymentTermsParametersRepository = (PaymentTermsParametersRepository) genericRepository;
	}

	@Override
	public PaymentTermsParameter findByCode(String parameterCode) {
		return paymentTermsParametersRepository.findByCode(parameterCode);
	}

	@Override
	public Map<String, PaymentTermsParameter> getAllParametersMap() {
		return paymentTermsParametersRepository.getAllParametersMap();
	}
    
}