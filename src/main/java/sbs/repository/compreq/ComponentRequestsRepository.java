package sbs.repository.compreq;

import java.util.List;

import sbs.model.compreq.ComponentRequest;
import sbs.repository.GenericRepository;

public interface ComponentRequestsRepository extends GenericRepository<ComponentRequest,Integer> {

	List<ComponentRequest> findAllPending();

	
}

