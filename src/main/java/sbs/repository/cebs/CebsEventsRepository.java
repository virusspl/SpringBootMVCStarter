package sbs.repository.cebs;

import sbs.model.cebs.CebsEvent;
import sbs.repository.GenericRepository;

public interface CebsEventsRepository extends GenericRepository<CebsEvent,Integer> {

	CebsEvent findActiveEvent();

	
}

