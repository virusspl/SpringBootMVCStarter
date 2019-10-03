package sbs.service.compreq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.compreq.ComponentRequestLine;
import sbs.repository.GenericRepository;
import sbs.repository.compreq.ComponentRequestLinesRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class ComponentRequestLinesServiceImpl extends GenericServiceAdapter<ComponentRequestLine, Integer> implements ComponentRequestLinesService {
	
	@SuppressWarnings("unused")
	private ComponentRequestLinesRepository componentRequestLinesRepository;
	
    @Autowired
	public ComponentRequestLinesServiceImpl(@Qualifier("componentRequestLinesRepositoryImpl") GenericRepository<ComponentRequestLine, Integer> genericRepository) {
			super(genericRepository);
			this.componentRequestLinesRepository = (ComponentRequestLinesRepository) genericRepository;
	}


}
