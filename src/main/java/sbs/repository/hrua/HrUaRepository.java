package sbs.repository.hrua;

import java.util.List;

import sbs.model.hruafiles.HrUaInfo;
import sbs.repository.GenericRepository;

public interface HrUaRepository extends GenericRepository<HrUaInfo,Integer> {

	List<HrUaInfo> findCurrent();

	List<HrUaInfo> findArchived();
	
}

