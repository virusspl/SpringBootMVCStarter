package sbs.service.compreq;

import java.util.List;

import sbs.model.compreq.ComponentRequest;
import sbs.service.GenericService;

public interface ComponentRequestsService extends GenericService<ComponentRequest, Integer>{

	List<ComponentRequest> findAllPending();

}
