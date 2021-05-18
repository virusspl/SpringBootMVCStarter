package sbs.repository.payterm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.payterm.PaymentTermsParameter;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class PaymentTermsParametersRepositoryImpl extends GenericRepositoryAdapter<PaymentTermsParameter, Integer>
		implements PaymentTermsParametersRepository {

	@Override
	public PaymentTermsParameter findByCode(String code) {
		String hql = "from PaymentTermsParameter s where s.code = :cod";
		@SuppressWarnings("unchecked")
		List<PaymentTermsParameter> result = (List<PaymentTermsParameter>) 
		currentSession()
		.createQuery(hql)
		.setString("cod", code)
		.list();
		
		if(!result.isEmpty()){
			return result.get(0);
		}
		else{
			return null;
		}
	}

	@Override
	public Map<String, PaymentTermsParameter> getAllParametersMap() {
		Map<String, PaymentTermsParameter> map = new HashMap<>();
		String hql = "from PaymentTermsParameter s";
		@SuppressWarnings("unchecked")
		List<PaymentTermsParameter> result = (List<PaymentTermsParameter>) 
		currentSession()
		.createQuery(hql)
		.list();
		
		for(PaymentTermsParameter param: result) {
			map.put(param.getCode(), param);
		}
		
		return map;
	}
	
	
}
