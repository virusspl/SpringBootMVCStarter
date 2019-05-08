package sbs.service.system;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sbs.model.system.SystemInfoParameter;
import sbs.repository.GenericRepository;
import sbs.repository.system.SystemInfoParametersRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class SystemInfoParametersServiceImpl extends GenericServiceAdapter<SystemInfoParameter, Integer> implements SystemInfoParametersService{
	
	private SystemInfoParametersRepository systemInfoParametersRepository;
	
    @Autowired
	public SystemInfoParametersServiceImpl(@Qualifier("systemInfoParametersRepositoryImpl") GenericRepository<SystemInfoParameter, Integer> genericRepository) {
			super(genericRepository);
			this.systemInfoParametersRepository = (SystemInfoParametersRepository) genericRepository;
	}

    
	@Override
	@Transactional
	public void storeSystemInfoParameter(String code, String description, String value) {
		SystemInfoParameter param = this.findByCode(code);
		if(param != null){
			param.setDescription(description);
			param.setValue(value);
			param.setDate(new Timestamp(new java.util.Date().getTime()));
		}
		else{
			param = new SystemInfoParameter(code, description, value, new java.util.Date());
		}
		
		this.saveOrUpdate(param);
	}

	@Override
	public SystemInfoParameter findByCode(String code) {
		return systemInfoParametersRepository.findByCode(code);
	}

    
}
