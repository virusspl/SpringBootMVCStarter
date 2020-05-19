package sbs.service.cebs;

import sbs.model.cebs.CebsEvent;
import sbs.service.GenericService;

public interface CebsEventsService extends GenericService<CebsEvent, Integer>{

	CebsEvent findActiveEvent();
	
}
