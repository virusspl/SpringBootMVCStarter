package sbs.repository.cebs;

import java.util.List;

import sbs.model.cebs.CebsLine;
import sbs.repository.GenericRepository;

public interface CebsLinesRepository extends GenericRepository<CebsLine,Integer> {

	List<CebsLine> findByEventId(int eventId);

	CebsLine findByLongId(long longId);

}

