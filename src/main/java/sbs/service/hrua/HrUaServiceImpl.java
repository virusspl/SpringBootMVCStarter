package sbs.service.hrua;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.hruafiles.HrUaInfo;
import sbs.repository.GenericRepository;
import sbs.repository.hrua.HrUaRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class HrUaServiceImpl extends GenericServiceAdapter<HrUaInfo, Integer> implements HrUaService{
	
	@SuppressWarnings("unused")
	private HrUaRepository hrUaRepository;
	
    @Autowired
	public HrUaServiceImpl(@Qualifier("hrUaRepositoryImpl") GenericRepository<HrUaInfo, Integer> genericRepository) {
			super(genericRepository);
			this.hrUaRepository = (HrUaRepository) genericRepository;
	}

}