package sbs.service.cebs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.cebs.CebsLine;
import sbs.repository.GenericRepository;
import sbs.repository.cebs.CebsLinesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class CebsLinesServiceImpl extends GenericServiceAdapter<CebsLine, Integer> implements CebsLinesService {
	
	
	private CebsLinesRepository cebsLinesRepository;
	
    @Autowired
	public CebsLinesServiceImpl(@Qualifier("cebsLinesRepositoryImpl") GenericRepository<CebsLine, Integer> genericRepository) {
			super(genericRepository);
			this.cebsLinesRepository = (CebsLinesRepository) genericRepository;
	}

	@Override
	public List<CebsLine> findByEventId(int eventId) {
		return cebsLinesRepository.findByEventId(eventId);
	}

	@Override
	public CebsLine findByLongId(long longId) {
		return cebsLinesRepository.findByLongId(longId);
	}


}
