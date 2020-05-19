package sbs.repository.cebs;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import sbs.model.cebs.CebsLine;
import sbs.repository.GenericRepositoryAdapter;

@Repository
@Transactional
public class CebsLinesRepositoryImpl extends GenericRepositoryAdapter<CebsLine, Integer>
		implements CebsLinesRepository {

	@Override
	public List<CebsLine> findByEventId(int eventId) {
		String hql = "from CebsLine r where r.cebsEvent.eventId = :evid";
		@SuppressWarnings("unchecked")
		List<CebsLine> result = (List<CebsLine>) 
		currentSession()
		.createQuery(hql)
		.setInteger("evid", eventId)
		.list();
			
		return result;		
	}

	@Override
	public CebsLine findByLongId(long longId) {
		String hql = "from CebsLine r where r.longId = :evid";
		@SuppressWarnings("unchecked")
		List<CebsLine> result = (List<CebsLine>) 
		currentSession()
		.createQuery(hql)
		.setLong("evid", longId)
		.list();
			
		if(!result.isEmpty()){
			return result.get(0);
		}
		else{
			return null;
		}
	}
	
}


