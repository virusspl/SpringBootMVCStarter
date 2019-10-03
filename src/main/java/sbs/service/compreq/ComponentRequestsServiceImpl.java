package sbs.service.compreq;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sbs.model.compreq.ComponentRequest;
import sbs.repository.GenericRepository;
import sbs.repository.compreq.ComponentRequestsRepository;
import sbs.service.GenericServiceAdapter;

@Service
public class ComponentRequestsServiceImpl extends GenericServiceAdapter<ComponentRequest, Integer> implements ComponentRequestsService {
	
	private ComponentRequestsRepository componentRequestsRepository;
	
    @Autowired
	public ComponentRequestsServiceImpl(@Qualifier("componentRequestsRepositoryImpl") GenericRepository<ComponentRequest, Integer> genericRepository) {
			super(genericRepository);
			this.componentRequestsRepository = (ComponentRequestsRepository) genericRepository;
	}

	@Override
	public List<ComponentRequest> findAllPending() {
		return componentRequestsRepository.findAllPending();
	}


}
