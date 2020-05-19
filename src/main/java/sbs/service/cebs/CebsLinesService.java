package sbs.service.cebs;

import java.util.List;

import sbs.model.cebs.CebsLine;
import sbs.service.GenericService;

public interface CebsLinesService extends GenericService<CebsLine, Integer>{

	List<CebsLine> findByEventId(int eventId);

	CebsLine findByLongId(long longId);
	
}
