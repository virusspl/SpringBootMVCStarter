package sbs.repository.system;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.system.SystemInfoParameter;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class SystemInfoParametersRepositoryImpl extends GenericRepositoryAdapter<SystemInfoParameter, Integer>
		implements SystemInfoParametersRepository {

	@Override
	public SystemInfoParameter findByCode(String code) {
		String hql = "from SystemInfoParameter s where s.code = :cod";
		@SuppressWarnings("unchecked")
		List<SystemInfoParameter> result = (List<SystemInfoParameter>) 
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

	


}
